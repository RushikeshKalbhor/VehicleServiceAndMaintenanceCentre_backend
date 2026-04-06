package com.example.vehicleservice.finance.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "This json add bill item related fields")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddBillItemJson {

    @Schema(description = "This field will store the service name", example = "oil change")
    @Size(min = 1, max = 100)
    private String biServiceName;

    @Schema(description = "This field will store the quantity", example = "1")
    @NotNull
    @Min(1)
    @Max(8388607)
    private Integer biQuantity;

    @Schema(description = "This field will store the price of the service", example = "100")
    private Double biRate;

    @Schema(description = "This field will store the total of that service", example = "100")
    private Double biTotal;

    public String getBiServiceName() {
        return biServiceName;
    }

    public void setBiServiceName(String biServiceName) {
        this.biServiceName = biServiceName;
    }

    public Integer getBiQuantity() {
        return biQuantity;
    }

    public void setBiQuantity(Integer biQuantity) {
        this.biQuantity = biQuantity;
    }

    public Double getBiRate() {
        return biRate;
    }

    public void setBiRate(Double biRate) {
        this.biRate = biRate;
    }

    public Double getBiTotal() {
        return biTotal;
    }

    public void setBiTotal(Double biTotal) {
        this.biTotal = biTotal;
    }
}
