package com.example.vehicleservice.finance.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RazorpayConfig {

    @Value("${razorpay.key}")
    private String key;

    @Value("${razorpay.secret}")
    private String secret;

    public String getKey() {
        return key;
    }

    public String getSecret() {
        return secret;
    }
}