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

    @Column(name = "pay_jc_id", nullable = false)
    private Integer payJcId;

    @Column(name = "pay_amount", nullable = false)
    private Double payAmount;

    // success, failed
    @Column(name = "pay_status", nullable = false)
    private String payStatus;

    @Column(name = "pay_transaction_id", nullable = false, unique = true)
    private String payTransactionId;

    @Column(name = "pay_date", nullable = false)
    private LocalDateTime payDate;

    public Integer getPayId() {
        return payId;
    }

    public void setPayId(Integer payId) {
        this.payId = payId;
    }

    public Integer getPayJcId() {
        return payJcId;
    }

    public void setPayJcId(Integer payJcId) {
        this.payJcId = payJcId;
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
}
