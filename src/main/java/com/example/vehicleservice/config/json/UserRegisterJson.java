package com.example.vehicleservice.config.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "This json shows user registration related fields")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRegisterJson {

    @Schema(description = "This field will store the username", example = "Rushikesh.k")
    @NotBlank
    private String useUsername;

    @Schema(description = "This field will store the users title", example = "Mr")
    private String useTitle;

    @Schema(description = "This field will store the user first name", example = "Rushikesh")
    @NotBlank
    private String useFirstName;

    @Schema(description = "This field will store the user surname", example = "kalbhor")
    @NotBlank
    private String useSurname;

    @Schema(description = "This field will store the users email", example = "Rushikesh.kalbhor@mailinator.com")
    private String useEmail;

    @Schema(description = "This field will store the users mobile number", example = "1234567890")
    private String useMobile;

    @Schema(description = "This field will store the password", example = "Clinical@2025")
    @NotBlank
    private String usePassword;

    @Schema(description = "This field will store the password", example = "Clinical@2025")
    @NotNull
    @Min(0)
    @Max(1)
    private Integer useActive;

    public String getUseUsername() {
        return useUsername;
    }

    public void setUseUsername(String useUsername) {
        this.useUsername = useUsername;
    }

    public String getUseTitle() {
        return useTitle;
    }

    public void setUseTitle(String useTitle) {
        this.useTitle = useTitle;
    }

    public String getUseFirstName() {
        return useFirstName;
    }

    public void setUseFirstName(String useFirstName) {
        this.useFirstName = useFirstName;
    }

    public String getUseSurname() {
        return useSurname;
    }

    public void setUseSurname(String useSurname) {
        this.useSurname = useSurname;
    }

    public String getUseEmail() {
        return useEmail;
    }

    public void setUseEmail(String useEmail) {
        this.useEmail = useEmail;
    }

    public String getUseMobile() {
        return useMobile;
    }

    public void setUseMobile(String useMobile) {
        this.useMobile = useMobile;
    }

    public String getUsePassword() {
        return usePassword;
    }

    public void setUsePassword(String usePassword) {
        this.usePassword = usePassword;
    }

    public Integer getUseActive() {
        return useActive;
    }

    public void setUseActive(Integer useActive) {
        this.useActive = useActive;
    }
}
