package com.example.vehicleservice.admin.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "failed_logins")
public class FailedLogin {

    @Column(name = "flo_id", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer	floId;

    @Column(name = "flo_type", nullable = false)
    private String	floType;

    @Column(name = "flo_ip_address", length = 15, nullable = false)
    private String floIpAddress;

    @Column(name = "flo_created", nullable = false)
    private LocalDateTime floCreated;

    @Column(name = "flo_notes", length = 255, nullable = false)
    private String	floNotes;

    public java.lang.Integer getFloId() {
        return floId;
    }
    public void setFloId(java.lang.Integer floId) {
        this.floId = floId;
    }
    public String getFloType() {
        return floType;
    }
    public void setFloType(String floType) {
        this.floType = floType;
    }
    public java.lang.String getFloIpAddress() {
        return floIpAddress;
    }
    public void setFloIpAddress(java.lang.String floIpAddress) {
        this.floIpAddress = floIpAddress;
    }
    public LocalDateTime getFloCreated() {
        return floCreated;
    }
    public void setFloCreated(LocalDateTime floCreated) {
        this.floCreated = floCreated;
    }
    public java.lang.String getFloNotes() {
        return floNotes;
    }
    public void setFloNotes(java.lang.String floNotes) {
        this.floNotes = floNotes;
    }
}
