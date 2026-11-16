package com.krishiconnect.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class MakeOfferRequest {

    @NotNull(message = "listingId is required")
    private Long listingId;

    @NotNull(message = "offerPrice is required")
    private BigDecimal offerPrice;

    private QuantityDto quantity;
    private BigDecimal quantityAmount;
    private String quantityUnit;

    private String message;

    public MakeOfferRequest() {}

    public BigDecimal resolveQuantityAmount() {
        if (quantity != null && quantity.getAmount() != null) return quantity.getAmount();
        return quantityAmount != null ? quantityAmount : BigDecimal.ONE;
    }

    public String resolveQuantityUnit() {
        if (quantity != null && quantity.getUnit() != null) return quantity.getUnit();
        return quantityUnit != null ? quantityUnit : "Quintal";
    }

    public Long getListingId() { return listingId; }
    public void setListingId(Long listingId) { this.listingId = listingId; }

    public BigDecimal getOfferPrice() { return offerPrice; }
    public void setOfferPrice(BigDecimal offerPrice) { this.offerPrice = offerPrice; }

    public QuantityDto getQuantity() { return quantity; }
    public void setQuantity(QuantityDto quantity) { this.quantity = quantity; }

    public BigDecimal getQuantityAmount() { return quantityAmount; }
    public void setQuantityAmount(BigDecimal quantityAmount) { this.quantityAmount = quantityAmount; }

    public String getQuantityUnit() { return quantityUnit; }
    public void setQuantityUnit(String quantityUnit) { this.quantityUnit = quantityUnit; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
