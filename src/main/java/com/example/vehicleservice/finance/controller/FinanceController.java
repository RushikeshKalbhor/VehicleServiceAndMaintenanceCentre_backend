package com.example.vehicleservice.finance.controller;

import com.example.vehicleservice.finance.json.AddBillJson;
import com.example.vehicleservice.finance.service.FinanceService;
import com.example.vehicleservice.general.json.ResponseJson;
import com.example.vehicleservice.swagger.GlobalApiResponses;
import com.example.vehicleservice.vehicle.json.VehiclePostJson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                    example = "finance.bill.list.not.found : Finance bill list found successfully, " +
                            "finance.bill.list.found : Finance bill list not found")))})
    @GlobalApiResponses
    @GetMapping("/finance/bill/list")
    public ResponseEntity<ResponseJson> getFinanceBillList(@RequestParam (required = false) @Size(min = 6, max = 10) String vehicleNumber, @RequestParam (required = false) @Min(1) @Max(8388607) Integer pageNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(financeService.getFinanceBillList(vehicleNumber, pageNumber));
    }

    @Operation(summary = "Add bill", description = "This API is used to add bill", security = @SecurityRequirement(name = "bearerAuth"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description ="<b>Required json: AddBillJson</b> <br>")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(description = "Shows Success",
                    example = "finance.bill.add.success : Finance bill added successfully")))})
    @GlobalApiResponses
    @PostMapping("/add-bill")
    public ResponseEntity<ResponseJson> addBill(@RequestBody @Valid AddBillJson addBillJson) {
        return ResponseEntity.status(HttpStatus.OK).body(financeService.addBill(addBillJson));
    }

    @Operation(summary = "Get bill extra details", description = "This API is used to get bill extra details", security = @SecurityRequirement(name = "bearerAuth"))
    @Parameter(name = "bId", description = "This is the bill id", schema = @Schema(type = "integer", minimum = "1", maximum = "8388607"), required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(description = "Shows found/ not found",
                    example = "finance.bill.details.found : Finance bill details found successfully, finance.bill.details.not.found : Finance bill details not found")))})
    @GlobalApiResponses
    @GetMapping("/add-bill/extra-details")
    public ResponseEntity<ResponseJson> addBillExtraDetails(@RequestParam Integer bId) {
        return ResponseEntity.status(HttpStatus.OK).body(financeService.addBillExtraDetails(bId));
    }
}
