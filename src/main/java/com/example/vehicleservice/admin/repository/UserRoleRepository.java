package com.example.vehicleservice.admin.repository;

import com.example.vehicleservice.admin.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

    @Query("SELECT usrName FROM UserRole WHERE usrUseUsername=:usrUseUsername")
    List<String> findUsrNameByUsrUseUsername(String usrUseUsername);
}
