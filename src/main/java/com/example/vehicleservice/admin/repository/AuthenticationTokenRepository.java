package com.example.vehicleservice.admin.repository;

import com.example.vehicleservice.admin.model.AuthenticationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthenticationTokenRepository extends JpaRepository<AuthenticationToken, Integer> {

    @Query("FROM AuthenticationToken e WHERE e.atkUseUsername = :atkUseUsername ORDER BY e.atkId DESC")
    List<AuthenticationToken> findAuthenticationTokenByAtkUseUsername(String atkUseUsername);
}
