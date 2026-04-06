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

    public Integer getbId() {
        return bId;
    }

    public void setbId(Integer bId) {
        this.bId = bId;
    }

    public Integer getbAptId() {
        return bAptId;
    }

    public void setbAptId(Integer bAptId) {
        this.bAptId = bAptId;
    }

    public Integer getbJcId() {
        return bJcId;
    }

    public void setbJcId(Integer bJcId) {
        this.bJcId = bJcId;
    }

    public Integer getbDiscount() {
        return bDiscount;
    }

    public void setbDiscount(Integer bDiscount) {
        this.bDiscount = bDiscount;
    }

    public Double getbTotal() {
        return bTotal;
    }

    public void setbTotal(Double bTotal) {
        this.bTotal = bTotal;
    }

    public Double getbFinalTotal() {
        return bFinalTotal;
    }

    public void setbFinalTotal(Double bFinalTotal) {
        this.bFinalTotal = bFinalTotal;
    }

    public String getbStatus() {
        return bStatus;
    }

    public void setbStatus(String bStatus) {
        this.bStatus = bStatus;
    }

    public String getbRecordStatus() {
        return bRecordStatus;
    }

    public void setbRecordStatus(String bRecordStatus) {
        this.bRecordStatus = bRecordStatus;
    }

    public LocalDateTime getbCreated() {
        return bCreated;
    }

    public void setbCreated(LocalDateTime bCreated) {
        this.bCreated = bCreated;
    }

    public String getbCreatedBy() {
        return bCreatedBy;
    }

    public void setbCreatedBy(String bCreatedBy) {
        this.bCreatedBy = bCreatedBy;
    }

    public LocalDateTime getbUpdated() {
        return bUpdated;
    }

    public void setbUpdated(LocalDateTime bUpdated) {
        this.bUpdated = bUpdated;
    }

    public String getbUpdatedBy() {
        return bUpdatedBy;
    }

    public void setbUpdatedBy(String bUpdatedBy) {
        this.bUpdatedBy = bUpdatedBy;
    }
}
