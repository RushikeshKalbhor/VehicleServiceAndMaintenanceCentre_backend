package com.example.vehicleservice.finance.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "This json add bill related fields")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddBillJson {

    @Schema(description = "This field will store the bill id", example = "1")
    @Min(1)
    @Max(8388607)
    @JsonProperty("bId")
    private Integer bId;

    @Schema(description = "This field will store the appointment id", example = "1")
    @NotNull
    @Min(1)
    @Max(8388607)
    @JsonProperty("bAptId")
    private Integer bAptId;

    @Schema(description = "This field will store the job card id", example = "1")
    @NotNull
    @Min(1)
    @Max(8388607)
    @JsonProperty("bJcId")
    private Integer bJcId;

    @Schema(description = "This field will store the total of the bill", example = "100.00")
    @NotNull
    @JsonProperty("bTotal")
    private Double bTotal;

    @Schema(description = "This field will store the discount", example = "5")
    @Min(1)
    @Max(99)
    @JsonProperty("bDiscount")
    private Integer bDiscount;

    @Schema(description = "This field will store the final bill amount", example = "95.00")
    @NotNull
    @JsonProperty("bFinalTotal")
    private Double bFinalTotal;

    @Schema(description = "This field will store the final item")
    @NotEmpty
    private List<@Valid AddBillItemJson> addBillItemJson;

    @JsonIgnore
    public Integer getBId() {
        return bId;
    }

    public void setBId(Integer bId) {
        this.bId = bId;
    }

    @JsonIgnore
    public Integer getBAptId() {
        return bAptId;
    }

    public void setBAptId(Integer bAptId) {
        this.bAptId = bAptId;
    }

    @JsonIgnore
    public Integer getBJcId() {
        return bJcId;
    }

    public void setBJcId(Integer bJcId) {
        this.bJcId = bJcId;
    }

    @JsonIgnore
    public Double getBTotal() {
        return bTotal;
    }

    public void setBTotal(Double bTotal) {
        this.bTotal = bTotal;
    }

    @JsonIgnore
    public Integer getBDiscount() {
        return bDiscount;
    }

    public void setBDiscount(Integer bDiscount) {
        this.bDiscount = bDiscount;
    }

    @JsonIgnore
    public Double getBFinalTotal() {
        return bFinalTotal;
    }

    public void setBFinalTotal(Double bFinalTotal) {
        this.bFinalTotal = bFinalTotal;
    }

    public List<AddBillItemJson> getAddBillItemJson() {
        return addBillItemJson;
    }

    public void setAddBillItemJson(List<AddBillItemJson> addBillItemJson) {
        this.addBillItemJson = addBillItemJson;
    }
}
