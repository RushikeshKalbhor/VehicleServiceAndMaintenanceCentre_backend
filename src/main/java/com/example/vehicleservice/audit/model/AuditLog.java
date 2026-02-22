package com.example.vehicleservice.audit.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "al_id", precision = 10, nullable = false)
    private Long alId;

    @Column(name = "al_username", nullable = false)
    private String alUsername;

    @Column(name = "al_role", nullable = false)
    private String alRole;              // ADMIN / CUSTOMER / MECHANIC

    @Column(name = "al_action", nullable = false)
    private String alAction;            // CREATE / UPDATE / DELETE

    @Column(name = "al_module", nullable = false)
    private String alModule;            // VEHICLE / APPOINTMENT / JOB_CARD

    @Column(name = "al_description")
    private String alDescription;       // extra info

    @Column(name = "al_ip_address")
    private String alIpAddress;

    @Column(name = "al_created", nullable = false)
    private LocalDateTime alCreated;

    @Column(name = "al_record_status", nullable = false)
    private String alRecordStatus;

    public Long getAlId() {
        return alId;
    }

    public void setAlId(Long alId) {
        this.alId = alId;
    }

    public String getAlUsername() {
        return alUsername;
    }

    public void setAlUsername(String alUsername) {
        this.alUsername = alUsername;
    }

    public String getAlRole() {
        return alRole;
    }

    public void setAlRole(String alRole) {
        this.alRole = alRole;
    }

    public String getAlAction() {
        return alAction;
    }

    public void setAlAction(String alAction) {
        this.alAction = alAction;
    }

    public String getAlModule() {
        return alModule;
    }

    public void setAlModule(String alModule) {
        this.alModule = alModule;
    }

    public String getAlDescription() {
        return alDescription;
    }

    public void setAlDescription(String alDescription) {
        this.alDescription = alDescription;
    }

    public String getAlIpAddress() {
        return alIpAddress;
    }

    public void setAlIpAddress(String alIpAddress) {
        this.alIpAddress = alIpAddress;
    }

    public LocalDateTime getAlCreated() {
        return alCreated;
    }

    public void setAlCreated(LocalDateTime alCreated) {
        this.alCreated = alCreated;
    }

    public String getAlRecordStatus() {
        return alRecordStatus;
    }

    public void setAlRecordStatus(String alRecordStatus) {
        this.alRecordStatus = alRecordStatus;
    }
}
