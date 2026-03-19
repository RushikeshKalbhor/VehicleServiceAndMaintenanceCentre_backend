package com.example.vehicleservice.email.controller;

import com.example.vehicleservice.email.service.EmailService;
import com.example.vehicleservice.general.json.ResponseJson;
import com.example.vehicleservice.swagger.GlobalApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Email controller", description = "This is the email controller hold the email related APIs")
@RestController
@CrossOrigin
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @Operation(summary = "send otp to user", description = "This API is used to get send the otp to user")
    @Parameter(name = "username", description = "This is the username", schema = @Schema(type = "string", minLength = 1, maxLength = 40), required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(description = "Shows Found/ Not found",
                    example = "user.not.found : User not found, otp.sent.on.registerd.email : Otp sent successfully")))})
    @GlobalApiResponses
    @GetMapping("/email/otp/send")
    public ResponseEntity<ResponseJson> otpSend(@RequestParam @Size(max = 40) String username) {
        return ResponseEntity.status(HttpStatus.OK).body(emailService.otpSend(username));
    }

    @Operation(summary = "Verify otp of user", description = "This API is used to verify user otp")
    @Parameter(name = "username", description = "This is the username", schema = @Schema(type = "string", minLength = 1, maxLength = 40), required = true)
    @Parameter(name = "otp", description = "This is the otp", schema = @Schema(type = "string", minLength = 6, maxLength = 6), required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(description = "Shows matched/ Not matched",
                    example = "otp.matched : User otp matched, otp.not.matched : Otp is not matched, user.not.found : User not found")))})
    @GlobalApiResponses
    @GetMapping("/email/otp/verify")
    public ResponseEntity<ResponseJson> otpVerify(@RequestParam @Size(max = 40) String username, @RequestParam @Size(min = 6, max = 6) String otp) {
        return ResponseEntity.status(HttpStatus.OK).body(emailService.otpVerify(username, otp));
    }

    @Operation(summary = "Confirm password", description = "This API is used to confirm password")
    @Parameter(name = "username", description = "This is the username", schema = @Schema(type = "string", minLength = 1, maxLength = 40), required = true)
    @Parameter(name = "password", description = "This is the password", schema = @Schema(type = "string", minLength = 8, maxLength = 20), required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(description = "Shows found/ Not found",
                    example = "password.updated : Password updated successfully, password.update.fail : Password update failed")))})
    @GlobalApiResponses
    @GetMapping("/confirm/password")
    public ResponseEntity<ResponseJson> confirmPassword(@RequestParam @Size(max = 40) String username, @RequestParam @Size(min = 8, max = 20) String password) {
        return ResponseEntity.status(HttpStatus.OK).body(emailService.confirmPassword(username, password));
    }
}
