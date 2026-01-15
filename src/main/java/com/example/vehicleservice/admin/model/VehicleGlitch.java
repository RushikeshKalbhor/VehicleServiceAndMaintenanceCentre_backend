package com.example.vehicleservice.admin.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_glitches")
public class VehicleGlitch {

    @Column(name = "veg_id", precision = 10, nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer vegId;

    @Column(name = "veg_use_username", length=40)
    private String vegUseUsername;

    @Column(name = "veg_cause")
    private String vegCause;

    @Column(name = "veg_file_name", length=255)
    private String vegFileName;

    @Column(name = "veg_line_number", precision = 10)
    private Integer vegLineNumber;

    @Column(name = "veg_navigation_area", length=100)
    private String vegNavigationArea;

    @Column(name = "veg_stack_trace")
    private String vegStackTrace;

    @Column(name = "veg_created")
    private LocalDateTime vegCreated;

    @Column(name = "veg_created_date")
    private LocalDate vegCreatedDate;

    @Column(name = "veg_resolved", precision = 1)
    private Byte vegResolved;

    public Integer getVegId() {
        return vegId;
    }

    public void setVegId(Integer vegId) {
        this.vegId = vegId;
    }

    public String getVegUseUsername() {
        return vegUseUsername;
    }

    public void setVegUseUsername(String vegUseUsername) {
        this.vegUseUsername = vegUseUsername;
    }

    public String getVegCause() {
        return vegCause;
    }

    public void setVegCause(String vegCause) {
        this.vegCause = vegCause;
    }

    public String getVegFileName() {
        return vegFileName;
    }

    public void setVegFileName(String vegFileName) {
        this.vegFileName = vegFileName;
    }

    public Integer getVegLineNumber() {
        return vegLineNumber;
    }

    public void setVegLineNumber(Integer vegLineNumber) {
        this.vegLineNumber = vegLineNumber;
    }

    public String getVegNavigationArea() {
        return vegNavigationArea;
    }

    public void setVegNavigationArea(String vegNavigationArea) {
        this.vegNavigationArea = vegNavigationArea;
    }

    public String getVegStackTrace() {
        return vegStackTrace;
    }

    public void setVegStackTrace(String vegStackTrace) {
        this.vegStackTrace = vegStackTrace;
    }

    public LocalDateTime getVegCreated() {
        return vegCreated;
    }

    public void setVegCreated(LocalDateTime vegCreated) {
        this.vegCreated = vegCreated;
    }

    public LocalDate getVegCreatedDate() {
        return vegCreatedDate;
    }

    public void setVegCreatedDate(LocalDate vegCreatedDate) {
        this.vegCreatedDate = vegCreatedDate;
    }

    public Byte getVegResolved() {
        return vegResolved;
    }

    public void setVegResolved(Byte vegResolved) {
        this.vegResolved = vegResolved;
    }
}