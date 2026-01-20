package com.example.vehicleservice.vehicle.util;

import com.example.vehicleservice.config.security.UserDetail;
import com.example.vehicleservice.vehicle.json.VehiclePostJson;
import com.example.vehicleservice.vehicle.model.Vehicle;
import com.example.vehicleservice.vehicle.repository.VehicleRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class VehicleUtil {

    private final VehicleRepository vehicleRepository;

    public VehicleUtil(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public void addVehicle(VehiclePostJson vehiclePostJson) {
        UserDetail userDetails = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Vehicle vehicle = new Vehicle();
        vehicle.setVehVehicleNumber(vehiclePostJson.getVehVehicleNumber());
        vehicle.setVehVehicleType(vehiclePostJson.getVehVehicleType());
        vehicle.setVehBrand(vehiclePostJson.getVehBrand());
        vehicle.setVehModel(vehiclePostJson.getVehModel());
        vehicle.setVehManufacturingYear(vehiclePostJson.getVehManufacturingYear());
        vehicle.setVehUseUsername(userDetails.getUsername());
        vehicle.setVehCreated(LocalDateTime.now());
        vehicle.setVehRecordStatus("approved");
        vehicleRepository.save(vehicle);
    }

}
