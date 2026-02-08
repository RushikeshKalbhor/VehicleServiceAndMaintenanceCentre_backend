package com.example.vehicleservice.admin.repository;

import com.example.vehicleservice.admin.model.User;
import com.example.vehicleservice.config.records.UserLoginDetailsRecord;
import com.example.vehicleservice.config.records.UserRecord;
import com.example.vehicleservice.mechanic.records.MechanicRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
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
    @Query("update User set useLoginAttempts = :useLoginAttempts where useUsername = :useUsername")
    void updateUseLoginAttempts(String useUsername, Integer useLoginAttempts);

    @Query("SELECT new com.example.vehicleservice.config.records.UserRecord(useUsername, useActive, usePasswordLastModified) FROM User WHERE useUsername = :useUsername")
    UserRecord findUseActiveByUseUsername(String useUsername);

    @Query("SELECT useLoginAttempts FROM User WHERE useUsername = :useUsername")
    Integer findUseLoginAttemptsByUseUsername(String useUsername);

    @Query("SELECT COUNT(useUsername) > 0 FROM User WHERE useUsername = :useUsername")
    boolean existsByUseUsername(String useUsername);

    @Query("""
    SELECT new com.example.vehicleservice.mechanic.records.MechanicRecord(useUsername, useTitle, useFirstName, useSurname) FROM User
    WHERE useType = :useType AND useActive = :useActive
    """)
    List<MechanicRecord> findMechanicRecordUseType(String useType, Byte useActive);

    @Query("SELECT useType FROM User WHERE useUsername = :useUsername")
    String findUserTypeByUseUsername(String useUsername);

    @Query("""
    SELECT new com.example.vehicleservice.mechanic.records.MechanicRecord(useUsername, useTitle, useFirstName, useSurname) FROM User
    WHERE useUsername IN (:useUsername)
    """)
    List<MechanicRecord> findUserNameRecordByUsernameList(List<String> useUsername);
}
