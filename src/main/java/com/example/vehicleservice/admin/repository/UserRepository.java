package com.example.vehicleservice.admin.repository;

import com.example.vehicleservice.admin.model.User;
import com.example.vehicleservice.config.records.UserLoginDetailsRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUseUsername(String useUsername);

    @Query("SELECT new com.example.vehicleservice.config.records.UserLoginDetailsRecord(useUsername, usePassword) FROM User WHERE useUsername = :useUsername")
    Optional<UserLoginDetailsRecord> findUserLoginDetailsRecordFromUserByUseUsername(String useUsername);

    @Query(value = "SELECT e.usePasswordLastModified FROM User e WHERE e.useUsername=:useUsername")
    LocalDate findUsePasswordLastModifiedFromUserByUseUsername(String useUsername);

    @Modifying
    @Query("UPDATE User SET useLastLoggedinDate = CURDATE(), useLoggedIn = :useLoggedIn WHERE useUsername = :useUsername")
    Integer updateUseLastLoggedinDateAndUseLoggedInByUseUsername(String useUsername, Byte useLoggedIn);

    @Modifying
    @Query("update User set useLoginAttempts = useLoginAttempts + 1 where useUsername = :useUsername")
    void updateUseLoginAttempts(@Param(value = "useUsername") String useUsername);
}
