package com.krishiconnect.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "harvest_requirements", indexes = {
    @Index(name = "idx_req_status_created", columnList = "status, created_at"),
    @Index(name = "idx_req_crop_state", columnList = "cropType, state")
})
public class HarvestRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    private User farmer;

    @Column(nullable = false, length = 50)
    private String vehicleType;

    @Column(nullable = false)
    private Integer manpower = 0;

    @Column(nullable = false, length = 100)
    private String cropType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantityAmount;

    @Column(nullable = false, length = 20)
    private String quantityUnit = "Quintal";

    @Column(nullable = false)
    private Integer durationValue = 1;

    @Column(nullable = false, length = 20)
    private String durationUnit = "Days";

    @Column(nullable = false)
    private String address;

    private String district;
    private String state;
    private String pincode;

    @Column(nullable = false, length = 20)
    private String urgency = "normal"; // "normal", "urgent", "immediate"

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false, length = 20)
    private String status = "open"; // "open", "in-progress", "completed", "cancelled"

    private Long acceptedBidId;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.manpower == null) this.manpower = 0;
        if (this.quantityUnit == null) this.quantityUnit = "Quintal";
        if (this.durationValue == null) this.durationValue = 1;
        if (this.durationUnit == null) this.durationUnit = "Days";
        if (this.urgency == null) this.urgency = "normal";
        if (this.status == null) this.status = "open";
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public HarvestRequirement() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getFarmer() { return farmer; }
    public void setFarmer(User farmer) { this.farmer = farmer; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public Integer getManpower() { return manpower; }
    public void setManpower(Integer manpower) { this.manpower = manpower; }

    public String getCropType() { return cropType; }
    public void setCropType(String cropType) { this.cropType = cropType; }

    public BigDecimal getQuantityAmount() { return quantityAmount; }
    public void setQuantityAmount(BigDecimal quantityAmount) { this.quantityAmount = quantityAmount; }

    public String getQuantityUnit() { return quantityUnit; }
    public void setQuantityUnit(String quantityUnit) { this.quantityUnit = quantityUnit; }

    public Integer getDurationValue() { return durationValue; }
    public void setDurationValue(Integer durationValue) { this.durationValue = durationValue; }

    public String getDurationUnit() { return durationUnit; }
    public void setDurationUnit(String durationUnit) { this.durationUnit = durationUnit; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

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
