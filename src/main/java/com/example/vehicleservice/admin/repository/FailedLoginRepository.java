package com.example.vehicleservice.admin.repository;

import com.example.vehicleservice.admin.model.FailedLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface FailedLoginRepository extends JpaRepository<FailedLogin, Integer> {

    @Query(value = "select count(floId) from FailedLogin e where e.floIpAddress =:floIpAddress and e.floNotes=:floNotes and e.floCreated >= :startTime and e.floCreated <= :endTime")
    Integer findFailedLoginByFloIpAddressNotes(java.lang.String floIpAddress, java.lang.String floNotes, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

}
