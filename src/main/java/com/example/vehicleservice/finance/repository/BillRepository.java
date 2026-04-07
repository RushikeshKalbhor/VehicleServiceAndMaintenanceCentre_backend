package com.example.vehicleservice.finance.repository;

import com.example.vehicleservice.finance.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {

    @Query("FROM Bill WHERE bId = :bId AND bRecordStatus = 'approved'")
    Bill findBillByBId(Integer bId);

    @Query("SELECT bFinalTotal FROM Bill WHERE bAptId = :bAptId AND bRecordStatus = 'approved'")
    Double findBFinalTotalByBAptId(Integer bAptId);
}
