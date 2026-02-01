package com.example.vehicleservice.vehicle.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "This json shows vehicle registration related fields")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehiclePostJson {

    @Schema(description = "This field will store the vehicle number", example = "MH12AA0001")
    @NotBlank
    @Size(min = 7, max = 15)
    private String vehVehicleNumber;

    @Schema(description = "This field will store the vehicle type e.g car, bike", example = "car")
    @Pattern(regexp = "^(car|bike)$", message = "vehVehicleType must be car or bike")
    @NotBlank
    private String vehVehicleType;

    @Schema(description = "This field will store the vehicle brand", example = "Tata")
    @NotBlank
    @Size(min = 1, max = 30)
    private String vehBrand;

    @Schema(description = "This field will store the vehicle name", example = "Nexon")
    @NotBlank
    @Size(min = 1, max = 30)
    private String vehModel;

    @Schema(description = "This field will store the vehicle manufacturing year", example = "2017")
    @Min(1950)
    @Max(2050)
    private Integer vehManufacturingYear;

    public String getVehVehicleNumber() {
        return vehVehicleNumber;
    }

    public void setVehVehicleNumber(String vehVehicleNumber) {
        this.vehVehicleNumber = vehVehicleNumber;
    }

    public String getVehVehicleType() {
        return vehVehicleType;
    }

    public void setVehVehicleType(String vehVehicleType) {
        this.vehVehicleType = vehVehicleType;
    }

    public String getVehBrand() {
        return vehBrand;
    }

    public void setVehBrand(String vehBrand) {
        this.vehBrand = vehBrand;
    }

    public String getVehModel() {
        return vehModel;
    }

    public void setVehModel(String vehModel) {
        this.vehModel = vehModel;
    }

    public Integer getVehManufacturingYear() {
        return vehManufacturingYear;
    }

    public void setVehManufacturingYear(Integer vehManufacturingYear) {
        this.vehManufacturingYear = vehManufacturingYear;
    }
}
