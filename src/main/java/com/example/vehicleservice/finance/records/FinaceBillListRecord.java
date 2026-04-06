package com.example.vehicleservice.finance.records;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record FinaceBillListRecord(Integer vehId, String vehVehicleNumber, String vehVehicleType, String vehModel,
                                   Integer aptId, LocalDate aptDate, String aptStatus, LocalDateTime aptCreated,
                                   String custTitle, String custFirstName, String custSurname,
                                   String mechanicTitle, String mechanicFirstName, String mechanicSurname, Integer jcId, Integer bId) {
    //
}
