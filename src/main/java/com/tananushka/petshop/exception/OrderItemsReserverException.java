package com.tananushka.petshop.exception;

public class OrderItemsReserverException extends RuntimeException {
  public OrderItemsReserverException() {
  }

  public OrderItemsReserverException(String message) {
    super(message);
  }

  public OrderItemsReserverException(String message, Throwable cause) {
    super(message, cause);
  }

  public OrderItemsReserverException(Throwable cause) {
    super(cause);
  }

  public OrderItemsReserverException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
