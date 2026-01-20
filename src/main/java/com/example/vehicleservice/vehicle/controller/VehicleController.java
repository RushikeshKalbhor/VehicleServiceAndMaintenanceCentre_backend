package com.example.vehicleservice.vehicle.controller;

import com.example.vehicleservice.general.json.ResponseJson;
import com.example.vehicleservice.swagger.GlobalApiResponses;
import com.example.vehicleservice.vehicle.json.VehiclePostJson;
import com.example.vehicleservice.vehicle.service.VehicleService;
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
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Vehicle controller", description = "This is the vehicle controller hold the vehicle related APIs")
@RestController
@CrossOrigin
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    // CUSTOMER
    @Operation(summary = "Add or register the vehicle", description = "This API is used to add or register the vehicle", security = @SecurityRequirement(name = "bearerAuth"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description ="<b>Required json: VehiclePostJson</b> <br>")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(description = "Shows Success",
                    example = "vehicle.add.success : Vehicle added successfully")))})
    @GlobalApiResponses
    @PreAuthorize("hasAuthority('customer')")
    @PostMapping("/customer/vehicles")
    public ResponseEntity<ResponseJson> addVehicle(@RequestBody @Valid VehiclePostJson vehiclePostJson) {
        return ResponseEntity.status(HttpStatus.OK).body(vehicleService.addVehicle(vehiclePostJson));
    }

    // CUSTOMER
    @Operation(summary = "get customer vehicle", description = "This API is used to get logged in customer vehicle", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(description = "Shows Found/ Not found",
                    example = "vehicle.not.found : Vehicle not found, vehicle.found : Vehicle details found")))})
    @GlobalApiResponses
    @PreAuthorize("hasAuthority('customer')")
    @GetMapping("/customer/vehicles")
    public ResponseEntity<ResponseJson> getMyVehicles() {
        return ResponseEntity.status(HttpStatus.OK).body(vehicleService.getMyVehicles());
    }

    // ADMIN
    @Operation(summary = "get all customer vehicle", description = "This API is used to get all customer vehicle", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(description = "Shows Found/ Not found",
                    example = "vehicle.not.found : Vehicle not found, vehicle.found : Vehicle details found")))})
    @GlobalApiResponses
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/admin/vehicles")
    public ResponseEntity<ResponseJson> getAllVehicles() {
        return ResponseEntity.status(HttpStatus.OK).body(vehicleService.getAllVehicles());
    }

    @Operation(summary = "Duplicate vehicle check", description = "This API is used to check vehicle is already present or not ", security = @SecurityRequirement(name = "bearerAuth"))
    @Parameter(name = "vehVehicleNumber", description = "This is the username", schema = @Schema(type = "string", minLength = 7, maxLength = 15), required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(description = "show Success",
                    example = """
                            vehicle.already.registered : Vehicle already registered,
                            vehicle.not.found : Vehicle not found
                            """))) })
    @GlobalApiResponses
    @GetMapping("/vehicle/duplicate-check")
    public ResponseEntity<ResponseJson> duplicateVehicleCheck(@RequestParam @NotBlank @Size(min = 7, max = 15) String vehVehicleNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(vehicleService.duplicateVehicleCheck(vehVehicleNumber));
    }
}
