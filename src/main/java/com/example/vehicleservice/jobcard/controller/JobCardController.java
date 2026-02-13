package com.example.vehicleservice.jobcard.controller;

import com.example.vehicleservice.general.json.ResponseJson;
import com.example.vehicleservice.jobcard.service.JobCardService;
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
import org.springframework.web.bind.annotation.*;

@Tag(name = "Job card controller", description = "This is the job card controller hold the job card related APIs")
@RestController
@CrossOrigin
public class JobCardController {

    private final JobCardService jobCardService;

    public JobCardController(JobCardService jobCardService) {
        this.jobCardService = jobCardService;
    }


    // ADMIN
    @Operation(summary = "Create job card", description = "This API is used to create job card", security = @SecurityRequirement(name = "bearerAuth"))
    @Parameter(name = "aptId", description = "This is the appointment id", schema = @Schema(type = "integer", minimum = "1", maximum = "8388607"), required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(description = "Shows Success",
                    example = "job.card.create.success : Job card created successfully, " +
                            "mechanic.not.assigned : Mechanic not assigned for given appointment")))})
    @GlobalApiResponses
    @PostMapping("/admin/job-cards")
    public ResponseEntity<ResponseJson> createJobCard(@RequestParam @Min(1) @Max(8388607) Integer aptId) {
        return ResponseEntity.status(HttpStatus.OK).body(jobCardService.createJobCard(aptId));
    }

    // MECHANIC
    @Operation(summary = "Update job card", description = "This API is used to update job card", security = @SecurityRequirement(name = "bearerAuth"))
    @Parameter(name = "jcId", description = "This is the job card id", schema = @Schema(type = "integer", minimum = "1", maximum = "8388607"), required = true)
    @Parameter(name = "jcStatus", description = "This is the job card status", schema = @Schema(type = "string", minLength = 1, maxLength = 50), required = true)
    @Parameter(name = "jcInspectionNotes", description = "This is the job card inspection notes", schema = @Schema(type = "string", minLength = 1, maxLength = 2000), required = false)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(description = "Shows Success",
                    example = "job.card.update.success : Job card updated successfully, " +
                            "job.card.not.found : Job card not found")))})
    @GlobalApiResponses
    @PutMapping("/admin/job-cards")
    public ResponseEntity<ResponseJson> updateService(@RequestParam @Min(1) @Max(8388607) Integer jcId,
                                                      @RequestParam @NotBlank @Size(max = 50) String jcStatus,
                                                      @RequestParam (required = false) @Size(max = 2000) String jcInspectionNotes) {
        return ResponseEntity.status(HttpStatus.OK).body(jobCardService.updateService(jcId, jcStatus, jcInspectionNotes));
    }

    // ADMIN / CUSTOMER / MECHANIC
    @Operation(summary = "Get job card", description = "This API is used to get job card", security = @SecurityRequirement(name = "bearerAuth"))
    @Parameter(name = "aptId", description = "This is the appointment id", schema = @Schema(type = "integer", minimum = "1", maximum = "8388607"), required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(description = "Shows found/not found",
                    example = "job.card.found : Job card details found successfully, " +
                            "job.card.not.found : Job card not found")))})
    @GlobalApiResponses
    @GetMapping("/job-cards")
    public ResponseEntity<ResponseJson> getJobCard(@RequestParam @Min(1) @Max(8388607) Integer aptId) {
        return ResponseEntity.status(HttpStatus.OK).body(jobCardService.getJobCard(aptId));
    }
}
