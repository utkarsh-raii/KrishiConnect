package com.krishiconnect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CropListingRequest {

    @NotBlank(message = "Crop name is required")
    private String cropName;

    private String category = "Other";

    // Supports both nested and flat fields
    private QuantityDto quantity;
    private BigDecimal quantityAmount;
    private String quantityUnit;

    private PriceDto price;
    private BigDecimal priceAmount;
    private String priceUnit;
    private Boolean priceNegotiable;

    private LocationDto location;
    private String locationAddress;
    private String address;
    private String district;
    private String state;
    private String pincode;

    private LocalDate harvestDate;
    private String harvestSeason;
    private String harvestGrade;

    private String contactPhone;
    private String description;
    private String image;
    private String status;

    public CropListingRequest() {}

    // Resolvers to handle either nested or flat input
    public BigDecimal resolveQuantityAmount() {
        if (quantity != null && quantity.getAmount() != null) return quantity.getAmount();
        return quantityAmount != null ? quantityAmount : BigDecimal.ONE;
    }

    public String resolveQuantityUnit() {
        if (quantity != null && quantity.getUnit() != null) return quantity.getUnit();
        return quantityUnit != null ? quantityUnit : "Quintal";
    }

    public BigDecimal resolvePriceAmount() {
        if (price != null && price.getAmount() != null) return price.getAmount();
        return priceAmount != null ? priceAmount : BigDecimal.ZERO;
    }

    public String resolvePriceUnit() {
        if (price != null && price.getUnit() != null) return price.getUnit();
        return priceUnit != null ? priceUnit : "per Quintal";
    }

    public Boolean resolvePriceNegotiable() {
        if (price != null && price.getNegotiable() != null) return price.getNegotiable();
        return priceNegotiable != null ? priceNegotiable : true;
    }

    public String resolveAddress() {
        if (location != null && location.getAddress() != null) return location.getAddress();
        if (locationAddress != null) return locationAddress;
        return address != null ? address : "Not specified";
    }

    public String resolveDistrict() {
        if (location != null && location.getDistrict() != null) return location.getDistrict();
        return district;
    }

    public String resolveState() {
        if (location != null && location.getState() != null) return location.getState();
        return state;
    }

    public String resolvePincode() {
        if (location != null && location.getPincode() != null) return location.getPincode();
        return pincode;
    }

    // Getters and Setters
    public String getCropName() { return cropName; }
    public void setCropName(String cropName) { this.cropName = cropName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public QuantityDto getQuantity() { return quantity; }
    public void setQuantity(QuantityDto quantity) { this.quantity = quantity; }

    public BigDecimal getQuantityAmount() { return quantityAmount; }
    public void setQuantityAmount(BigDecimal quantityAmount) { this.quantityAmount = quantityAmount; }

    public String getQuantityUnit() { return quantityUnit; }
    public void setQuantityUnit(String quantityUnit) { this.quantityUnit = quantityUnit; }

    public PriceDto getPrice() { return price; }
    public void setPrice(PriceDto price) { this.price = price; }

    public BigDecimal getPriceAmount() { return priceAmount; }
    public void setPriceAmount(BigDecimal priceAmount) { this.priceAmount = priceAmount; }

    public String getPriceUnit() { return priceUnit; }
    public void setPriceUnit(String priceUnit) { this.priceUnit = priceUnit; }

    public Boolean getPriceNegotiable() { return priceNegotiable; }
    public void setPriceNegotiable(Boolean priceNegotiable) { this.priceNegotiable = priceNegotiable; }

    public LocationDto getLocation() { return location; }
    public void setLocation(LocationDto location) { this.location = location; }

    public String getLocationAddress() { return locationAddress; }
    public void setLocationAddress(String locationAddress) { this.locationAddress = locationAddress; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public LocalDate getHarvestDate() { return harvestDate; }
    public void setHarvestDate(LocalDate harvestDate) { this.harvestDate = harvestDate; }

    public String getHarvestSeason() { return harvestSeason; }
    public void setHarvestSeason(String harvestSeason) { this.harvestSeason = harvestSeason; }

    public String getHarvestGrade() { return harvestGrade; }
    public void setHarvestGrade(String harvestGrade) { this.harvestGrade = harvestGrade; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
