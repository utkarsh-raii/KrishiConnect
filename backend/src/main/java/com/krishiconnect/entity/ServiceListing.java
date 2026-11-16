package com.krishiconnect.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_listings", indexes = {
    @Index(name = "idx_service_active", columnList = "isActive, isAvailable"),
    @Index(name = "idx_service_type_price", columnList = "serviceType, pricePerDay")
})
public class ServiceListing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private User provider;

    private String vehicleType;
    private Integer vehicleCount = 1;
    private String vehicleCapacity;
    private String vehicleRegistrationNo;

    @Column(columnDefinition = "TEXT")
    private String vehicleDescription;

    private Integer manpowerAvailable = 0;

    @Column(columnDefinition = "TEXT")
    private String manpowerSkills;

    @Column(columnDefinition = "TEXT")
    private String manpowerDescription;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerDay;

    @Column(nullable = false)
    private Boolean isAvailable = true;

    private LocalDate availableFrom;
    private LocalDate availableTo;

    @Column(columnDefinition = "TEXT")
    private String availabilityNotes;

    @Column(columnDefinition = "TEXT")
    private String serviceStates;

    @Column(columnDefinition = "TEXT")
    private String serviceDistricts;

    private Integer serviceRadius = 50;

    @Column(nullable = false, length = 50)
    private String serviceType; // "transport", "manpower", "harvester", "cold-storage", "both"

    @Column(precision = 3, scale = 2)
    private BigDecimal rating = BigDecimal.ZERO;

    private Integer reviewCount = 0;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.vehicleCount == null) this.vehicleCount = 1;
        if (this.manpowerAvailable == null) this.manpowerAvailable = 0;
        if (this.isAvailable == null) this.isAvailable = true;
        if (this.serviceRadius == null) this.serviceRadius = 50;
        if (this.rating == null) this.rating = BigDecimal.ZERO;
        if (this.reviewCount == null) this.reviewCount = 0;
        if (this.isActive == null) this.isActive = true;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public ServiceListing() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getProvider() { return provider; }
    public void setProvider(User provider) { this.provider = provider; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public Integer getVehicleCount() { return vehicleCount; }
    public void setVehicleCount(Integer vehicleCount) { this.vehicleCount = vehicleCount; }

    public String getVehicleCapacity() { return vehicleCapacity; }
    public void setVehicleCapacity(String vehicleCapacity) { this.vehicleCapacity = vehicleCapacity; }

    public String getVehicleRegistrationNo() { return vehicleRegistrationNo; }
    public void setVehicleRegistrationNo(String vehicleRegistrationNo) { this.vehicleRegistrationNo = vehicleRegistrationNo; }

    public String getVehicleDescription() { return vehicleDescription; }
    public void setVehicleDescription(String vehicleDescription) { this.vehicleDescription = vehicleDescription; }

    public Integer getManpowerAvailable() { return manpowerAvailable; }
    public void setManpowerAvailable(Integer manpowerAvailable) { this.manpowerAvailable = manpowerAvailable; }

    public String getManpowerSkills() { return manpowerSkills; }
    public void setManpowerSkills(String manpowerSkills) { this.manpowerSkills = manpowerSkills; }

    public String getManpowerDescription() { return manpowerDescription; }
    public void setManpowerDescription(String manpowerDescription) { this.manpowerDescription = manpowerDescription; }

    public BigDecimal getPricePerDay() { return pricePerDay; }
    public void setPricePerDay(BigDecimal pricePerDay) { this.pricePerDay = pricePerDay; }

    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }

    public LocalDate getAvailableFrom() { return availableFrom; }
    public void setAvailableFrom(LocalDate availableFrom) { this.availableFrom = availableFrom; }

    public LocalDate getAvailableTo() { return availableTo; }
    public void setAvailableTo(LocalDate availableTo) { this.availableTo = availableTo; }

    public String getAvailabilityNotes() { return availabilityNotes; }
    public void setAvailabilityNotes(String availabilityNotes) { this.availabilityNotes = availabilityNotes; }

    public String getServiceStates() { return serviceStates; }
    public void setServiceStates(String serviceStates) { this.serviceStates = serviceStates; }

    public String getServiceDistricts() { return serviceDistricts; }
    public void setServiceDistricts(String serviceDistricts) { this.serviceDistricts = serviceDistricts; }

    public Integer getServiceRadius() { return serviceRadius; }
    public void setServiceRadius(Integer serviceRadius) { this.serviceRadius = serviceRadius; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }

    public Integer getReviewCount() { return reviewCount; }
    public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
