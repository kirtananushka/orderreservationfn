package com.tananushka.petshop.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
  PLACED("placeed"),
  APPROVED("approved"),
  DELIVERED("delivered");

  private final String value;

  Status(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static Status fromValue(String text) {
    for (Status b : Status.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
