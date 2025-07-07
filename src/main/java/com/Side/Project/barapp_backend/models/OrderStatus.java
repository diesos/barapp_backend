package com.Side.Project.barapp_backend.models;

public enum OrderStatus {
  ORDERED("Commandée"),
  IN_PROGRESS("En cours de préparation"),
  COMPLETED("Terminée");

  private final String displayName;

  OrderStatus(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}