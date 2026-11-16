package com.krishiconnect.controller;

import com.krishiconnect.dto.PaymentOrderRequest;
import com.krishiconnect.dto.PaymentOrderResponse;
import com.krishiconnect.dto.PaymentVerificationRequest;
import com.krishiconnect.security.UserPrincipal;
import com.krishiconnect.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<Map<String, Object>> createPaymentOrder(@Valid @RequestBody PaymentOrderRequest req) {
        PaymentOrderResponse order = paymentService.createPaymentOrder(req);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("order", order);
        res.put("data", order);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyPayment(@AuthenticationPrincipal UserPrincipal currentUser,
                                                             @Valid @RequestBody PaymentVerificationRequest req) {
        paymentService.processPaymentSuccess(currentUser != null ? currentUser.getId() : null, req);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Payment verified and recorded successfully");
        return ResponseEntity.ok(res);
    }
}
