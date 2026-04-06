package com.example.vehicleservice.finance.repository;

import com.example.vehicleservice.finance.model.BillItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillItemRepository extends JpaRepository<BillItem, Integer> {
    //
}
