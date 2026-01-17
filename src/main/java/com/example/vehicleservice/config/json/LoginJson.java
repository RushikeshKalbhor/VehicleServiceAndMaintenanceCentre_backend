package com.example.vehicleservice.config.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "This json shows login related fields")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginJson {

    @Schema(description = "This field will store the username", example = "Rushikesh.kb")
    @NotBlank
    private String username;

    @Schema(description = "This field will store the password", example = "Clinical@2025")
    @NotBlank
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
