package com.example.vehicleservice.config;

import com.example.vehicleservice.admin.model.AuthenticationToken;
import com.example.vehicleservice.admin.repository.AuthenticationTokenRepository;
import com.example.vehicleservice.config.security.UserDetail;
import com.example.vehicleservice.config.service.AuthService;
import com.example.vehicleservice.general.json.ResponseJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {


    private final AuthenticationTokensCache authenticationTokensCache;
    private final JwtUtil jwtUtil;
    private final AuthService authService;
    private final AuthenticationTokenRepository authenticationTokenRepository;

    public JwtRequestFilter(AuthenticationTokensCache authenticationTokensCache, JwtUtil jwtUtil, AuthService authService,
                            AuthenticationTokenRepository authenticationTokenRepository) {
        this.authenticationTokensCache = authenticationTokensCache;
        this.jwtUtil = jwtUtil;
        this.authService = authService;
        this.authenticationTokenRepository = authenticationTokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = null;
        try {

            final String authorizationHeader = request.getHeader("Authorization");

            String username = null;
            UserDetail userDetails = null;
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
                jwt = authorizationHeader.substring(7);

                username = jwtUtil.getUsernameFromToken(jwt);
            }

            // Once we get the token validate it.
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                AuthenticationToken authenticationToken = authenticationTokensCache.getAuthenticationToken(username);

                if(authenticationToken != null && (authenticationToken.getAtkJwtToken() == null || (!jwt.equals(authenticationToken.getAtkJwtToken())))) {
                    List<AuthenticationToken> userAuthenticationTokens = authenticationTokenRepository.findAuthenticationTokenByAtkUseUsername(username);
                    if(!userAuthenticationTokens.isEmpty()) {
                        authenticationToken = userAuthenticationTokens.getFirst();
                        authenticationTokensCache.updateAuthenticationToken(username, authenticationToken.getAtkJwtToken(), authenticationToken.getAtkRefreshToken(), authenticationToken.getAtkUuid());
                    }
                }

                // if token is valid configure Spring Security to manually set authentication
                if(authenticationToken != null && authenticationToken.getAtkJwtToken().equals(jwt)) {
                    userDetails = new UserDetail(jwtUtil.getUsernameFromToken(jwt), "",jwtUtil.getRolesFromToken(jwt));

                    if (!jwtUtil.validateTokenAgainstAuthentication(userDetails, authenticationToken)) {
                        sendUnauthorized(response, "unauthorized.mismatch.token.claims");
                        return;
                    }

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // After setting the Authentication in the context, we specify
                    // that the current user is authenticated. So it passes the
                    // Spring Security Configurations successfully.
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }

            }
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {
            postJwtExpirationProcessing(request, response, ex, filterChain);
        } catch (Exception ex) {
            logger.error("An error occurred", ex);
            sendUnauthorized(response, "unauthorized.invalid.or.expired.token");
        }
    }

    private void postJwtExpirationProcessing(HttpServletRequest request,HttpServletResponse response, ExpiredJwtException ex,
                                             FilterChain filterChain) throws IOException, ServletException {
        String requestURL = request.getRequestURL().toString();
        String refreshToken = request.getHeader("refreshToken");
        boolean isRefreshTokenExpired = true;
        String username = (String) ex.getClaims().get("sub");

        if(refreshToken != null && !refreshToken.isEmpty()) {

            AuthenticationToken authenticationToken = authenticationTokensCache.getAuthenticationToken(username);
            if(authenticationToken != null && authenticationToken.getAtkRefreshToken() != null && authenticationToken.getAtkRefreshToken().equals(refreshToken)) {
                try {
                    isRefreshTokenExpired = jwtUtil.isTokenExpired(refreshToken);
                }catch (ExpiredJwtException exception) {
                    setExpiredTokenResponse(response, "refreshtoken.expired");
                    authService.logout(username);
                }
            }
        }

        if (requestURL.contains("refresh-token") && !isRefreshTokenExpired) {
            allowForRefreshToken(ex, request);
            filterChain.doFilter(request, response);
        }
        else {
            setExpiredTokenResponse(response, "jwt.expired");
            authService.logout(username);
        }
    }

    private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {

        Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(ex.getClaims());
        String userRoles =  (String) ex.getClaims().get("userRoles");
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userRoles.split(","));

        UserDetail userDetails = new UserDetail(expectedMap.get("sub").toString(), "",
                authorities);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    public Map<String, Object> getMapFromIoJsonwebtokenClaims(Claims claims) {
        Map<String, Object> expectedMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            expectedMap.put(entry.getKey(), entry.getValue());
        }
        return expectedMap;
    }

    public void setExpiredTokenResponse(HttpServletResponse httpServletResponse, String validationCode) throws IOException {
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ResponseJson errorResponse = new ResponseJson();
        ObjectMapper objectMapper = new ObjectMapper();
        errorResponse.setValidationCode(validationCode);
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    private void sendUnauthorized(HttpServletResponse response, String validationCode) throws IOException {
        ResponseJson responseJson = new ResponseJson();
        ObjectMapper objectMapper = new ObjectMapper();
        responseJson.setValidationCode(validationCode);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(responseJson));
    }
}
