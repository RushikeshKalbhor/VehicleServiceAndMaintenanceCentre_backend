package com.example.vehicleservice.email.repository;

import com.example.vehicleservice.email.model.UserOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOtpRepository extends JpaRepository<UserOtp, Integer> {
    //
}
