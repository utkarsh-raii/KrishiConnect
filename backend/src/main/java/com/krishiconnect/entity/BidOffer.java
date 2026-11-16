package com.krishiconnect.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bid_offers", indexes = {
    @Index(name = "idx_bid_status_created", columnList = "status, created_at")
}, uniqueConstraints = {
    @UniqueConstraint(name = "uk_provider_requirement", columnNames = {"provider_id", "requirement_id"})
})
public class BidOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private User provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    private User farmer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requirement_id", nullable = false)
    private HarvestRequirement requirement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private ServiceListing service;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, length = 30)
    private String priceType = "per-day"; // "per-day", "fixed", "per-quintal"

    @Column(length = 500)
    private String message;

    @Column(nullable = false, length = 20)
    private String status = "pending"; // "pending", "accepted", "rejected", "withdrawn", "completed"

    private LocalDateTime respondedAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.priceType == null) this.priceType = "per-day";
        if (this.status == null) this.status = "pending";
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        if (("accepted".equalsIgnoreCase(this.status) || "rejected".equalsIgnoreCase(this.status)) && this.respondedAt == null) {
            this.respondedAt = LocalDateTime.now();
        }
    }

    public BidOffer() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getProvider() { return provider; }
    public void setProvider(User provider) { this.provider = provider; }

    public User getFarmer() { return farmer; }
    public void setFarmer(User farmer) { this.farmer = farmer; }

    public HarvestRequirement getRequirement() { return requirement; }
    public void setRequirement(HarvestRequirement requirement) { this.requirement = requirement; }

    public ServiceListing getService() { return service; }
    public void setService(ServiceListing service) { this.service = service; }

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
