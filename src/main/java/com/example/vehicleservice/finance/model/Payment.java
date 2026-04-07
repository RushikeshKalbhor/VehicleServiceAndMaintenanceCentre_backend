package com.example.vehicleservice.finance.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_id")
    private Integer payId;

    @Column(name = "pay_apt_id")
    private Integer payAptId;

    @Column(name = "pay_amount", nullable = false)
    private Double payAmount;

    // success, failed
    @Column(name = "pay_status", nullable = false)
    private String payStatus;

    @Column(name = "pay_transaction_id")
    private String payTransactionId;

    @Column(name = "pay_date", nullable = false)
    private LocalDateTime payDate;

    @Column(name = "pay_record_status", nullable = false)
    private String payRecordStatus;

    @Column(name = "pay_created", nullable = false)
    private LocalDateTime payCreated;

    @Column(name = "pay_created_by", nullable = false)
    private String payCreatedBy;

    public Integer getPayId() {
        return payId;
    }

    public void setPayId(Integer payId) {
        this.payId = payId;
    }

    public Integer getPayAptId() {
        return payAptId;
    }

    public void setPayAptId(Integer payAptId) {
        this.payAptId = payAptId;
    }

    public Double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getPayTransactionId() {
        return payTransactionId;
    }

    public void setPayTransactionId(String payTransactionId) {
        this.payTransactionId = payTransactionId;
    }

    public LocalDateTime getPayDate() {
        return payDate;
    }

    public void setPayDate(LocalDateTime payDate) {
        this.payDate = payDate;
    }

    public String getPayRecordStatus() {
        return payRecordStatus;
    }

    public void setPayRecordStatus(String payRecordStatus) {
        this.payRecordStatus = payRecordStatus;
    }

    public LocalDateTime getPayCreated() {
        return payCreated;
    }

    public void setPayCreated(LocalDateTime payCreated) {
        this.payCreated = payCreated;
    }

    public String getPayCreatedBy() {
        return payCreatedBy;
    }

    public void setPayCreatedBy(String payCreatedBy) {
        this.payCreatedBy = payCreatedBy;
    }
}
