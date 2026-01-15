package com.example.vehicleservice.admin.repository;

import com.example.vehicleservice.admin.model.BlockingIpAddressDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockingIpAddressDetailRepository extends JpaRepository<BlockingIpAddressDetail, Integer> {

    @Query("select count(bpaId) from BlockingIpAddressDetail where bpaIpAddress =:bpaIpAddress and bpaBlockedTillTime >= NOW() order by bpaBlockedTillTime desc")
    Integer findBlockingIpAddressDetailsByIpAddressType(String bpaIpAddress);
}
