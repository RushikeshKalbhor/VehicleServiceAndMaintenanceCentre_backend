package com.example.vehicleservice.finance.repository;

import com.example.vehicleservice.finance.model.BillItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillItemRepository extends JpaRepository<BillItem, Integer> {

    @Query("FROM BillItem WHERE biBId = :biBId AND biRecordStatus = 'approved'")
    List<BillItem> findBillItemByBiBId(Integer biBId);
}
