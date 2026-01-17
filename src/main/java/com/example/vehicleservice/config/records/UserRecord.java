package com.example.vehicleservice.config.records;

import java.time.LocalDate;

public record UserRecord(String useUsername, Byte useActive, LocalDate usePasswordLastModified) {
    //
}
