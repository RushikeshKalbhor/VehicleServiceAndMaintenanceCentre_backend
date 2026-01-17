package com.example.vehicleservice.config.util;

import com.example.vehicleservice.admin.repository.UserRepository;
import com.example.vehicleservice.general.json.ResponseJson;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AuthUtil {

    private final UserRepository userRepository;

    public AuthUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public ResponseJson validateMaximumLoginAttemptsIncorrectCredentials(String username, ResponseJson responseJson) {
        Integer useLoginAttempts = userRepository.findUseLoginAttemptsByUseUsername(username);

        useLoginAttempts = useLoginAttempts == null || useLoginAttempts < 1 ? 1 : useLoginAttempts + 1;
        userRepository.updateUseLoginAttempts(username, useLoginAttempts);
        responseJson.setValidationCode("username.or.password.incorrect");
        return responseJson;
    }

    @Transactional
    public void updateUserMarkAsActive(String username, Byte markAsActive) {
        userRepository.updateUseLastLoggedinDateAndUseLoggedInByUseUsername(username, markAsActive);
    }
}
