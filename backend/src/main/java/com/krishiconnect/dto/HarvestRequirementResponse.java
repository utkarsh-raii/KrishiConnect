package com.krishiconnect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krishiconnect.entity.HarvestRequirement;

import java.time.LocalDateTime;

public class HarvestRequirementResponse {

    @JsonProperty("_id")
    private Long id;

    private UserDto farmer;
    private String vehicleType;
    private Integer manpower;
    private String cropType;
    private QuantityDto quantity;
    private DurationDto duration;
    private LocationDto location;
    private String urgency;
    private String notes;
    private String status;
    private Long acceptedBidId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public HarvestRequirementResponse() {}

    public static HarvestRequirementResponse fromEntity(HarvestRequirement req) {
        if (req == null) return null;
        HarvestRequirementResponse dto = new HarvestRequirementResponse();
        dto.setId(req.getId());
        dto.setFarmer(UserDto.fromEntity(req.getFarmer()));
        dto.setVehicleType(req.getVehicleType());
        dto.setManpower(req.getManpower());
        dto.setCropType(req.getCropType());
        dto.setQuantity(new QuantityDto(req.getQuantityAmount(), req.getQuantityUnit()));
        dto.setDuration(new DurationDto(req.getDurationValue(), req.getDurationUnit()));
        dto.setLocation(new LocationDto(req.getAddress(), req.getDistrict(), req.getState(), req.getPincode()));
        dto.setUrgency(req.getUrgency());
        dto.setNotes(req.getNotes());
        dto.setStatus(req.getStatus());
        dto.setAcceptedBidId(req.getAcceptedBidId());
        dto.setCreatedAt(req.getCreatedAt());
        dto.setUpdatedAt(req.getUpdatedAt());
        return dto;
    }

    // Expose both id and _id, as well as farmer/farmerId
    @JsonProperty("id")
    public Long getRawId() { return id; }

    @JsonProperty("farmerId")
    public UserDto getFarmerId() { return farmer; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserDto getFarmer() { return farmer; }
    public void setFarmer(UserDto farmer) { this.farmer = farmer; }

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

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getAcceptedBidId() { return acceptedBidId; }
    public void setAcceptedBidId(Long acceptedBidId) { this.acceptedBidId = acceptedBidId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
