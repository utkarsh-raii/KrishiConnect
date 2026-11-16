package com.krishiconnect.service;

import com.krishiconnect.dto.PaymentOrderRequest;
import com.krishiconnect.dto.PaymentOrderResponse;
import com.krishiconnect.dto.PaymentVerificationRequest;

public interface PaymentService {

    PaymentOrderResponse createPaymentOrder(PaymentOrderRequest req);

    boolean verifyPaymentSignature(PaymentVerificationRequest req);

    void processPaymentSuccess(Long userId, PaymentVerificationRequest req);
}
