package com.example.vehicleservice.config.service;

import com.example.vehicleservice.admin.model.*;
import com.example.vehicleservice.admin.repository.*;
import com.example.vehicleservice.config.AuthenticationTokensCache;
import com.example.vehicleservice.config.JwtUtil;
import com.example.vehicleservice.config.UserDetailsServiceImpl;
import com.example.vehicleservice.config.json.LoginJson;
import com.example.vehicleservice.config.json.UserRegisterJson;
import com.example.vehicleservice.config.records.UserRecord;
import com.example.vehicleservice.config.security.UserDetail;
import com.example.vehicleservice.config.util.AuthUtil;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
    private final AuthenticationTokensCache authenticationTokensCache;
    private final UserRoleRepository userRoleRepository;
    private final AuthUtil authUtil;
    private final AuthenticationTokenRepository authenticationTokenRepository;
    private final UserGroupRepository userGroupRepository;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil,
                       UserDetailsServiceImpl userDetailsServiceImpl, AuthenticationManager authenticationManager,
                       HttpServletRequest request, VehiclePreferenceRepository vehiclePreferenceRepository,
                       ValidationUtil validationUtil, BlockingIpAddressDetailRepository blockingIpAddressDetailRepository,
                       FailedLoginRepository failedLoginRepository, AuthenticationTokensCache authenticationTokensCache,
                       UserRoleRepository userRoleRepository, AuthUtil authUtil, AuthenticationTokenRepository authenticationTokenRepository,
                       UserGroupRepository userGroupRepository) {
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
        this.authenticationTokensCache = authenticationTokensCache;
        this.userRoleRepository = userRoleRepository;
        this.authUtil = authUtil;
        this.authenticationTokenRepository = authenticationTokenRepository;
        this.userGroupRepository = userGroupRepository;
    }

    public ResponseJson register(UserRegisterJson userRegisterJson) {
        User user = new User();
        user.setUseUsername(userRegisterJson.getUseUsername());
        user.setUseTitle(userRegisterJson.getUseTitle());
        user.setUseFirstName(userRegisterJson.getUseFirstName());
        user.setUseSurname(userRegisterJson.getUseSurname());
        user.setUseFullName(userRegisterJson.getUseFirstName() + " " + userRegisterJson.getUseSurname());
        user.setUseActive(userRegisterJson.getUseActive().byteValue());
        user.setUseEmail(userRegisterJson.getUseEmail());
        user.setUseMobile(userRegisterJson.getUseMobile());
        user.setUsePassword(passwordEncoder.encode(userRegisterJson.getUsePassword()));
        List<UserRole> userRoleList = new ArrayList<>();
        if (!validationUtil.isNullOrEmpty(userRegisterJson.getUseType())) {
            user.setUseType(userRegisterJson.getUseType());
            List<UserGroup> userGroupList = userGroupRepository.findUserGroupByUsgName(userRegisterJson.getUseType());
            for (UserGroup userGroup : userGroupList) {
                addUserRole(userRegisterJson.getUseUsername(), userGroup, userRoleList);
            }

        } else {
            user.setUseType("customer");
        }
        user.setUsePasswordLastModified(LocalDate.now());
        user.setUseCreated(LocalDateTime.now());
        user.setUseCreatedBy(userRegisterJson.getUseUsername());
        userRepository.save(user);
        if (!userRoleList.isEmpty()) {
            userRoleRepository.saveAll(userRoleList);
        }
        return new ResponseJson("user.registered.success");
    }

    private static void addUserRole(String usrUseUsername, UserGroup userGroup, List<UserRole> userRoleList) {
        UserRole userRole = new UserRole();
        userRole.setUsrUseUsername(usrUseUsername);
        userRole.setUsrUsgId(userGroup.getUsgId());
        userRole.setUsrName(userGroup.getUsgName());
        userRoleList.add(userRole);
    }

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
        responseJson = validateLoginCorrectCredentials(loginJson.getUsername(), responseJson);

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
            authenticationTokensCache.updateAuthenticationToken(loginJson.getUsername(), jwt, refreshToken, uuid);

            // mark 1 for user is active
            authUtil.updateUserMarkAsActive(loginJson.getUsername(), Byte.valueOf("1"));

            // set response
            buildAuthenticationResponse(jwt, refreshToken, responseJson);
            return ResponseEntity.status(HttpStatus.OK).body(responseJson).getBody();
        }
        throw new BadCredentialsException(responseJson.toString());
    }

    public ResponseJson validateLoginCorrectCredentials(String username, ResponseJson responseJson) {

        UserRecord userRecord = userRepository.findUseActiveByUseUsername(username);
        if (userRecord != null) {
            if (!Byte.valueOf("1").equals(userRecord.useActive())) {
                responseJson.setValidationCode("user.not.active");
                return responseJson;
            }

            String passwordActiveDuration = vehiclePreferenceRepository.findVehiclePreferenceCecValueByCecName("password_active_duration");

            if (passwordActiveDuration != null && Integer.parseInt(passwordActiveDuration) > 0) {
                int daysSincePasswordModified = (int) ChronoUnit.DAYS.between(userRecord.usePasswordLastModified(), LocalDate.now());

                if(daysSincePasswordModified >= Integer.parseInt(passwordActiveDuration)) {
                    responseJson.setValidationCode("user.password.is.expired");
                    return responseJson;
                }
            }

            List<String> userRolesList = userRoleRepository.findUsrNameByUsrUseUsername(username);

            if (userRolesList.isEmpty()) {
                responseJson.setValidationCode("user.has.not.user.role");
                return responseJson;
            }
        }
        return new ResponseJson();
    }

    public void buildAuthenticationResponse(String jwt, String refreshToken, ResponseJson responseJson) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("Jwt", jwt);
        responseMap.put("RefreshToken", refreshToken);

        responseJson.setEntity(responseMap);
        responseJson.setValidationCode("user.login.success");
    }

    public ResponseJson checkPasswordActiveDuration(String username, ResponseJson responseJson) {
        String passwordActiveDuration = vehiclePreferenceRepository.findVehiclePreferenceCecValueByCecName("password_active_duration");

        if (passwordActiveDuration != null && Integer.parseInt(passwordActiveDuration) > 0) {
            LocalDate datePasswordLastModified =  userRepository.findUsePasswordLastModifiedFromUserByUseUsername(username);
            int daysSincePasswordModified = (int) ChronoUnit.DAYS.between(datePasswordLastModified, LocalDate.now());

            if(daysSincePasswordModified >= Integer.parseInt(passwordActiveDuration)) {
                responseJson.setValidationCode("user.password.is.expired");
            }
        }
        return responseJson;
    }

    public ResponseJson validateLoginIncorrectCredentials(String username, String ipAddress, ResponseJson responseJson) {
        responseJson = checkIpAddressBlocking(ipAddress, username, responseJson);
        if(responseJson.getValidationCode() != null) {
            throw new BadCredentialsException(responseJson.toString());
        }
        responseJson = authUtil.validateMaximumLoginAttemptsIncorrectCredentials(username, responseJson);
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

    public ResponseJson logout(String username) {
        authenticationTokensCache.updateAuthenticationToken(username,null,null, null);
        Integer isUpdate = authUtil.updateUserMarkAsActive(username, Byte.valueOf("0"));
        return new ResponseJson(isUpdate > 0 ? "logout.success" : "logout.fail");
    }

    public ResponseJson refreshToken() {

        UserDetail userDetails = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // While generating refresh token do not generate new UUID
        // Keeping same UUID
        String uuid = authenticationTokenRepository.findUuidByUsername(userDetails.getUsername());

        // Generate new tokens
        Map<String,String> tokens =  jwtUtil.generateTokens(userDetails);
        // Update authentication_tokens table
        authenticationTokensCache.updateAuthenticationToken(userDetails.getUsername(), tokens.get("jwtToken"), tokens.get("refreshToken"), uuid);

        return new ResponseJson("refresh.token.fetch.success" ,Map.of("refreshToken", tokens));
    }

    public ResponseJson duplicateCheck(String username) {
        boolean userExist = userRepository.existsByUseUsername(username);
        return new ResponseJson(userExist ? "user.already.exist" : "user.not.found");
    }
}
