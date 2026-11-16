package com.krishiconnect.dto;

import java.math.BigDecimal;

public class PriceDto {
    private BigDecimal amount;
    private String unit = "per Quintal";
    private Boolean negotiable = true;

    public PriceDto() {}

    public PriceDto(BigDecimal amount, String unit, Boolean negotiable) {
        this.amount = amount;
        this.unit = unit;
        this.negotiable = negotiable;
    }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public Boolean getNegotiable() { return negotiable; }
    public void setNegotiable(Boolean negotiable) { this.negotiable = negotiable; }
}
