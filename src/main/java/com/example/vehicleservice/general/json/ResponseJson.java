package com.example.vehicleservice.general.json;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseJson {

    private String validationCode;
    private Object entity;

    public ResponseJson() {
        //
    }

    @Override
    public String toString() {
        return "{ \"validationCode\":" + validationCode + "}";
    }

    public ResponseJson(String validationCode) {
        this.validationCode = validationCode;
    }

    public ResponseJson(String validationCode, Object entity) {
        this.validationCode = validationCode;
        this.entity = entity;
    }

    public String getValidationCode() {
        return validationCode;
    }

    public void setValidationCode(String validationCode) {
        this.validationCode = validationCode;
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }
}
