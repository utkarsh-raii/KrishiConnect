package com.krishiconnect.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "crop_offers", indexes = {
    @Index(name = "idx_offer_status_created", columnList = "status, created_at")
})
public class CropOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    private User farmer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_id", nullable = false)
    private CropListing listing;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal offerPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantityAmount;

    @Column(nullable = false, length = 20)
    private String quantityUnit = "Quintal";

    @Column(length = 500)
    private String message;

    @Column(nullable = false, length = 20)
    private String status = "pending"; // "pending", "accepted", "rejected", "counter", "withdrawn", "completed"

    @Column(precision = 10, scale = 2)
    private BigDecimal counterPrice;

    @Column(length = 500)
    private String counterMessage;

    private LocalDateTime respondedAt;
    private LocalDateTime expiresAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) this.status = "pending";
        if (this.quantityUnit == null) this.quantityUnit = "Quintal";
        if (this.expiresAt == null) this.expiresAt = LocalDateTime.now().plusDays(3);
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        if (("accepted".equalsIgnoreCase(this.status) || "rejected".equalsIgnoreCase(this.status) || "counter".equalsIgnoreCase(this.status))
                && this.respondedAt == null) {
            this.respondedAt = LocalDateTime.now();
        }
    }

    public CropOffer() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getBuyer() { return buyer; }
    public void setBuyer(User buyer) { this.buyer = buyer; }

    public User getFarmer() { return farmer; }
    public void setFarmer(User farmer) { this.farmer = farmer; }

    public CropListing getListing() { return listing; }
    public void setListing(CropListing listing) { this.listing = listing; }

    public BigDecimal getOfferPrice() { return offerPrice; }
    public void setOfferPrice(BigDecimal offerPrice) { this.offerPrice = offerPrice; }

    public BigDecimal getQuantityAmount() { return quantityAmount; }
    public void setQuantityAmount(BigDecimal quantityAmount) { this.quantityAmount = quantityAmount; }

    public String getQuantityUnit() { return quantityUnit; }
    public void setQuantityUnit(String quantityUnit) { this.quantityUnit = quantityUnit; }

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
