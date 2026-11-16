package com.krishiconnect.controller;

import com.krishiconnect.dto.CreateOrderRequest;
import com.krishiconnect.dto.OrderResponseDto;
import com.krishiconnect.security.UserPrincipal;
import com.krishiconnect.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(value = {"", "/create"})
    public ResponseEntity<Map<String, Object>> createOrder(@AuthenticationPrincipal UserPrincipal currentUser,
                                                           @Valid @RequestBody CreateOrderRequest req) {
        OrderResponseDto order = orderService.createOrder(currentUser.getId(), req);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Order placed successfully");
        res.put("order", order);
        res.put("data", order);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/buyer")
    public ResponseEntity<Map<String, Object>> getBuyerOrders(@AuthenticationPrincipal UserPrincipal currentUser) {
        List<OrderResponseDto> orders = orderService.getBuyerOrders(currentUser.getId());
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("count", orders.size());
        res.put("orders", orders);
        res.put("data", orders);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/farmer")
    public ResponseEntity<Map<String, Object>> getFarmerOrders(@AuthenticationPrincipal UserPrincipal currentUser) {
        List<OrderResponseDto> orders = orderService.getFarmerOrders(currentUser.getId());
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("count", orders.size());
        res.put("orders", orders);
        res.put("data", orders);
        return ResponseEntity.ok(res);
    }

    @RequestMapping(value = {"/{id}/status", "/status/{id}"}, method = {RequestMethod.PATCH, RequestMethod.PUT})
    public ResponseEntity<Map<String, Object>> updateOrderStatus(@AuthenticationPrincipal UserPrincipal currentUser,
                                                                 @PathVariable Long id,
                                                                 @RequestBody Map<String, String> body) {
        String status = body.get("status");
        OrderResponseDto order = orderService.updateOrderStatus(currentUser.getId(), id, status);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Order status updated successfully");
        res.put("order", order);
        res.put("data", order);
        return ResponseEntity.ok(res);
    }

    // Buyer cancels order
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Map<String, Object>> cancelOrder(@AuthenticationPrincipal UserPrincipal currentUser,
                                                          @PathVariable Long id) {
        OrderResponseDto order = orderService.buyerCancelOrder(currentUser.getId(), id);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Order cancelled successfully");
        res.put("order", order);
        res.put("data", order);
        return ResponseEntity.ok(res);
    }

    // Buyer confirms delivery
    @PatchMapping("/{id}/confirm-delivery")
    public ResponseEntity<Map<String, Object>> confirmDelivery(@AuthenticationPrincipal UserPrincipal currentUser,
                                                                @PathVariable Long id) {
        OrderResponseDto order = orderService.buyerConfirmDelivery(currentUser.getId(), id);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Delivery confirmed successfully");
        res.put("order", order);
        res.put("data", order);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getOrderById(@AuthenticationPrincipal UserPrincipal currentUser,
                                                            @PathVariable Long id) {
        OrderResponseDto order = orderService.getOrderById(currentUser.getId(), id);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("order", order);
        res.put("data", order);
        return ResponseEntity.ok(res);
    }
}
