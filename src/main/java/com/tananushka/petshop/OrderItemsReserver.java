package com.tananushka.petshop;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.ServiceBusQueueTrigger;
import com.tananushka.petshop.exception.OrderItemsReserverException;
import com.tananushka.petshop.exception.StorageUploadingException;
import com.tananushka.petshop.model.Order;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.lang.String.format;

public class OrderItemsReserver {

  @FunctionName("OrderItemsReserver")
  public void run(
    @ServiceBusQueueTrigger(
      name = "msg",
      queueName = "petstoreorderqueue",
      connection = "SERVICE_BUS_CONNECTION_STRING")
    String message, final ExecutionContext context) {

    try {
      context.getLogger().info("OrderItemsReserver: Starting...");
      Order order = new ObjectMapper().readValue(message, Order.class);
      String id = Optional.ofNullable(order.getId())
        .orElseThrow(() -> new IllegalArgumentException("OrderItemsReserver: order id is null"));
      uploadToStorage(context, message, id);
      context.getLogger().info("OrderItemsReserver: Success.");
    } catch (StorageUploadingException sue) {
      throw new OrderItemsReserverException(sue);
    } catch (Exception e) {
      throw new OrderItemsReserverException("Error while replacing order: ", e);
    }
  }

  private void uploadToStorage(ExecutionContext context, String orderJSON, String id) {
    context.getLogger().info("OrderItemsReserver: Retrieving connection string and container name...");
    String connectionString = Optional.ofNullable(
        System.getenv("AZURE_STORAGE_CONNECTION_STRING"))
      .orElseThrow(() -> new NoSuchElementException("AZURE_STORAGE_CONNECTION_STRING is not set"));
    String containerName = Optional.ofNullable(
        System.getenv("AZURE_STORAGE_CONTAINER_NAME"))
      .orElseThrow(() -> new NoSuchElementException("AZURE_STORAGE_CONTAINER_NAME is not set"));

    String fileName = format("%s.json", id);

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
      throw new StorageUploadingException("Error while uploading data to blob storage", e);
    }
  }
}
