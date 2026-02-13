package com.example.vehicleservice.config.controller;

import com.example.vehicleservice.config.json.LoginJson;
import com.example.vehicleservice.config.json.UserRegisterJson;
import com.example.vehicleservice.config.service.AuthService;
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
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth controller", description = "This is the auth controller hold the login related APIs")
@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Add or register the user", description = "This API is used to add or register the user")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description ="<b>Required json: UserRegisterJson</b> <br>")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(description = "Shows Success",
                    example = "user.registered.success : User added successfully")))})
    @GlobalApiResponses
    @PostMapping("/register")
    public ResponseEntity<ResponseJson> register(@RequestBody @Valid UserRegisterJson userRegisterJson) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.register(userRegisterJson));
    }

    @Operation(summary = "Get login into the application", description = "This API is used to login vehicle service and maintenance centre application")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description ="<b>Required json: LoginJson</b> <br>")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(description = "Shows Success/Fail",
                    example = """
                            username.or.password.incorrect : Username or password is incorrect,
                            user.not.active : User is not active,
                            user.password.is.expired : User password is expired,
                            user.has.not.user.role : User is not associated with user roles,
                            user.login.success : User logged in successfully,
                            area.users.ipblocked : User ip is blocked
                            """)))})
    @GlobalApiResponses
    @PostMapping("/login")
    public ResponseEntity<ResponseJson> login(@Valid @RequestBody LoginJson loginJson) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginJson));
    }

    @Operation(summary = "Logout user", description = "This API used for logout the user", security = @SecurityRequirement(name = "bearerAuth"))
    @Parameter(name = "username", description = "This is the username", schema = @Schema(type = "string", maxLength = 60), required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(description = "show Success / Fail",
                    example = "logout.success : Logout successful, logout.fail : Logout Fail "))) })
    @GlobalApiResponses
    @DeleteMapping("/logout")
    public ResponseEntity<ResponseJson> logout(@RequestParam @NotBlank String username) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.logout(username));
    }

    @Operation(summary = "Refresh Token", description = "API is provide new jwt and refresh token to the user", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(description = "show Success",
                    example = "refresh.token.fetch.success : Updated token fetch successfully"))) })
    @GlobalApiResponses
    @GetMapping("/refresh-token")
    public ResponseEntity<ResponseJson> refreshToken(){
        return ResponseEntity.status(HttpStatus.OK).body(authService.refreshToken());
    }


    @Operation(summary = "Duplicate user check", description = "This API is used to check user is already present or not ")
    @Parameter(name = "username", description = "This is the username", schema = @Schema(type = "string", maxLength = 60), required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(description = "show Success",
                    example = """
                            user.already.exist : User already exist for given username,
                            user.not.found : User not found for given username
                            """))) })
    @GlobalApiResponses
    @GetMapping("/user/duplicate-check")
    public ResponseEntity<ResponseJson> duplicateCheck(@RequestParam @NotBlank String username) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.duplicateCheck(username));
    }

    @Operation(summary = "Get user list", description = "This API is used to get user list", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(description = "show Found/Not found",
                    example = """
                            user.list.found : User list found,
                            user.list.not.found : User list not found
                            """))) })
    @GlobalApiResponses
    @GetMapping("/user/list")
    public ResponseEntity<ResponseJson> getUserList() {
        return ResponseEntity.status(HttpStatus.OK).body(authService.getUserList());
    }
}
