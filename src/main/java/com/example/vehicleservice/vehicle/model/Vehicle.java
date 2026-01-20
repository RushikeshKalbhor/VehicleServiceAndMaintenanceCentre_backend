package com.example.vehicleservice.vehicle.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "veh_id", precision = 8, nullable = false)
    private Integer vehId;

    @Column(name = "veh_vehicle_number", nullable = false)
    private String vehVehicleNumber;

    @Column(name = "veh_vehicle_type", nullable = false)
    private String vehVehicleType;     // Car / Bike

    @Column(name = "veh_brand", nullable = false)
    private String vehBrand;

    @Column(name = "veh_model", nullable = false)
    private String vehModel;

    @Column(name = "veh_manufacturing_year")
    private Integer vehManufacturingYear;

    @Column(name = "veh_use_username", nullable = false)
    private String vehUseUsername;

    @Column(name = "veh_created", nullable = false)
    private java.time.LocalDateTime vehCreated;

    @Column(name = "veh_record_status", nullable = false)
    private String vehRecordStatus;

    public Integer getVehId() {
        return vehId;
    }

    public void setVehId(Integer vehId) {
        this.vehId = vehId;
    }

    public String getVehVehicleNumber() {
        return vehVehicleNumber;
    }

    public void setVehVehicleNumber(String vehVehicleNumber) {
        this.vehVehicleNumber = vehVehicleNumber;
    }

    public String getVehVehicleType() {
        return vehVehicleType;
    }

    public void setVehVehicleType(String vehVehicleType) {
        this.vehVehicleType = vehVehicleType;
    }

    public String getVehBrand() {
        return vehBrand;
    }

    public void setVehBrand(String vehBrand) {
        this.vehBrand = vehBrand;
    }

    public String getVehModel() {
        return vehModel;
    }

    public void setVehModel(String vehModel) {
        this.vehModel = vehModel;
    }

    public Integer getVehManufacturingYear() {
        return vehManufacturingYear;
    }

    public void setVehManufacturingYear(Integer vehManufacturingYear) {
        this.vehManufacturingYear = vehManufacturingYear;
    }

    public String getVehUseUsername() {
        return vehUseUsername;
    }

    public void setVehUseUsername(String vehUseUsername) {
        this.vehUseUsername = vehUseUsername;
    }

    public LocalDateTime getVehCreated() {
        return vehCreated;
    }

    public void setVehCreated(LocalDateTime vehCreated) {
        this.vehCreated = vehCreated;
    }

    public String getVehRecordStatus() {
        return vehRecordStatus;
    }

    public void setVehRecordStatus(String vehRecordStatus) {
        this.vehRecordStatus = vehRecordStatus;
    }
}
