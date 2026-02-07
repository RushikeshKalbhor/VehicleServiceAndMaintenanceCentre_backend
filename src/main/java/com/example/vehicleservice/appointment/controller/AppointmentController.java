package com.example.vehicleservice.appointment.controller;

import com.example.vehicleservice.appointment.service.AppointmentService;
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Appointment controller", description = "This is the appointment controller hold the appointment related APIs")
@RestController
@CrossOrigin
public class AppointmentController {

    private final AppointmentService  appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Operation(summary = "book appointment", description = "This API is used to book appointment", security = @SecurityRequirement(name = "bearerAuth"))
    @Parameter(name = "aptVehId", description = "This is the vehicle id", schema = @Schema(type = "integer"), required = true)
    @Parameter(name = "aptDate", description = "This is the appointment date must be pass in dd/mm/yyyy ", schema = @Schema(type = "string"), required = true)
    @Parameter(name = "aptProblemDescription", description = "This is the problem description", schema = @Schema(type = "string", maxLength = 500), required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(description = "Shows Success",
                    example = "appointment.book.success : Appointment book successfully")))})
    @GlobalApiResponses
    @PreAuthorize("hasAuthority('customer')")
    @PostMapping("/customer/appointments")
    public ResponseEntity<ResponseJson> bookAppointment(@RequestParam @Min(1) @Max(8388607) Integer aptVehId,
                                                        @RequestParam @NotBlank String aptDate,
                                                        @RequestParam @NotBlank @Size(max = 500) String aptProblemDescription) {
        return ResponseEntity.status(HttpStatus.OK).body(appointmentService.bookAppointment(aptVehId, aptDate, aptProblemDescription));
    }

    // CUSTOMER
    @Operation(summary = "Get appointment", description = "This API is used to get booked appointment", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(description = "Shows found/not found",
                    example = "customer.appointment.found : Customer appointment found successfully, " +
                            "customer.appointment.not.found : Customer appointment not found")))})
    @GlobalApiResponses
    @PreAuthorize("hasAuthority('customer')")
    @GetMapping("/customer/appointments")
    public ResponseEntity<ResponseJson> myAppointments() {
        return ResponseEntity.status(HttpStatus.OK).body(appointmentService.myAppointments());
    }

    // ADMIN
    @Operation(summary = "Approve appointment", description = "This API is used to approve appointment", security = @SecurityRequirement(name = "bearerAuth"))
    @Parameter(name = "aptId", description = "This is the appointment id", schema = @Schema(type = "integer", minimum = "1", maximum = "8388607"), required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(description = "Shows Success/failed",
                    example = "appointment.approval.failed : Appointment approval failed, " +
                            "appointment.approved : Appointment approved successfully")))})
    @GlobalApiResponses
    @PutMapping("/customer/appointments/approve")
    public ResponseEntity<ResponseJson> approveAppointment(@RequestParam @Min(1) @Max(8388607) Integer aptId) {
        return ResponseEntity.status(HttpStatus.OK).body(appointmentService.approveAppointment(aptId));
    }

    // ADMIN
    @Operation(summary = "Reject appointment", description = "This API is used to reject appointment", security = @SecurityRequirement(name = "bearerAuth"))
    @Parameter(name = "aptId", description = "This is the appointment id", schema = @Schema(type = "integer", minimum = "1", maximum = "8388607"), required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(description = "Shows Success/failed",
                    example = "appointment.reject.failed : Appointment reject failed, " +
                            "appointment.rejected : Appointment rejected successfully")))})
    @GlobalApiResponses
    @PutMapping("/customer/appointments/reject")
    public ResponseEntity<ResponseJson> rejectAppointment(@RequestParam @Min(1) @Max(8388607) Integer aptId) {
        return ResponseEntity.status(HttpStatus.OK).body(appointmentService.rejectAppointment(aptId));
    }

    @Operation(summary = "Assign mechanic", description = "This API is used to assign mechanic to appointment", security = @SecurityRequirement(name = "bearerAuth"))
    @Parameter(name = "aptId", description = "This is the appointment id", schema = @Schema(type = "integer", minimum = "1", maximum = "8388607"), required = true)
    @Parameter(name = "username", description = "This is the mechanic username", schema = @Schema(type = "string", maxLength = 40), required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(description = "Shows Success/Failed",
                    example = """
                            mechanic.assigned.success : Mechanic assigned successfully,
                            mechanic.assigned.failed : Failed to assign mechanic
                            """)))})
    @GlobalApiResponses
    @PutMapping("/admin/appointments/assign")
    public ResponseEntity<ResponseJson> assignMechanic(@RequestParam @Min(1) @Max(8388607) Integer aptId,
                                      @RequestParam @NotBlank @Size(max = 40) String username) {
        return ResponseEntity.status(HttpStatus.OK).body(appointmentService.assignMechanic(aptId, username));
    }

    // MECHANIC
    @Operation(summary = "Get mechanic appointments", description = "This API is used to get mechanic appointment", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(description = "Shows Found/Not found",
                    example = """
                            mechanic.appointment.details.found : Mechanic appointment details found successfully,
                            mechanic.appointment.details.not.found : Mechanic appointment details not found
                            """)))})
    @GlobalApiResponses
    @PreAuthorize("hasAuthority('mechanic')")
    @GetMapping("/mechanic/appointments")
    public ResponseEntity<ResponseJson> mechanicAppointments() {
        return ResponseEntity.status(HttpStatus.OK).body(appointmentService.mechanicAppointments());
    }

    // ADMIN
    @Operation(summary = "Get admin appointment", description = "This API is used to get admin appointment list", security = @SecurityRequirement(name = "bearerAuth"))
    @Parameter(name = "pageNumber", description = "This is the page number", schema = @Schema(type = "integer", minimum = "1", maximum = "8388607"), required = false)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(description = "Shows found/not found",
                    example = "appointment.list.found : Appointment list found successfully, " +
                            "appointment.list.not.found : Appointment list not found")))})
    @GlobalApiResponses
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/admin/appointments/list")
    public ResponseEntity<ResponseJson> getAdminAppointmentList(@RequestParam (required = false) @Min(1) @Max(8388607) Integer pageNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(appointmentService.getAdminAppointmentList(pageNumber));
    }

    @Operation(summary = "Delete appointment", description = "This API is used to delete appointment", security = @SecurityRequirement(name = "bearerAuth"))
    @Parameter(name = "aptId", description = "This is the appointment id", schema = @Schema(type = "integer", minimum = "1", maximum = "8388607"), required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(description = "show Success/Fail",
                    example = """
                            appointment.delete.success : Appointment delete successfully,
                            appointment.delete.fail : Failed to delete appointment
                            """))) })
    @GlobalApiResponses
    @DeleteMapping("/appointment")
    public ResponseEntity<ResponseJson> deleteAppointment(@RequestParam @Min(1) @Max(8388607) Integer aptId) {
        return ResponseEntity.status(HttpStatus.OK).body(appointmentService.deleteAppointment(aptId));
    }
}
