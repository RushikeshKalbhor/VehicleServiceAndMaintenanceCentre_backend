package com.example.vehicleservice.config;

import com.example.vehicleservice.admin.model.AuthenticationToken;
import com.example.vehicleservice.admin.repository.AuthenticationTokenRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class AuthenticationTokensCache {

    private final AuthenticationTokenRepository authenticationTokenRepository;

    public AuthenticationTokensCache(AuthenticationTokenRepository authenticationTokenRepository) {
        this.authenticationTokenRepository = authenticationTokenRepository;
    }

    @Cacheable(cacheNames="tokens", key="#username")
    public AuthenticationToken getAuthenticationToken(String username) {
        List<AuthenticationToken> userAuthenticationTokens = authenticationTokenRepository.findAuthenticationTokenByAtkUseUsername(username);
        if(!userAuthenticationTokens.isEmpty()) {

            return userAuthenticationTokens.getFirst();
        }
        return null;
    }

    @CachePut(value="tokens", key="#username")
    public void updateAuthenticationToken(String username, String jwtToken, String refreshToken, String uuid){
        AuthenticationToken authenticationToken;
        List<AuthenticationToken> userAuthenticationTokens = authenticationTokenRepository.findAuthenticationTokenByAtkUseUsername(username);
        if(!userAuthenticationTokens.isEmpty()) {
            authenticationToken = userAuthenticationTokens.getFirst();
        }
        else {
            authenticationToken = new AuthenticationToken();
        }
        authenticationToken.setAtkUseUsername(username);
        authenticationToken.setAtkJwtToken(jwtToken);
        authenticationToken.setAtkRefreshToken(refreshToken);
        authenticationToken.setAtkCreated(LocalDateTime.now());
        authenticationToken.setAtkUuid(uuid);
        authenticationTokenRepository.save(authenticationToken);
    }
}
