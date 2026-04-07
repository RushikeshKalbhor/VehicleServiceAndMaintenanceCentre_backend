package com.example.vehicleservice.finance.repository;

import com.example.vehicleservice.finance.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    @Query("SELECT SUM(payAmount) FROM Payment WHERE payAptId = :payAptId AND payStatus = 'paid' AND payRecordStatus = 'approved'")
    Double findSumPayAmountByPayAptId(Integer payAptId);
}
