package com.tananushka.petshop.exception;

public class StorageUploadingException extends RuntimeException {
  public StorageUploadingException() {
  }

  public StorageUploadingException(String message) {
    super(message);
  }

  public StorageUploadingException(String message, Throwable cause) {
    super(message, cause);
  }

  public StorageUploadingException(Throwable cause) {
    super(cause);
  }

  public StorageUploadingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
