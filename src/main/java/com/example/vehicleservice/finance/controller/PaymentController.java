package com.example.vehicleservice.finance.controller;

import com.example.vehicleservice.finance.service.PaymentService;
import com.example.vehicleservice.general.json.ResponseJson;
import com.example.vehicleservice.swagger.GlobalApiResponses;
import com.razorpay.RazorpayException;
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Tag(name = "Payment controller", description = "This is the payment controller hold the payment related APIs")
@RestController
@CrossOrigin
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(summary = "Create order", description = "This API is used to create order", security = @SecurityRequirement(name = "bearerAuth"))
    @Parameter(name = "amount", description = "This is the amount", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(description = "Shows Success",
                    example = "create.order.success : Order created successfully")))})
    @GlobalApiResponses
    @PostMapping("/create-order")
    public ResponseEntity<ResponseJson> createOrder(@RequestParam @Min(1) @Max(8388607) Double amount) throws RazorpayException {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.createOrder(amount));
    }

    @Operation(summary = "Verify payment", description = "This API is used to verify payment", security = @SecurityRequirement(name = "bearerAuth"))
//    @Parameter(name = "amount", description = "This is the amount", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(description = "Shows Success",
                    example = "appointment.book.success : Appointment book successfully")))})
    @GlobalApiResponses
    @PostMapping("/verify")
    public ResponseEntity<ResponseJson> verifyPayment(@RequestBody Map<String, String> payload) throws NoSuchAlgorithmException, InvalidKeyException {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.verifyPayment(payload));
    }

}
