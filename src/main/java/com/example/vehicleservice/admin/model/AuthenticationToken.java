package com.example.vehicleservice.admin.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "authentication_tokens")
public class AuthenticationToken {

    @Column(name = "atk_id", precision = 10, nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer atkId;

    @Column(name = "atk_use_username", length=40)
    private String atkUseUsername;

    @Column(name = "atk_jwt_token", length=3000)
    private String atkJwtToken;

    @Column(name = "atk_refresh_token", length=255)
    private String atkRefreshToken;

    @Column(name = "atk_uuid", length = 50)
    private String	atkUuid;

    @Column(name = "atk_created", nullable = false)
    private LocalDateTime atkCreated;

    public LocalDateTime getAtkCreated() {
        return atkCreated;
    }

    public void setAtkCreated(LocalDateTime atkCreated) {
        this.atkCreated = atkCreated;
    }

    public Integer getAtkId() {
        return atkId;
    }

    public void setAtkId(Integer atkId) {
        this.atkId = atkId;
    }

    public String getAtkJwtToken() {
        return atkJwtToken;
    }

    public void setAtkJwtToken(String atkJwtToken) {
        this.atkJwtToken = atkJwtToken;
    }

    public String getAtkRefreshToken() {
        return atkRefreshToken;
    }

    public void setAtkRefreshToken(String atkRefreshToken) {
        this.atkRefreshToken = atkRefreshToken;
    }

    public String getAtkUseUsername() {
        return atkUseUsername;
    }

    public void setAtkUseUsername(String atkUseUsername) {
        this.atkUseUsername = atkUseUsername;
    }

    public String getAtkUuid() {
        return atkUuid;
    }

    public void setAtkUuid(String atkUuid) {
        this.atkUuid = atkUuid;
    }
}