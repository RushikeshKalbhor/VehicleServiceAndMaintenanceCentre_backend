package com.example.vehicleservice.general.json;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ValidationExceptionJson {

    private String messageKey;
    private Map<String, String> parameters= new HashMap<>();

    public String getMessageKey() {
        return messageKey;
    }
    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }
    public Map<String, String> getParameters() {
        return parameters;
    }
    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}

