package com.krishiconnect.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchases", indexes = {
    @Index(name = "idx_purchase_payment_status", columnList = "paymentStatus, created_at"),
    @Index(name = "idx_purchase_buyer", columnList = "buyer_id"),
    @Index(name = "idx_purchase_farmer", columnList = "farmer_id")
})
public class Purchase {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_id")
    private CropOffer offer;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantityAmount;

    @Column(nullable = false, length = 20)
    private String quantityUnit = "Quintal";

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal agreedPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    private String receiptFileName;
    private String receiptFilePath;
    private LocalDateTime receiptUploadedAt;

    @Column(nullable = false, length = 30)
    private String paymentStatus = "pending"; // "pending", "receipt-uploaded", "verified", "disputed", "completed", "refunded"

    @Column(length = 30)
    private String paymentMethod; // "UPI", "NEFT", "RTGS", "Cheque", "Cash", "RAZORPAY", "Other"

    private String transactionId;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(columnDefinition = "TEXT")
    private String disputeReason;

    private LocalDateTime completedAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.paymentStatus == null) this.paymentStatus = "pending";
        if (this.quantityUnit == null) this.quantityUnit = "Quintal";
        if (this.totalAmount == null && this.agreedPrice != null && this.quantityAmount != null) {
            this.totalAmount = this.agreedPrice.multiply(this.quantityAmount);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        if ("completed".equalsIgnoreCase(this.paymentStatus) && this.completedAt == null) {
            this.completedAt = LocalDateTime.now();
        }
    }

    public Purchase() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getBuyer() { return buyer; }
    public void setBuyer(User buyer) { this.buyer = buyer; }

    public User getFarmer() { return farmer; }
    public void setFarmer(User farmer) { this.farmer = farmer; }

    public CropListing getListing() { return listing; }
    public void setListing(CropListing listing) { this.listing = listing; }

    public CropOffer getOffer() { return offer; }
    public void setOffer(CropOffer offer) { this.offer = offer; }

    public BigDecimal getQuantityAmount() { return quantityAmount; }
    public void setQuantityAmount(BigDecimal quantityAmount) { this.quantityAmount = quantityAmount; }

    public String getQuantityUnit() { return quantityUnit; }
    public void setQuantityUnit(String quantityUnit) { this.quantityUnit = quantityUnit; }

    public BigDecimal getAgreedPrice() { return agreedPrice; }
    public void setAgreedPrice(BigDecimal agreedPrice) { this.agreedPrice = agreedPrice; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getReceiptFileName() { return receiptFileName; }
    public void setReceiptFileName(String receiptFileName) { this.receiptFileName = receiptFileName; }

    public String getReceiptFilePath() { return receiptFilePath; }
    public void setReceiptFilePath(String receiptFilePath) { this.receiptFilePath = receiptFilePath; }

    public LocalDateTime getReceiptUploadedAt() { return receiptUploadedAt; }
    public void setReceiptUploadedAt(LocalDateTime receiptUploadedAt) { this.receiptUploadedAt = receiptUploadedAt; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getDisputeReason() { return disputeReason; }
    public void setDisputeReason(String disputeReason) { this.disputeReason = disputeReason; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
