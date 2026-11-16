package com.krishiconnect.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PaymentOrderRequest {

    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    public PaymentOrderRequest() {}

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
