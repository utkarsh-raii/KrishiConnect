package com.krishiconnect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class PaymentVerificationRequest {

    @NotBlank(message = "razorpay_order_id is required")
    @JsonProperty("razorpay_order_id")
    private String razorpayOrderId;

    @NotBlank(message = "razorpay_payment_id is required")
    @JsonProperty("razorpay_payment_id")
    private String razorpayPaymentId;

    @NotBlank(message = "razorpay_signature is required")
    @JsonProperty("razorpay_signature")
    private String razorpaySignature;

    private Long purchaseId;
    private Long orderId;

    public PaymentVerificationRequest() {}

    public String getRazorpayOrderId() { return razorpayOrderId; }
    public void setRazorpayOrderId(String razorpayOrderId) { this.razorpayOrderId = razorpayOrderId; }

    public String getRazorpayPaymentId() { return razorpayPaymentId; }
    public void setRazorpayPaymentId(String razorpayPaymentId) { this.razorpayPaymentId = razorpayPaymentId; }

    public String getRazorpaySignature() { return razorpaySignature; }
    public void setRazorpaySignature(String razorpaySignature) { this.razorpaySignature = razorpaySignature; }

    public Long getPurchaseId() { return purchaseId; }
    public void setPurchaseId(Long purchaseId) { this.purchaseId = purchaseId; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
}
