package com.example.vehicleservice.finance.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bills")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "b_id", precision = 8, nullable = false)
    private Integer bId;

    @Column(name = "b_apt_id", nullable = false)
    private Integer bAptId;

    @Column(name = "b_jc_id", nullable = false)
    private Integer bJcId;

    @Column(name = "b_total")
    private Double bTotal;

    @Column(name = "b_discount")
    private Integer bDiscount;

    @Column(name = "b_final_total")
    private Double bFinalTotal;

    @Column(name = "b_status")
    private String bStatus;

    @Column(name = "b_record_status", nullable = false)
    private String bRecordStatus;

    @Column(name = "b_created", nullable = false)
    private LocalDateTime bCreated;

    @Column(name = "b_created_by", nullable = false)
    private String bCreatedBy;

    @Column(name = "b_updated")
    private LocalDateTime bUpdated;

    @Column(name = "b_updated_by")
    private String bUpdatedBy;

    public Integer getBId() {
        return bId;
    }

    public void setBId(Integer bId) {
        this.bId = bId;
    }

    public Integer getBAptId() {
        return bAptId;
    }

    public void setBAptId(Integer bAptId) {
        this.bAptId = bAptId;
    }

    public Integer getBJcId() {
        return bJcId;
    }

    public void setBJcId(Integer bJcId) {
        this.bJcId = bJcId;
    }

    public Integer getBDiscount() {
        return bDiscount;
    }

    public void setBDiscount(Integer bDiscount) {
        this.bDiscount = bDiscount;
    }

    public Double getBTotal() {
        return bTotal;
    }

    public void setBTotal(Double bTotal) {
        this.bTotal = bTotal;
    }

    public Double getBFinalTotal() {
        return bFinalTotal;
    }

    public void setBFinalTotal(Double bFinalTotal) {
        this.bFinalTotal = bFinalTotal;
    }

    public String getBStatus() {
        return bStatus;
    }

    public void setBStatus(String bStatus) {
        this.bStatus = bStatus;
    }

    public String getBRecordStatus() {
        return bRecordStatus;
    }

    public void setBRecordStatus(String bRecordStatus) {
        this.bRecordStatus = bRecordStatus;
    }

    public LocalDateTime getBCreated() {
        return bCreated;
    }

    public void setBCreated(LocalDateTime bCreated) {
        this.bCreated = bCreated;
    }

    public String getBCreatedBy() {
        return bCreatedBy;
    }

    public void setBCreatedBy(String bCreatedBy) {
        this.bCreatedBy = bCreatedBy;
    }

    public LocalDateTime getBUpdated() {
        return bUpdated;
    }

    public void setBUpdated(LocalDateTime bUpdated) {
        this.bUpdated = bUpdated;
    }

    public String getBUpdatedBy() {
        return bUpdatedBy;
    }

    public void setBUpdatedBy(String bUpdatedBy) {
        this.bUpdatedBy = bUpdatedBy;
    }
}
