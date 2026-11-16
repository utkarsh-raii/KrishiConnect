package com.krishiconnect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krishiconnect.entity.CropListing;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CropListingResponse {

    @JsonProperty("_id")
    private Long id;

    // Both farmerId and farmer provided for frontend compatibility
    @JsonProperty("farmerId")
    private UserDto farmer;

    private String cropName;
    private String category;
    private QuantityDto quantity;
    private PriceDto price;
    private String image;
    private LocationDto location;

    private HarvestDto harvest;
    private String contactPhone;
    private String description;
    private String status;
    private Integer views;
    private Integer offerCount;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static class HarvestDto {
        private LocalDate date;
        private String season;
        private String grade;

        public HarvestDto() {}
        public HarvestDto(LocalDate date, String season, String grade) {
            this.date = date;
            this.season = season;
            this.grade = grade;
        }
        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }
        public String getSeason() { return season; }
        public void setSeason(String season) { this.season = season; }
        public String getGrade() { return grade; }
        public void setGrade(String grade) { this.grade = grade; }
    }

    public CropListingResponse() {}

    public static CropListingResponse fromEntity(CropListing listing) {
        if (listing == null) return null;
        CropListingResponse dto = new CropListingResponse();
        dto.setId(listing.getId());
        dto.setFarmer(UserDto.fromEntity(listing.getFarmer()));
        dto.setCropName(listing.getCropName());
        dto.setCategory(listing.getCategory());
        dto.setQuantity(new QuantityDto(listing.getQuantityAmount(), listing.getQuantityUnit()));
        dto.setPrice(new PriceDto(listing.getPriceAmount(), listing.getPriceUnit(), listing.getPriceNegotiable()));
        dto.setImage(listing.getImage());
        dto.setLocation(new LocationDto(listing.getAddress(), listing.getDistrict(), listing.getState(), listing.getPincode()));
        dto.setHarvest(new HarvestDto(listing.getHarvestDate(), listing.getHarvestSeason(), listing.getHarvestGrade()));
        dto.setContactPhone(listing.getContactPhone());
        dto.setDescription(listing.getDescription());
        dto.setStatus(listing.getStatus());
        dto.setViews(listing.getViews());
        dto.setOfferCount(listing.getOfferCount());
        dto.setExpiresAt(listing.getExpiresAt());
        dto.setCreatedAt(listing.getCreatedAt());
        dto.setUpdatedAt(listing.getUpdatedAt());
        return dto;
    }

    // Expose both id and _id
    @JsonProperty("id")
    public Long getRawId() { return id; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserDto getFarmer() { return farmer; }
    public void setFarmer(UserDto farmer) { this.farmer = farmer; }

    public String getCropName() { return cropName; }
    public void setCropName(String cropName) { this.cropName = cropName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public QuantityDto getQuantity() { return quantity; }
    public void setQuantity(QuantityDto quantity) { this.quantity = quantity; }

    public PriceDto getPrice() { return price; }
    public void setPrice(PriceDto price) { this.price = price; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public LocationDto getLocation() { return location; }
    public void setLocation(LocationDto location) { this.location = location; }

    public HarvestDto getHarvest() { return harvest; }
    public void setHarvest(HarvestDto harvest) { this.harvest = harvest; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getViews() { return views; }
    public void setViews(Integer views) { this.views = views; }

    public Integer getOfferCount() { return offerCount; }
    public void setOfferCount(Integer offerCount) { this.offerCount = offerCount; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
