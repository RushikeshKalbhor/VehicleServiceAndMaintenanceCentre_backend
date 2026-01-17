package com.example.vehicleservice.config;

import com.example.vehicleservice.admin.model.AuthenticationToken;
import com.example.vehicleservice.config.security.UserDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;

@Component
public class JwtUtil {

    @Value("${jwt.jwtExpirationInMs}")
    private long jwtExpirationInMs;

    @Value("${refreshExpirationDateInMs}")
    private long refreshExpirationDateInMs;

    private SecretKey key;

    @Value("${jwtSecret}")
    private String jwtSecret;

    @PostConstruct
    public void initKey() {
        if (jwtSecret == null || jwtSecret.isEmpty()) {
            throw new IllegalArgumentException("JWT secret cannot be null or empty");
        }
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public Map<String,String> generateTokens(UserDetail userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());

        java.util.Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
        Set<String> userRoles = AuthorityUtils.authorityListToSet(roles);
        String userRolesString = String.join(",", userRoles);
        claims.put("userRoles", userRolesString);

        Map<String,String> tokens = new HashMap<>();
        tokens.put("jwtToken",doGenerateToken(claims, userDetails.getUsername()));
        tokens.put("refreshToken",generateRefreshToken());
        return tokens;
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        long expiration = jwtExpirationInMs;
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken() {
        long expiration = refreshExpirationDateInMs;
        Map<String, Object> claims = new HashMap<>();
        String randomString = new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .filteredBy(LETTERS, DIGITS).get().generate(10);

        return Jwts.builder()
                .claims(claims)
                .subject(randomString)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    //check if the token has expired
    public boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieving any information from token we will need the secret key
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public List<GrantedAuthority> getRolesFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        String userRoles =  (String) claims.get("userRoles");
        if (userRoles == null || userRoles.isBlank()) {
            return List.of();
        }
        return AuthorityUtils.createAuthorityList(userRoles.split(","));
    }

    public boolean validateTokenAgainstAuthentication(UserDetail userDetail, AuthenticationToken authenticationToken) {
        try {

            //Parse stored token claims
            Claims claims = getAllClaimsFromToken(authenticationToken.getAtkJwtToken());

            // username check
            if (!userDetail.getUsername().equals(claims.getSubject())) {
                return false;
            }

            //Compare roles
            List<String> tokenRoles = getRolesFromToken(authenticationToken.getAtkJwtToken()).stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            List<String> userRoles = userDetail.getAuthorities() == null
                    ? List.of()
                    :userDetail.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            return new HashSet<>(userRoles).equals(new HashSet<>(tokenRoles));
        }
        catch (JwtException ex) {
            return false;
        }
    }

}