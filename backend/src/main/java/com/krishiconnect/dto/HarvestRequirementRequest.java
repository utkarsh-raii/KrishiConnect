package com.krishiconnect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krishiconnect.entity.HarvestRequirement;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class HarvestRequirementRequest {

    @NotBlank(message = "Vehicle type is required")
    private String vehicleType;

    private Integer manpower = 0;

    @NotBlank(message = "Crop type is required")
    private String cropType;

    @NotNull(message = "Quantity is required")
    private QuantityDto quantity;

    @NotNull(message = "Duration is required")
    private DurationDto duration;

    @NotNull(message = "Location is required")
    private LocationDto location;

    private String urgency = "normal";
    private String notes;

    public HarvestRequirementRequest() {}

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public Integer getManpower() { return manpower; }
    public void setManpower(Integer manpower) { this.manpower = manpower; }

    public String getCropType() { return cropType; }
    public void setCropType(String cropType) { this.cropType = cropType; }

    public QuantityDto getQuantity() { return quantity; }
    public void setQuantity(QuantityDto quantity) { this.quantity = quantity; }

    public DurationDto getDuration() { return duration; }
    public void setDuration(DurationDto duration) { this.duration = duration; }

    public LocationDto getLocation() { return location; }
    public void setLocation(LocationDto location) { this.location = location; }

    public String getUrgency() { return urgency; }
    public void setUrgency(String urgency) { this.urgency = urgency; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
