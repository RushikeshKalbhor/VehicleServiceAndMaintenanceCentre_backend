package com.example.vehicleservice.config.service;

import com.example.vehicleservice.admin.model.AuthenticationToken;
import com.example.vehicleservice.admin.model.BlockingIpAddressDetail;
import com.example.vehicleservice.admin.model.FailedLogin;
import com.example.vehicleservice.admin.model.User;
import com.example.vehicleservice.admin.repository.*;
import com.example.vehicleservice.config.JwtUtil;
import com.example.vehicleservice.config.UserDetailsServiceImpl;
import com.example.vehicleservice.config.json.LoginJson;
import com.example.vehicleservice.config.json.UserRegisterJson;
import com.example.vehicleservice.config.security.UserDetail;
import com.example.vehicleservice.general.DisplayRecordStatus;
import com.example.vehicleservice.general.json.ResponseJson;
import com.example.vehicleservice.general.util.ValidationUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final AuthenticationManager authenticationManager;
    private final HttpServletRequest request;
    private final VehiclePreferenceRepository vehiclePreferenceRepository;
    private final ValidationUtil validationUtil;
    private final BlockingIpAddressDetailRepository blockingIpAddressDetailRepository;
    private final FailedLoginRepository failedLoginRepository;
    private final AuthenticationTokenRepository authenticationTokenRepository;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil,
                       UserDetailsServiceImpl userDetailsServiceImpl, AuthenticationManager authenticationManager,
                       HttpServletRequest request, VehiclePreferenceRepository vehiclePreferenceRepository,
                       ValidationUtil validationUtil, BlockingIpAddressDetailRepository blockingIpAddressDetailRepository,
                       FailedLoginRepository failedLoginRepository, AuthenticationTokenRepository authenticationTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.authenticationManager = authenticationManager;
        this.request = request;
        this.vehiclePreferenceRepository = vehiclePreferenceRepository;
        this.validationUtil = validationUtil;
        this.blockingIpAddressDetailRepository = blockingIpAddressDetailRepository;
        this.failedLoginRepository = failedLoginRepository;
        this.authenticationTokenRepository = authenticationTokenRepository;
    }

    public ResponseJson register(UserRegisterJson userRegisterJson) {
        User user = new User();
        user.setUseUsername(userRegisterJson.getUseUsername());
        user.setUseFirstName(userRegisterJson.getUseFirstName());
        user.setUseSurname(userRegisterJson.getUseSurname());
        user.setUseFullName(userRegisterJson.getUseFirstName() + " " + userRegisterJson.getUseSurname());
        user.setUseEmail(userRegisterJson.getUseEmail());
        user.setUsePassword(passwordEncoder.encode(userRegisterJson.getUsePassword()));
        user.setUsePasswordLastModified(LocalDate.now());
        userRepository.save(user);
        return new ResponseJson("user.registered.success");
    }

    @Transactional
    public ResponseJson login(LoginJson loginJson) {
        ResponseJson responseJson = new ResponseJson();
        User user = userRepository.findByUseUsername(loginJson.getUsername());
        if (user == null) {
            responseJson.setValidationCode("username.or.password.incorrect");
            return responseJson;
        }
        String remoteAddress = request.getRemoteAddr();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginJson.getUsername(), loginJson.getPassword()));
        } catch(BadCredentialsException e) {
            responseJson = validateLoginIncorrectCredentials(loginJson.getUsername(), remoteAddress, responseJson);
            throw new BadCredentialsException(responseJson.toString(), e);
        }
        catch(InternalAuthenticationServiceException e){
            responseJson.setValidationCode("login.fail");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseJson).getBody();
        }
        responseJson = validateLoginCorrectCredentials(loginJson.getUsername(), remoteAddress, responseJson);

        if(responseJson.getValidationCode() == null) {
            responseJson = checkPasswordActiveDuration(loginJson.getUsername(), responseJson);
            if(responseJson.getValidationCode() != null) {
                throw new BadCredentialsException(responseJson.toString());
            }
            UserDetail userDetail = userDetailsServiceImpl.loadUserByUsername(loginJson.getUsername());
            Map<String,String> tokens = jwtUtil.generateTokens(userDetail);
            String jwt = tokens.get("jwtToken");
            String refreshToken = tokens.get("refreshToken");
            String uuid = UUID.randomUUID().toString();

            // token added in authentication table
            updateAuthenticationToken(loginJson.getUsername(), jwt, refreshToken, uuid);

            // mark 1 for user is active
            updateUserLoginDetails(loginJson.getUsername(), Byte.valueOf("1"));
            // set response
            buildAuthenticationResponse(jwt, refreshToken, responseJson);
            return ResponseEntity.status(HttpStatus.OK).body(responseJson).getBody();
        }
        throw new BadCredentialsException(responseJson.toString());
    }

    public ResponseJson validateLoginCorrectCredentials(String username, String remoteAddress, ResponseJson responseJson) {


        return new ResponseJson();
    }

    public void buildAuthenticationResponse(String jwt, String refreshToken, ResponseJson responseJson) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("Jwt", jwt);
        responseMap.put("RefreshToken", refreshToken);

        responseJson.setEntity(responseMap);
        responseJson.setValidationCode("user.login.success");
    }

    @Transactional
    public void updateUserLoginDetails(String username, Byte login) {
        userRepository.updateUseLastLoggedinDateAndUseLoggedInByUseUsername(username, login);
    }

    public ResponseJson checkPasswordActiveDuration(String username, ResponseJson responseJson) {
        String passwordActiveDuration = vehiclePreferenceRepository.findVehiclePreferenceCecValueByCecName("password_active_duration");

        if (passwordActiveDuration != null && Integer.parseInt(passwordActiveDuration) > 0) {
            LocalDate datePasswordLastModified =  userRepository.findUsePasswordLastModifiedFromUserByUseUsername(username);
            int daysSincePasswordModified = (int) ChronoUnit.DAYS.between(datePasswordLastModified, LocalDate.now());

            if(daysSincePasswordModified >= Integer.parseInt(passwordActiveDuration)) {
                responseJson.setValidationCode("area.login.reminder.passwordexpired");
            }
        }
        return responseJson;
    }

    public ResponseJson validateLoginIncorrectCredentials(String username, String ipAddress, ResponseJson responseJson) {
        responseJson = checkIpAddressBlocking(ipAddress, username, responseJson);
        if(responseJson.getValidationCode() != null) {
            throw new BadCredentialsException(responseJson.toString());
        }
        responseJson = validateMaximumLoginAttemptsIncorrectCredentials(username, responseJson);
        return responseJson;
    }

    @Transactional
    public ResponseJson validateMaximumLoginAttemptsIncorrectCredentials(String username, ResponseJson responseJson) {
        userRepository.updateUseLoginAttempts(username);
        responseJson.setValidationCode("username.or.password.incorrect");
        return responseJson;
    }


    public ResponseJson checkIpAddressBlocking(String ipAddress, String username, ResponseJson responseJson){

        String turnOffIpBlocking = vehiclePreferenceRepository.findVehiclePreferenceCecValueByCecName("turn_off_ip_blocking");

        if (validationUtil.isNullOrEmpty(turnOffIpBlocking) || "0".equals(turnOffIpBlocking)) {

            String loginHoursBlocked = vehiclePreferenceRepository.findVehiclePreferenceCecValueByCecName("login_amount_of_hours_blocked");
            int blockedHours = loginHoursBlocked != null && !loginHoursBlocked.isEmpty() ? Integer.parseInt(loginHoursBlocked) : 24;

            if (getBlockedIpAddressInformation(blockedHours, ipAddress, username, false)) {
                responseJson.setValidationCode("area.users.ipblocked");
            }
        }
        return responseJson;
    }

    public boolean getBlockedIpAddressInformation(int blockedHours, String ipAddress, String username, boolean blockedIpAddressMessage) {

        Integer foundBlockedIpAddress = blockingIpAddressDetailRepository.findBlockingIpAddressDetailsByIpAddressType(ipAddress);

        if(foundBlockedIpAddress != null && foundBlockedIpAddress == 0) {

            LocalDateTime endTime = LocalDateTime.now();
            LocalDateTime blockedTillDateTime = endTime.plusHours(blockedHours);
            addFailedLogin(ipAddress,  username);
            Integer foundFailedLogins = failedLoginRepository.findFailedLoginByFloIpAddressNotes(ipAddress, username, endTime.minusMinutes(10), endTime);

            if(foundFailedLogins != null && foundFailedLogins >= 10) {
                addBlockingIpAddressDetail(ipAddress, blockedTillDateTime);
                blockedIpAddressMessage = true;
            }

        } else if(foundBlockedIpAddress != null && foundBlockedIpAddress > 0 ) {
            blockedIpAddressMessage = true;
        }
        return blockedIpAddressMessage;

    }

    void addFailedLogin(String useIP, String notes) {
        FailedLogin flb = new FailedLogin();

        flb.setFloIpAddress(useIP);
        flb.setFloType(DisplayRecordStatus.USER.getStatus());
        flb.setFloCreated(LocalDateTime.now());
        flb.setFloNotes(notes);
        failedLoginRepository.save(flb);
    }

    void addBlockingIpAddressDetail(String useIP, LocalDateTime blockedTillDateTime) {
        BlockingIpAddressDetail blockingIpAddressDetail = new BlockingIpAddressDetail();
        blockingIpAddressDetail.setBpaIpAddress(useIP);
        blockingIpAddressDetail.setBpaType(DisplayRecordStatus.USER.getStatus());
        blockingIpAddressDetail.setBpaBlockedTillTime(blockedTillDateTime);
        blockingIpAddressDetail.setBpaCreated(LocalDateTime.now());
        blockingIpAddressDetailRepository.save(blockingIpAddressDetail);
    }

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
