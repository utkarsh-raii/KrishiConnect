package com.krishiconnect.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "crop_listings", indexes = {
    @Index(name = "idx_crop_status_created", columnList = "status, created_at"),
    @Index(name = "idx_crop_name", columnList = "cropName"),
    @Index(name = "idx_crop_state_dist", columnList = "state, district")
})
public class CropListing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    private User farmer;

    @Column(nullable = false, length = 150)
    private String cropName;

    @Column(nullable = false, length = 50)
    private String category = "Other";

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantityAmount;

    @Column(nullable = false, length = 20)
    private String quantityUnit = "Quintal";

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAmount;

    @Column(nullable = false, length = 50)
    private String priceUnit = "per Quintal";

    @Column(nullable = false)
    private Boolean priceNegotiable = true;

    private String image;

    @Column(nullable = false)
    private String address;

    private String district;
    private String state;
    private String pincode;

    private LocalDate harvestDate;
    private String harvestSeason;
    private String harvestGrade = "A";

    private String contactPhone;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 20)
    private String status = "active"; // "active", "sold", "expired", "withdrawn"

    private Integer views = 0;
    private Integer offerCount = 0;

    private LocalDateTime expiresAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) this.status = "active";
        if (this.category == null) this.category = "Other";
        if (this.quantityUnit == null) this.quantityUnit = "Quintal";
        if (this.priceUnit == null) this.priceUnit = "per Quintal";
        if (this.priceNegotiable == null) this.priceNegotiable = true;
        if (this.harvestGrade == null) this.harvestGrade = "A";
        if (this.views == null) this.views = 0;
        if (this.offerCount == null) this.offerCount = 0;
        if (this.expiresAt == null) this.expiresAt = LocalDateTime.now().plusDays(30);
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public CropListing() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getFarmer() { return farmer; }
    public void setFarmer(User farmer) { this.farmer = farmer; }

    public String getCropName() { return cropName; }
    public void setCropName(String cropName) { this.cropName = cropName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BigDecimal getQuantityAmount() { return quantityAmount; }
    public void setQuantityAmount(BigDecimal quantityAmount) { this.quantityAmount = quantityAmount; }

    public String getQuantityUnit() { return quantityUnit; }
    public void setQuantityUnit(String quantityUnit) { this.quantityUnit = quantityUnit; }

    public BigDecimal getPriceAmount() { return priceAmount; }
    public void setPriceAmount(BigDecimal priceAmount) { this.priceAmount = priceAmount; }

    public String getPriceUnit() { return priceUnit; }
    public void setPriceUnit(String priceUnit) { this.priceUnit = priceUnit; }

    public Boolean getPriceNegotiable() { return priceNegotiable; }
    public void setPriceNegotiable(Boolean priceNegotiable) { this.priceNegotiable = priceNegotiable; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

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
