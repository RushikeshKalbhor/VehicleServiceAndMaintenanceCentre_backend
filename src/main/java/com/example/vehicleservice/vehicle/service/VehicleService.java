package com.example.vehicleservice.vehicle.service;

import com.example.vehicleservice.config.security.UserDetail;
import com.example.vehicleservice.general.json.ResponseJson;
import com.example.vehicleservice.vehicle.json.VehiclePostJson;
import com.example.vehicleservice.vehicle.model.Vehicle;
import com.example.vehicleservice.vehicle.repository.VehicleRepository;
import com.example.vehicleservice.vehicle.util.VehicleUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleUtil vehicleUtil;

    public VehicleService(VehicleRepository vehicleRepository, VehicleUtil vehicleUtil) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleUtil = vehicleUtil;
    }

    public ResponseJson addVehicle(VehiclePostJson vehiclePostJson) {
        vehicleUtil.addVehicle(vehiclePostJson);
        return new ResponseJson("vehicle.add.success");
    }

    public ResponseJson getMyVehicles() {
        UserDetail userDetails = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Vehicle> vehicleList = vehicleRepository.findVehicleByVehUseUsername(userDetails.getUsername());
        if (vehicleList.isEmpty()) {
            return new ResponseJson("vehicle.not.found");
        }
        return new ResponseJson("vehicle.found", vehicleList);
    }

    public ResponseJson getAllVehicles() {
        List<Vehicle> vehicleList = vehicleRepository.findVehicleByVehRecordStatus();
        if (vehicleList.isEmpty()) {
            return new ResponseJson("vehicle.not.found");
        }
        return new ResponseJson("vehicle.found", vehicleList);
    }

    public ResponseJson duplicateVehicleCheck(String vehVehicleNumber) {
        boolean existVehicle = vehicleRepository.existVehicleByVehVehicleNumberAndVehRecordStatus(vehVehicleNumber);
        return new ResponseJson(existVehicle ? "vehicle.already.registered" : "vehicle.not.found");
    }
}
