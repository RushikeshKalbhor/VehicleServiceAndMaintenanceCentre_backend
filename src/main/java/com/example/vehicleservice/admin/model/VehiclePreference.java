package com.example.vehicleservice.admin.model;

import jakarta.persistence.*;

@Entity
@Table(name = "vehicle_preferences")
public class VehiclePreference {

    @Column(name = "vep_id", precision = 8)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer vepId;

    @Column(name = "vep_name", length=60)
    private String vepName;

    @Column(name = "vep_value", length=255)
    private String vepValue;

    public Integer getVepId() {
        return vepId;
    }

    public void setVepId(Integer vepId) {
        this.vepId = vepId;
    }

    public String getVepName() {
        return vepName;
    }

    public void setVepName(String vepName) {
        this.vepName = vepName;
    }

    public String getVepValue() {
        return vepValue;
    }

    public void setVepValue(String vepValue) {
        this.vepValue = vepValue;
    }
}
