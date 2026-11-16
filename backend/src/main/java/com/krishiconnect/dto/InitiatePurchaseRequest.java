package com.krishiconnect.dto;

import jakarta.validation.constraints.NotNull;

public class InitiatePurchaseRequest {

    @NotNull(message = "offerId is required")
    private Long offerId;

    private String paymentMethod;
    private String transactionId;
    private String notes;

    public InitiatePurchaseRequest() {}

    public Long getOfferId() { return offerId; }
    public void setOfferId(Long offerId) { this.offerId = offerId; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
