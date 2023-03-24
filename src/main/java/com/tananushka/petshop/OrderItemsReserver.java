package com.tananushka.petshop;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.tananushka.petshop.model.OrderReservationRequest;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.lang.String.format;

public class OrderItemsReserver {

  @FunctionName("OrderItemsReserver")
  public HttpResponseMessage run(
    @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<OrderReservationRequest>> request,
    final ExecutionContext context) {

    context.getLogger().info("OrderItemsReserver: Starting...");

    try {
      OrderReservationRequest reservationRequest = request.getBody().filter(r -> !r.getSessionId().isBlank()).orElseThrow(() -> new IllegalArgumentException("Empty body"));

      context.getLogger().info(format("OrderItemsReserver: new request=%s", reservationRequest));

      uploadToStorage(context, reservationRequest);

      return request.createResponseBuilder(HttpStatus.OK)
        .body("Order successfully placed in storage").build();
    } catch (Exception e) {
      context.getLogger().severe("Error while replacing order: " + e);
      return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
        .body(e.getMessage()).build();
    }
  }

  private void uploadToStorage(ExecutionContext context, OrderReservationRequest request) {
    context.getLogger().info("OrderItemsReserver: Retrieving connection string and container name...");
    String connectionString = Optional.ofNullable(
        System.getenv("AZURE_STORAGE_CONNECTION_STRING"))
      .orElseThrow(() -> new NoSuchElementException("AZURE_STORAGE_CONNECTION_STRING is not set"));
    String containerName = Optional.ofNullable(
        System.getenv("AZURE_STORAGE_CONTAINER_NAME"))
      .orElseThrow(() -> new NoSuchElementException("AZURE_STORAGE_CONTAINER_NAME is not set"));

    String fileName = format("%s.json", request.getSessionId());
    String orderJSON = request.getOrderJSON();

    try {
      context.getLogger().info("OrderItemsReserver: Building container client...");
      BlobContainerClient containerClient = new BlobContainerClientBuilder()
        .connectionString(connectionString)
        .containerName(containerName)
        .buildClient();

      if (!containerClient.exists()) {
        context.getLogger().info(format("OrderItemsReserver: Container %s is absent. Creating container...", containerName));
        containerClient.create();
      }

      context.getLogger().info("OrderItemsReserver: Building blob client...");
      BlobClient blobClient = new BlobClientBuilder()
        .connectionString(connectionString)
        .containerName(containerName)
        .blobName(fileName)
        .buildClient();

      if (Boolean.TRUE.equals(blobClient.exists())) {
        context.getLogger().info(format("OrderItemsReserver: Blob=%s already exists and will be replaced", fileName));
      }

      BinaryData binaryData = BinaryData.fromString(orderJSON);

      context.getLogger().info(format("OrderItemsReserver: Uploading %s to blob storage...", fileName));
      blobClient.upload(binaryData.toStream(), orderJSON.length(), true);
    } catch (Exception e) {
      throw new RuntimeException("Error while uploading data to blob storage", e);
    }
  }
}
