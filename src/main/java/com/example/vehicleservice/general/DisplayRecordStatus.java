package com.example.vehicleservice.general;

public enum DisplayRecordStatus {
    APPROVED, WRONG, UPDATED, USER;

    public String getStatus() {
        return switch (this) {
            case APPROVED -> "approved";
            case UPDATED -> "updated";
            case WRONG -> "wrong";
            case USER -> "user";
            default -> null;

        };
    }
}
