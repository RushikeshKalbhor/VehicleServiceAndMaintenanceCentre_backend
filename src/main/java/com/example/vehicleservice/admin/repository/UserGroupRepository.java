package com.example.vehicleservice.admin.repository;

import com.example.vehicleservice.admin.model.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Integer> {

    @Query("FROM UserGroup WHERE usgName = :usgName")
    List<UserGroup> findUserGroupByUsgName(String usgName);
}
