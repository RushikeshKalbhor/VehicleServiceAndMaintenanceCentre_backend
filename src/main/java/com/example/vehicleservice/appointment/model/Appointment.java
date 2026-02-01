package com.example.vehicleservice.appointment.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apt_id", nullable = false)
    private Integer aptId;

    @Column(name = "apt_date", nullable = false)
    private LocalDate aptDate;

    @Column(name = "apt_problem_description")
    private String aptProblemDescription;

    //PENDING, APPROVED, REJECTED, ASSIGNED
    @Column(name = "apt_status", nullable = false)
    private String aptStatus;

    @Column(name = "apt_customer", nullable = false)
    private String aptCustomer;

    @JoinColumn(name = "apt_veh_id", nullable = false)
    private Integer aptVehId;

    @Column(name = "apt_mechanic")
    private String aptMechanic;

    @Column(name = "apt_created", nullable = false)
    private LocalDateTime aptCreated;

    @Column(name = "apt_record_status", nullable = false)
    private String aptRecordStatus;

    public Integer getAptId() {
        return aptId;
    }

    public void setAptId(Integer aptId) {
        this.aptId = aptId;
    }

    public LocalDate getAptDate() {
        return aptDate;
    }

    public void setAptDate(LocalDate aptDate) {
        this.aptDate = aptDate;
    }

    public String getAptProblemDescription() {
        return aptProblemDescription;
    }

    public void setAptProblemDescription(String aptProblemDescription) {
        this.aptProblemDescription = aptProblemDescription;
    }

    public String getAptStatus() {
        return aptStatus;
    }

    public void setAptStatus(String aptStatus) {
        this.aptStatus = aptStatus;
    }

    public String getAptCustomer() {
        return aptCustomer;
    }

    public void setAptCustomer(String aptCustomer) {
        this.aptCustomer = aptCustomer;
    }

    public Integer getAptVehId() {
        return aptVehId;
    }

    public void setAptVehId(Integer aptVehId) {
        this.aptVehId = aptVehId;
    }

    public String getAptMechanic() {
        return aptMechanic;
    }

    public void setAptMechanic(String aptMechanic) {
        this.aptMechanic = aptMechanic;
    }

    public LocalDateTime getAptCreated() {
        return aptCreated;
    }

    public void setAptCreated(LocalDateTime aptCreated) {
        this.aptCreated = aptCreated;
    }

    public String getAptRecordStatus() {
        return aptRecordStatus;
    }

    public void setAptRecordStatus(String aptRecordStatus) {
        this.aptRecordStatus = aptRecordStatus;
    }
}
