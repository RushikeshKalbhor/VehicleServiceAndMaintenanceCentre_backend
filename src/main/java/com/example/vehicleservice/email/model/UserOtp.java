package com.example.vehicleservice.email.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_otp")
public class UserOtp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uo_id", precision = 8, nullable = false)
    private Integer uoId;

    @Column(name = "uo_use_username", nullable = false)
    private String uoUseUsername;

    @Column(name = "uo_otp", nullable = false)
    private String uoOtp;

    @Column(name = "uo_expiry_time", nullable = false)
    private LocalDateTime uoExpiryTime;

    @Column(name = "uo_created", nullable = false)
    private LocalDateTime uoCreated;

    public Integer getUoId() {
        return uoId;
    }

    public void setUoId(Integer uoId) {
        this.uoId = uoId;
    }

    public String getUoUseUsername() {
        return uoUseUsername;
    }

    public void setUoUseUsername(String uoUseUsername) {
        this.uoUseUsername = uoUseUsername;
    }

    public String getUoOtp() {
        return uoOtp;
    }

    public void setUoOtp(String uoOtp) {
        this.uoOtp = uoOtp;
    }

    public LocalDateTime getUoExpiryTime() {
        return uoExpiryTime;
    }

    public void setUoExpiryTime(LocalDateTime uoExpiryTime) {
        this.uoExpiryTime = uoExpiryTime;
    }

    public LocalDateTime getUoCreated() {
        return uoCreated;
    }

    public void setUoCreated(LocalDateTime uoCreated) {
        this.uoCreated = uoCreated;
    }
}
