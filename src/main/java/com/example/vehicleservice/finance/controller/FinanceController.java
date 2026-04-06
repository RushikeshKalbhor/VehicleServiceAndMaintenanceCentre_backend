package com.example.vehicleservice.finance.controller;

import com.example.vehicleservice.finance.service.FinanceService;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Finance controller", description = "This is the finance controller hold the finance related APIs")
@RestController
@CrossOrigin
public class FinanceController {

    private final FinanceService financeService;

    public FinanceController(FinanceService financeService) {
        this.financeService = financeService;
    }

    @Operation(summary = "Get finance bill list", description = "This API is used to get finance bill list", security = @SecurityRequirement(name = "bearerAuth"))
    @Parameter(name = "vehicleNumber", description = "This is the vehicle number", schema = @Schema(type = "string", minLength = 6, maxLength = 10), required = false)
    @Parameter(name = "pageNumber", description = "This is the page number", schema = @Schema(type = "integer", minimum = "1", maximum = "8388607"), required = false)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(description = "Shows found/not found",
                    example = "job.card.found : Job card details found successfully, " +
                            "job.card.not.found : Job card not found")))})
    @GlobalApiResponses
    @GetMapping("/finance/bill/list")
    public ResponseEntity<ResponseJson> getFinanceBillList(@RequestParam (required = false) @Size(min = 6, max = 10) String vehicleNumber, @RequestParam (required = false) @Min(1) @Max(8388607) Integer pageNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(financeService.getFinanceBillList(vehicleNumber, pageNumber));
    }
}
