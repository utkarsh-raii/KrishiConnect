package com.krishiconnect.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class DirectPurchaseRequest {

    @NotNull(message = "listingId is required")
    private Long listingId;

    private BigDecimal quantityAmount = BigDecimal.ONE;
    private String notes;

    public DirectPurchaseRequest() {}

    public Long getListingId() { return listingId; }
    public void setListingId(Long listingId) { this.listingId = listingId; }

    public BigDecimal getQuantityAmount() { return quantityAmount != null ? quantityAmount : BigDecimal.ONE; }
    public void setQuantityAmount(BigDecimal quantityAmount) { this.quantityAmount = quantityAmount; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
