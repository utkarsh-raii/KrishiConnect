package com.krishiconnect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krishiconnect.entity.BidOffer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BidResponseDto {

    @JsonProperty("_id")
    private Long id;

    @JsonProperty("providerId")
    private UserDto provider;

    @JsonProperty("farmerId")
    private UserDto farmer;

    @JsonProperty("requirementId")
    private HarvestRequirementResponse requirement;

    private Long serviceId;
    private BigDecimal price;
    private String priceType;
    private String message;
    private String status;
    private LocalDateTime respondedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BidResponseDto() {}

    public static BidResponseDto fromEntity(BidOffer bid) {
        if (bid == null) return null;
        BidResponseDto dto = new BidResponseDto();
        dto.setId(bid.getId());
        dto.setProvider(UserDto.fromEntity(bid.getProvider()));
        dto.setFarmer(UserDto.fromEntity(bid.getFarmer()));
        dto.setRequirement(HarvestRequirementResponse.fromEntity(bid.getRequirement()));
        if (bid.getService() != null) {
            dto.setServiceId(bid.getService().getId());
        }
        dto.setPrice(bid.getPrice());
        dto.setPriceType(bid.getPriceType());
        dto.setMessage(bid.getMessage());
        dto.setStatus(bid.getStatus());
        dto.setRespondedAt(bid.getRespondedAt());
        dto.setCreatedAt(bid.getCreatedAt());
        dto.setUpdatedAt(bid.getUpdatedAt());
        return dto;
    }

    // Expose both id and _id, as well as provider/providerId, farmer/farmerId, requirement/requirementId
    @JsonProperty("id")
    public Long getRawId() { return id; }

    @JsonProperty("provider")
    public UserDto getProviderObj() { return provider; }

    @JsonProperty("farmer")
    public UserDto getFarmerObj() { return farmer; }

    @JsonProperty("requirement")
    public HarvestRequirementResponse getRequirementObj() { return requirement; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserDto getProvider() { return provider; }
    public void setProvider(UserDto provider) { this.provider = provider; }

    public UserDto getFarmer() { return farmer; }
    public void setFarmer(UserDto farmer) { this.farmer = farmer; }

    public HarvestRequirementResponse getRequirement() { return requirement; }
    public void setRequirement(HarvestRequirementResponse requirement) { this.requirement = requirement; }

    public Long getServiceId() { return serviceId; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getPriceType() { return priceType; }
    public void setPriceType(String priceType) { this.priceType = priceType; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getRespondedAt() { return respondedAt; }
    public void setRespondedAt(LocalDateTime respondedAt) { this.respondedAt = respondedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
