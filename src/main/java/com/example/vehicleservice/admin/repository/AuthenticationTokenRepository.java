package com.example.vehicleservice.admin.repository;

import com.example.vehicleservice.admin.model.AuthenticationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthenticationTokenRepository extends JpaRepository<AuthenticationToken, Integer> {

    @Query("FROM AuthenticationToken e WHERE e.atkUseUsername = :atkUseUsername ORDER BY e.atkId DESC")
    List<AuthenticationToken> findAuthenticationTokenByAtkUseUsername(String atkUseUsername);

    /**
     * Returns the UUID of any authentication token belonging to the given username.
     * <p>
     * This query uses a native SQL statement with {@code LIMIT 1},
     * so it returns an arbitrary matching record (no guaranteed order).
     * If you want a specific record, add an {@code ORDER BY} clause
     * (e.g. {@code ORDER BY atk_created DESC}).
     *
     * @param atkUseUsername the username whose token UUID should be retrieved
     * @return the UUID of one matching authentication token, or {@code null} if none exists
     */
    @Query(value = "SELECT atk_uuid FROM authentication_tokens WHERE atk_use_username = :atkUseUsername LIMIT 1",nativeQuery = true)
    String findUuidByUsername(@Param("atkUseUsername") String atkUseUsername);
}
