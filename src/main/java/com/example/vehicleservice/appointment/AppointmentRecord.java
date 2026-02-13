package com.example.vehicleservice.appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AppointmentRecord(Integer aptId, String aptStatus, String aptProblemDescription, String aptMechanic, Integer aptVehId,
                                LocalDate aptDate, String aptCustomer, LocalDateTime aptCreated, String vehVehicleNumber, String custTitle, String custFirstName, String custSurname,
                                String mechanicTitle, String mechanicFirstName, String mechanicSurname) {
    //
}
