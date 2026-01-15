package com.example.vehicleservice.admin.repository;

import com.example.vehicleservice.admin.model.VehicleGlitch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleGlitchRepository extends JpaRepository<VehicleGlitch, Integer> {
//
}
