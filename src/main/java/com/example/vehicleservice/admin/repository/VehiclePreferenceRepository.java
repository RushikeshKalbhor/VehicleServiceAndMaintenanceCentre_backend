package com.example.vehicleservice.admin.repository;

import com.example.vehicleservice.admin.model.VehiclePreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiclePreferenceRepository extends JpaRepository<VehiclePreference, Integer> {

    @Query("SELECT vepValue FROM VehiclePreference  WHERE vepName=:vepName")
    String findVehiclePreferenceCecValueByCecName(String vepName);
}
