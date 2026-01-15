package com.example.vehicleservice.general.json;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionJson {

    private Integer cegId;
    private Map<String, String> parameters;
    private ArrayList<ValidationExceptionJson> validationException = new ArrayList<>();

    public Integer getCegId() {
        return cegId;
    }

    public void setCegId(Integer cegId) {
        this.cegId = cegId;
    }

    public List<ValidationExceptionJson> getValidationException() {
        return validationException;
    }

    public void setValidationException(ValidationExceptionJson validationExceptionJson) {
        this.validationException.add(validationExceptionJson);
    }

    public void setValidationException(String validationMessage) {
        ValidationExceptionJson validationExceptionJson = new ValidationExceptionJson();
        validationExceptionJson.setMessageKey(validationMessage);
        this.validationException.add(validationExceptionJson);
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
