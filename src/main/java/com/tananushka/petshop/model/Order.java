package com.tananushka.petshop.model;

import java.time.OffsetDateTime;
import java.util.List;

public class Order {

  private String id;
  private String email;
  private List<Product> products;
  private OffsetDateTime shipDate;
  private List<Tag> tags;
  private Status status;
  private Boolean complete;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<Product> getProducts() {
    return products;
  }

  public void setProducts(List<Product> products) {
    this.products = products;
  }

  public OffsetDateTime getShipDate() {
    return shipDate;
  }

  public void setShipDate(OffsetDateTime shipDate) {
    this.shipDate = shipDate;
  }

  public List<Tag> getTags() {
    return tags;
  }

  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Boolean getComplete() {
    return complete;
  }

  public void setComplete(Boolean complete) {
    this.complete = complete;
  }

  @Override
  public String toString() {
    return "Order{" +
      "id='" + id + '\'' +
      ", email='" + email + '\'' +
      ", products=" + products +
      ", shipDate=" + shipDate +
      ", tags=" + tags +
      ", status=" + status +
      ", complete=" + complete +
      '}';
  }
}
