package com.krishiconnect.dto;

import java.math.BigDecimal;

public class QuantityDto {
    private BigDecimal amount;
    private String unit = "Quintal";

    public QuantityDto() {}

    public QuantityDto(BigDecimal amount, String unit) {
        this.amount = amount;
        this.unit = unit;
    }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}
