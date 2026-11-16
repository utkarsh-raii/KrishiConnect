package com.krishiconnect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krishiconnect.entity.CropOffer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CropOfferResponseDto {

    @JsonProperty("_id")
    private Long id;

    @JsonProperty("buyerId")
    private UserDto buyer;

    @JsonProperty("farmerId")
    private UserDto farmer;

    @JsonProperty("listingId")
    private CropListingResponse listing;

    private BigDecimal offerPrice;
    private QuantityDto quantity;
    private String message;
    private String status;
    private BigDecimal counterPrice;
    private String counterMessage;
    private LocalDateTime respondedAt;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CropOfferResponseDto() {}

    public static CropOfferResponseDto fromEntity(CropOffer offer) {
        if (offer == null) return null;
        CropOfferResponseDto dto = new CropOfferResponseDto();
        dto.setId(offer.getId());
        dto.setBuyer(UserDto.fromEntity(offer.getBuyer()));
        dto.setFarmer(UserDto.fromEntity(offer.getFarmer()));
        dto.setListing(CropListingResponse.fromEntity(offer.getListing()));
        dto.setOfferPrice(offer.getOfferPrice());
        dto.setQuantity(new QuantityDto(offer.getQuantityAmount(), offer.getQuantityUnit()));
        dto.setMessage(offer.getMessage());
        dto.setStatus(offer.getStatus());
        dto.setCounterPrice(offer.getCounterPrice());
        dto.setCounterMessage(offer.getCounterMessage());
        dto.setRespondedAt(offer.getRespondedAt());
        dto.setExpiresAt(offer.getExpiresAt());
        dto.setCreatedAt(offer.getCreatedAt());
        dto.setUpdatedAt(offer.getUpdatedAt());
        return dto;
    }

    // Expose both id and _id, as well as buyer/buyerId, farmer/farmerId, listing/listingId
    @JsonProperty("id")
    public Long getRawId() { return id; }

    @JsonProperty("buyer")
    public UserDto getBuyerObj() { return buyer; }

    @JsonProperty("farmer")
    public UserDto getFarmerObj() { return farmer; }

    @JsonProperty("listing")
    public CropListingResponse getListingObj() { return listing; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserDto getBuyer() { return buyer; }
    public void setBuyer(UserDto buyer) { this.buyer = buyer; }

    public UserDto getFarmer() { return farmer; }
    public void setFarmer(UserDto farmer) { this.farmer = farmer; }

    public CropListingResponse getListing() { return listing; }
    public void setListing(CropListingResponse listing) { this.listing = listing; }

    public BigDecimal getOfferPrice() { return offerPrice; }
    public void setOfferPrice(BigDecimal offerPrice) { this.offerPrice = offerPrice; }

    public QuantityDto getQuantity() { return quantity; }
    public void setQuantity(QuantityDto quantity) { this.quantity = quantity; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getCounterPrice() { return counterPrice; }
    public void setCounterPrice(BigDecimal counterPrice) { this.counterPrice = counterPrice; }

    public String getCounterMessage() { return counterMessage; }
    public void setCounterMessage(String counterMessage) { this.counterMessage = counterMessage; }

    public LocalDateTime getRespondedAt() { return respondedAt; }
    public void setRespondedAt(LocalDateTime respondedAt) { this.respondedAt = respondedAt; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
