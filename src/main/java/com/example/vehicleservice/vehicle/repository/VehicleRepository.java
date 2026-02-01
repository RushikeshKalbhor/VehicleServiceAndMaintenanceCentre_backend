package com.example.vehicleservice.vehicle.repository;

import com.example.vehicleservice.vehicle.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

    @Query("FROM Vehicle WHERE vehUseUsername = :vehUseUsername AND vehRecordStatus = 'approved'")
    List<Vehicle> findVehicleByVehUseUsername(String vehUseUsername);

    @Query("FROM Vehicle WHERE vehRecordStatus = 'approved'")
    List<Vehicle> findVehicleByVehRecordStatus();

    @Query("SELECT COUNT(vehId) > 0 FROM Vehicle WHERE vehVehicleNumber = :vehVehicleNumber AND vehRecordStatus = 'approved'")
    boolean existVehicleByVehVehicleNumberAndVehRecordStatus(String vehVehicleNumber);

    @Modifying
    @Query("UPDATE Vehicle SET vehRecordStatus = 'wrong' WHERE vehId = :vehId AND vehRecordStatus = 'approved'")
    int deleteVehicleByVehId(Integer vehId);
}
