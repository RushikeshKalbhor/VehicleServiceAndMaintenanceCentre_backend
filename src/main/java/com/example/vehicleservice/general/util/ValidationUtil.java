package com.example.vehicleservice.general.util;

import org.springframework.stereotype.Component;

@Component
public class ValidationUtil {

    public boolean isNullOrEmpty(String data) {
        return data == null || data.isEmpty();
    }

}
