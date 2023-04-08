package com.tananushka.petshop.model;

public class Product {

  private Long id;
  private Integer quantity;
  private String name;
  private String photoURL;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhotoURL() {
    return photoURL;
  }

  public void setPhotoURL(String photoURL) {
    this.photoURL = photoURL;
  }

  @Override
  public String toString() {
    return "Product{" +
      "id=" + id +
      ", quantity=" + quantity +
      ", name='" + name + '\'' +
      ", photoURL='" + photoURL + '\'' +
      '}';
  }
}
