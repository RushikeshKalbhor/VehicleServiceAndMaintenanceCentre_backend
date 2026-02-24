package com.example.vehicleservice.config.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "This json shows user edit related fields")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserEditJson {

    @Schema(description = "This field will store the username", example = "Rushikesh.kb")
    @NotBlank
    private String useUsername;

    @Schema(description = "This field will shows the user title", example = "Mr")
    @Size(min = 1, max = 10)
    private String useTitle;

    @Schema(description = "This field will shows the user firstname", example = "Rushikesh")
    @NotBlank
    @Size(max = 60)
    private String useFirstName;

    @Schema(description = "This field will shows the user surname", example = "Kalbhor")
    @NotBlank
    @Size(max = 60)
    private String useSurname;

    @Schema(description = "This field will shows the user is active or not", example = "1")
    @Min(0)
    @Max(1)
    @NotNull
    private Integer useActive;

    @Schema(description = "This field will shows the user email", example = "Rushikesh@mailinator.com")
    @Size(min = 1, max = 100)
    private String useEmail;

    @Schema(description = "This field will shows the user mobile no", example = "1234567890")
    @Size(min = 1, max = 20)
    private String useMobile;

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

    public Integer getUseActive() {
        return useActive;
    }

    public void setUseActive(Integer useActive) {
        this.useActive = useActive;
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
}
