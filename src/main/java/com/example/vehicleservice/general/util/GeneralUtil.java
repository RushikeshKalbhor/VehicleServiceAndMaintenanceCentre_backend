package com.example.vehicleservice.general.util;

import jakarta.persistence.Id;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;

@Component
public class GeneralUtil {

    public String getFieldNameWithIdAnnotation(Class<?> modelName) {
        return Arrays.stream(modelName.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .map(Field::getName)
                .findFirst()
                .orElse(null); // Return null if no such field is found
    }
}
