package com.example.vehicleservice.admin.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "blocking_ip_address_details")
public class BlockingIpAddressDetail {

    @Column(name = "bpa_id", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private java.lang.Integer	bpaId;

    @Column(name = "bpa_type", nullable = false)
    private String	bpaType;

    @Column(name = "bpa_blocked_till_time", nullable = false)
    private LocalDateTime bpaBlockedTillTime;

    @Column(name = "bpa_ip_address", length = 15, nullable = false)
    private java.lang.String	bpaIpAddress;

    @Column(name = "bpa_created", nullable = false)
    private java.time.LocalDateTime	bpaCreated;

    public java.lang.Integer getBpaId() {
        return bpaId;
    }
    public void setBpaId(java.lang.Integer bpaId) {
        this.bpaId = bpaId;
    }
    public String getBpaType() {
        return bpaType;
    }
    public void setBpaType(String bpaType) {
        this.bpaType = bpaType;
    }
    public LocalDateTime getBpaBlockedTillTime() {
        return bpaBlockedTillTime;
    }
    public void setBpaBlockedTillTime(LocalDateTime blockedTillDateTime) {
        this.bpaBlockedTillTime = blockedTillDateTime;
    }
    public java.lang.String getBpaIpAddress() {
        return bpaIpAddress;
    }
    public void setBpaIpAddress(java.lang.String bpaIpAddress) {
        this.bpaIpAddress = bpaIpAddress;
    }
    public java.time.LocalDateTime getBpaCreated() {
        return bpaCreated;
    }
    public void setBpaCreated(java.time.LocalDateTime bpaCreated) {
        this.bpaCreated = bpaCreated;
    }
}
