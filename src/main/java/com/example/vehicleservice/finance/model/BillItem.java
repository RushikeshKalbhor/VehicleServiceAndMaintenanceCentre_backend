package com.example.vehicleservice.finance.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bill_items")
public class BillItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bi_id", precision = 8, nullable = false)
    private Integer biId;

    @Column(name = "bi_b_id", nullable = false)
    private Integer biBId;

    @Column(name = "bi_service_name", nullable = false)
    private String biServiceName;

    @Column(name = "bi_quantity", nullable = false)
    private Integer biQuantity;

    @Column(name = "bi_rate", nullable = false)
    private Double biRate;

    @Column(name = "bi_total", nullable = false)
    private Double biTotal;

    @Column(name = "bi_record_status", nullable = false)
    private String biRecordStatus;

    @Column(name = "bi_created", nullable = false)
    private LocalDateTime biCreated;

    @Column(name = "bi_created_by", nullable = false)
    private String biCreatedBy;

    public Integer getBiId() {
        return biId;
    }

    public void setBiId(Integer biId) {
        this.biId = biId;
    }

    public Integer getBiBId() {
        return biBId;
    }

    public void setBiBId(Integer biBId) {
        this.biBId = biBId;
    }

    public String getBiServiceName() {
        return biServiceName;
    }

    public void setBiServiceName(String biServiceName) {
        this.biServiceName = biServiceName;
    }

    public Integer getBiQuantity() {
        return biQuantity;
    }

    public void setBiQuantity(Integer biQuantity) {
        this.biQuantity = biQuantity;
    }

    public Double getBiRate() {
        return biRate;
    }

    public void setBiRate(Double biRate) {
        this.biRate = biRate;
    }

    public Double getBiTotal() {
        return biTotal;
    }

    public void setBiTotal(Double biTotal) {
        this.biTotal = biTotal;
    }

    public String getBiRecordStatus() {
        return biRecordStatus;
    }

    public void setBiRecordStatus(String biRecordStatus) {
        this.biRecordStatus = biRecordStatus;
    }

    public LocalDateTime getBiCreated() {
        return biCreated;
    }

    public void setBiCreated(LocalDateTime biCreated) {
        this.biCreated = biCreated;
    }

    public String getBiCreatedBy() {
        return biCreatedBy;
    }

    public void setBiCreatedBy(String biCreatedBy) {
        this.biCreatedBy = biCreatedBy;
    }
}
