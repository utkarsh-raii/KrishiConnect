package com.krishiconnect.dto;

import java.math.BigDecimal;

public class PaymentOrderResponse {
    private String id;
    private BigDecimal amount;
    private String currency;
    private String receipt;
    private String status;

    public PaymentOrderResponse() {}

    public PaymentOrderResponse(String id, BigDecimal amount, String currency, String receipt, String status) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.receipt = receipt;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getReceipt() { return receipt; }
    public void setReceipt(String receipt) { this.receipt = receipt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
