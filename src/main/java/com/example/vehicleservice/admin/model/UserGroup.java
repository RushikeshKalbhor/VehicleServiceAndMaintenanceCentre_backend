package com.example.vehicleservice.admin.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_groups")
public class UserGroup {

    @Column(name = "usg_id", precision = 8, nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer usgId;

    @Column(name = "usg_name")
    private String usgName;

    @Column(name = "usg_description")
    private String usgDescription;

    @Column(name = "usg_type")
    private String usgType;

    @Column(name = "usg_created")
    private LocalDateTime usgCreated;

    public Integer getUsgId() {
        return usgId;
    }

    public void setUsgId(Integer usgId) {
        this.usgId = usgId;
    }

    public String getUsgName() {
        return usgName;
    }

    public void setUsgName(String usgName) {
        this.usgName = usgName;
    }

    public String getUsgDescription() {
        return usgDescription;
    }

    public void setUsgDescription(String usgDescription) {
        this.usgDescription = usgDescription;
    }

    public String getUsgType() {
        return usgType;
    }

    public void setUsgType(String usgType) {
        this.usgType = usgType;
    }

    public LocalDateTime getUsgCreated() {
        return usgCreated;
    }

    public void setUsgCreated(LocalDateTime usgCreated) {
        this.usgCreated = usgCreated;
    }
}
