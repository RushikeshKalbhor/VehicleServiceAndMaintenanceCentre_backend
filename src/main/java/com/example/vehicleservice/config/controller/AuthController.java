package com.example.vehicleservice.config.controller;

import com.example.vehicleservice.admin.model.User;
import com.example.vehicleservice.config.json.LoginJson;
import com.example.vehicleservice.config.json.UserRegisterJson;
import com.example.vehicleservice.general.json.ResponseJson;
import com.example.vehicleservice.config.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseJson> register(@RequestBody UserRegisterJson userRegisterJson) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.register(userRegisterJson));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseJson> login(@RequestBody LoginJson loginJson) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginJson));
    }
}
