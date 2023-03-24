package com.tananushka.petshop.model;

public class OrderReservationRequest {
  private String sessionId;
  private String orderJSON;

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public String getOrderJSON() {
    return orderJSON;
  }

  public void setOrderJSON(String orderJSON) {
    this.orderJSON = orderJSON;
  }

  @Override
  public String toString() {
    return "OrderReservationRequest{" +
      "sessionId='" + sessionId + '\'' +
      ", orderJSON='" + orderJSON + '\'' +
      '}';
  }
}
