package com.krishiconnect.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class SubmitBidRequest {

    @NotNull(message = "requirementId is required")
    private Long requirementId;

    private Long serviceId;

    @NotNull(message = "price is required")
    private BigDecimal price;

    private String priceType = "per-day";
    private String message;

    public SubmitBidRequest() {}

    public Long getRequirementId() { return requirementId; }
    public void setRequirementId(Long requirementId) { this.requirementId = requirementId; }

    public Long getServiceId() { return serviceId; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getPriceType() { return priceType; }
    public void setPriceType(String priceType) { this.priceType = priceType; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
