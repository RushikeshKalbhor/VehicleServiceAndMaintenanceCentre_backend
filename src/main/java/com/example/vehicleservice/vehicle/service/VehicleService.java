package com.example.vehicleservice.vehicle.service;

import com.example.vehicleservice.admin.repository.UserRepository;
import com.example.vehicleservice.config.security.UserDetail;
import com.example.vehicleservice.general.json.ResponseJson;
import com.example.vehicleservice.mechanic.records.MechanicRecord;
import com.example.vehicleservice.vehicle.json.VehiclePostJson;
import com.example.vehicleservice.vehicle.model.Vehicle;
import com.example.vehicleservice.vehicle.repository.VehicleRepository;
import com.example.vehicleservice.vehicle.util.VehicleUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleUtil vehicleUtil;
    private final UserRepository userRepository;

    public VehicleService(VehicleRepository vehicleRepository, VehicleUtil vehicleUtil, UserRepository userRepository) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleUtil = vehicleUtil;
        this.userRepository = userRepository;
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

    public ResponseJson getAllVehicles(Integer pageNumber) {
        pageNumber = pageNumber == null ? 1 : pageNumber;
        Pageable pageable = PageRequest.of(pageNumber - 1, 10);
        List<Vehicle> vehicleList = vehicleRepository.findVehicleByVehRecordStatus(pageable);
        if (vehicleList.isEmpty()) {
            return new ResponseJson("vehicle.not.found");
        }

        List<String> username = vehicleList.stream().map(Vehicle :: getVehUseUsername).distinct().toList();
        List<MechanicRecord> usernameList = userRepository.findUserNameRecordByUsernameList(username);

        List<Map<String, Object>> finalVehicleList =  new ArrayList<>();
        for (Vehicle vehicle : vehicleList) {
            Map<String, Object> vehicleMap = new HashMap<>();
            vehicleMap.put("vehId", vehicle.getVehId());
            vehicleMap.put("vehBrand", vehicle.getVehBrand());
            vehicleMap.put("vehCreated", vehicle.getVehCreated());
            vehicleMap.put("vehManufacturingYear", vehicle.getVehManufacturingYear());
            vehicleMap.put("vehModel", vehicle.getVehModel());
            vehicleMap.put("vehRecordStatus", vehicle.getVehRecordStatus());
            vehicleMap.put("vehUseUsername", vehicle.getVehUseUsername());
            vehicleMap.put("vehVehicleNumber", vehicle.getVehVehicleNumber());
            vehicleMap.put("vehVehicleType", vehicle.getVehVehicleType());
            for (MechanicRecord userRecord : usernameList) {
                if (userRecord.useUsername().equals(vehicle.getVehUseUsername())) {
                    vehicleMap.put("userFullName", concatUserFullname(userRecord.useTitle(), userRecord.useFirstName(), userRecord.useSurname()));
                }
            }
            finalVehicleList.add(vehicleMap);
        }
        Map<String, Object> entityMap = new HashMap<>();
        entityMap.put("finalVehicleList", finalVehicleList);

        if (pageNumber == 1) {
            Integer vehicleCount = vehicleRepository.findVehicleByVehRecordStatusAndPageNumber();
            entityMap.put("vehicleCount", vehicleCount);
        }
        return new ResponseJson("vehicle.found", entityMap);
    }

    public String concatUserFullname(String useTitle, String useFirstname, String useSurname) {
        return (Stream.of(useTitle, useFirstname, useSurname)
                .filter(Objects::nonNull)
                .filter(s -> !s.trim().isEmpty())
                .collect(Collectors.joining(" ")));
    }

    public ResponseJson duplicateVehicleCheck(String vehVehicleNumber) {
        boolean existVehicle = vehicleRepository.existVehicleByVehVehicleNumberAndVehRecordStatus(vehVehicleNumber);
        return new ResponseJson(existVehicle ? "vehicle.already.registered" : "vehicle.not.found");
    }

    @Transactional
    public ResponseJson deleteVehicle(Integer vehId) {
        int deletedVehicle = vehicleRepository.deleteVehicleByVehId(vehId);
        return new ResponseJson(deletedVehicle == 0 ? "vehicle.delete.fail" : "vehicle.delete.success");
    }
}
