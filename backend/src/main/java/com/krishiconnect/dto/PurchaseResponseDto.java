package com.krishiconnect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krishiconnect.entity.Purchase;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PurchaseResponseDto {

    @JsonProperty("_id")
    private Long id;

    @JsonProperty("buyerId")
    private UserDto buyer;

    @JsonProperty("farmerId")
    private UserDto farmer;

    @JsonProperty("listingId")
    private CropListingResponse listing;

    private Long offerId;
    private QuantityDto quantity;
    private BigDecimal agreedPrice;
    private BigDecimal totalAmount;

    private ReceiptDto receipt;
    private String paymentStatus;
    private String paymentMethod;
    private String transactionId;
    private String notes;
    private String disputeReason;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static class ReceiptDto {
        private String fileName;
        private String filePath;
        private LocalDateTime uploadedAt;

        public ReceiptDto() {}
        public ReceiptDto(String fileName, String filePath, LocalDateTime uploadedAt) {
            this.fileName = fileName;
            this.filePath = filePath;
            this.uploadedAt = uploadedAt;
        }
        public String getFileName() { return fileName; }
        public void setFileName(String fileName) { this.fileName = fileName; }
        public String getFilePath() { return filePath; }
        public void setFilePath(String filePath) { this.filePath = filePath; }
        public LocalDateTime getUploadedAt() { return uploadedAt; }
        public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
    }

    public PurchaseResponseDto() {}

    public static PurchaseResponseDto fromEntity(Purchase p) {
        if (p == null) return null;
        PurchaseResponseDto dto = new PurchaseResponseDto();
        dto.setId(p.getId());
        dto.setBuyer(UserDto.fromEntity(p.getBuyer()));
        dto.setFarmer(UserDto.fromEntity(p.getFarmer()));
        dto.setListing(CropListingResponse.fromEntity(p.getListing()));
        if (p.getOffer() != null) dto.setOfferId(p.getOffer().getId());
        dto.setQuantity(new QuantityDto(p.getQuantityAmount(), p.getQuantityUnit()));
        dto.setAgreedPrice(p.getAgreedPrice());
        dto.setTotalAmount(p.getTotalAmount());
        if (p.getReceiptFileName() != null || p.getReceiptFilePath() != null) {
            dto.setReceipt(new ReceiptDto(p.getReceiptFileName(), p.getReceiptFilePath(), p.getReceiptUploadedAt()));
        }
        dto.setPaymentStatus(p.getPaymentStatus());
        dto.setPaymentMethod(p.getPaymentMethod());
        dto.setTransactionId(p.getTransactionId());
        dto.setNotes(p.getNotes());
        dto.setDisputeReason(p.getDisputeReason());
        dto.setCompletedAt(p.getCompletedAt());
        dto.setCreatedAt(p.getCreatedAt());
        dto.setUpdatedAt(p.getUpdatedAt());
        return dto;
    }

    // Expose both id and _id, as well as buyer/buyerId, farmer/farmerId, listing/listingId
    @JsonProperty("id")
    public Long getRawId() { return id; }

    @JsonProperty("buyer")
    public UserDto getBuyerObj() { return buyer; }

    @JsonProperty("buyerId")
    public UserDto getBuyerId() { return buyer; }

    @JsonProperty("farmerId")
    public UserDto getFarmerId() { return farmer; }

    @JsonProperty("farmer")
    public UserDto getFarmerObj() { return farmer; }

    @JsonProperty("listing")
    public CropListingResponse getListingObj() { return listing; }

    @JsonProperty("listingId")
    public CropListingResponse getListingId() { return listing; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserDto getBuyer() { return buyer; }
    public void setBuyer(UserDto buyer) { this.buyer = buyer; }

    public UserDto getFarmer() { return farmer; }
    public void setFarmer(UserDto farmer) { this.farmer = farmer; }

    public CropListingResponse getListing() { return listing; }
    public void setListing(CropListingResponse listing) { this.listing = listing; }

    public Long getOfferId() { return offerId; }
    public void setOfferId(Long offerId) { this.offerId = offerId; }

    public QuantityDto getQuantity() { return quantity; }
    public void setQuantity(QuantityDto quantity) { this.quantity = quantity; }

    public BigDecimal getAgreedPrice() { return agreedPrice; }
    public void setAgreedPrice(BigDecimal agreedPrice) { this.agreedPrice = agreedPrice; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public ReceiptDto getReceipt() { return receipt; }
    public void setReceipt(ReceiptDto receipt) { this.receipt = receipt; }

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
