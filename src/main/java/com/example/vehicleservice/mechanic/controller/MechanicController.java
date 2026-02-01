package com.example.vehicleservice.mechanic.controller;

import com.example.vehicleservice.general.json.ResponseJson;
import com.example.vehicleservice.mechanic.service.MechanicService;
import com.example.vehicleservice.swagger.GlobalApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Mechanic controller", description = "This is the mechanic controller hold the mechanic related APIs")
@RestController
@CrossOrigin
public class MechanicController {

    private final MechanicService mechanicService;

    public MechanicController(MechanicService mechanicService) {
        this.mechanicService = mechanicService;
    }

    @Operation(summary = "Get mechanic list", description = "This API is used to get mechanic list", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(description = "Shows found/not found",
                    example = "mechanic.list.found : Mechanic list found successfully, " +
                            "mechanic.list.not.found : Mechanic list not found")))})
    @GlobalApiResponses
    @GetMapping("/mechanic/list")
    public ResponseEntity<ResponseJson> getMechanicList() {
        return ResponseEntity.status(HttpStatus.OK).body(mechanicService.getMechanicList());
    }
}
