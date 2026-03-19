package com.example.vehicleservice.email.repository;

import com.example.vehicleservice.email.model.UserOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOtpRepository extends JpaRepository<UserOtp, Integer> {

    @Query("FROM UserOtp WHERE uoUseUsername = :uoUseUsername ORDER BY uoId DESC LIMIT 1")
    UserOtp findUserOtpByUoUseUsername(String uoUseUsername);
}
