package com.krishiconnect.service;

import com.krishiconnect.dto.CreateOrderRequest;
import com.krishiconnect.dto.OrderItemRequest;
import com.krishiconnect.dto.OrderResponseDto;
import com.krishiconnect.entity.Order;
import com.krishiconnect.entity.User;
import com.krishiconnect.exception.BadRequestException;
import com.krishiconnect.exception.ForbiddenException;
import com.krishiconnect.repository.OrderRepository;
import com.krishiconnect.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class OrderStatusIntegrationTests {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    private User buyer;
    private User farmer;

    @BeforeEach
    public void setup() {
        buyer = userRepository.findByEmailIgnoreCase("priya@buyer.com")
                .orElseThrow();

        farmer = userRepository.findByEmailIgnoreCase("farmer@demo.com")
                .orElseThrow();
    }

    private OrderResponseDto createSampleOrder() {
        CreateOrderRequest req = new CreateOrderRequest();
        req.setFarmer(farmer.getId());
        req.setShippingAddress("123 Test St");
        req.setPaymentMethod("COD");
        OrderItemRequest item = new OrderItemRequest();
        item.setProduct(1L); // assume a crop listing with id 1 exists in seed data
        item.setQuantity(BigDecimal.ONE);
        item.setPrice(null);
        req.setItems(Collections.singletonList(item));
        return orderService.createOrder(buyer.getId(), req);
    }

    @Test
    public void testValidStatusTransitions() {
        OrderResponseDto dto = createSampleOrder();
        Long orderId = dto.getId();

        // Farmer accepts
        OrderResponseDto updated = orderService.updateOrderStatus(farmer.getId(), orderId, "Accepted");
        assertEquals("Accepted", updated.getOrderStatus());

        // Farmer packs
        updated = orderService.updateOrderStatus(farmer.getId(), orderId, "Packed");
        assertEquals("Packed", updated.getOrderStatus());

        // Farmer ships
        updated = orderService.updateOrderStatus(farmer.getId(), orderId, "Shipped");
        assertEquals("Shipped", updated.getOrderStatus());

        // Buyer confirms delivery
        updated = orderService.buyerConfirmDelivery(buyer.getId(), orderId);
        assertEquals("Delivered", updated.getOrderStatus());
    }

    @Test
    public void testRejectAndCancel() {
        // Reject scenario
        OrderResponseDto dto = createSampleOrder();
        Long orderId = dto.getId();
        OrderResponseDto rejected = orderService.updateOrderStatus(farmer.getId(), orderId, "Rejected");
        assertEquals("Rejected", rejected.getOrderStatus());

        // New order for cancel
        dto = createSampleOrder();
        orderId = dto.getId();
        OrderResponseDto cancelled = orderService.buyerCancelOrder(buyer.getId(), orderId);
        assertEquals("Cancelled", cancelled.getOrderStatus());
    }

    @Test
    public void testInvalidTransitions() {
        OrderResponseDto dto = createSampleOrder();
        Long orderId = dto.getId();
        // Invalid pack before accept
        assertThrows(BadRequestException.class, () ->
                orderService.updateOrderStatus(farmer.getId(), orderId, "Packed"));
        // Buyer trying to cancel after payment marked paid
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setPaymentStatus("Paid");
        orderRepository.save(order);
        assertThrows(BadRequestException.class, () ->
                orderService.buyerCancelOrder(buyer.getId(), orderId));
    }

    @Test
    public void testUnauthorizedAccess() {
        OrderResponseDto dto = createSampleOrder();
        Long orderId = dto.getId();
        // Another user (different buyer) trying to cancel
        User otherBuyer = userRepository.findByEmailIgnoreCase("anil@buyer.com")
                .orElseThrow();
        assertThrows(ForbiddenException.class, () ->
                orderService.buyerCancelOrder(otherBuyer.getId(), orderId));
        // Another farmer trying to update status
        User otherFarmer = userRepository.findByEmailIgnoreCase("suresh@farmer.com")
                .orElseThrow();
        assertThrows(ForbiddenException.class, () ->
                orderService.updateOrderStatus(otherFarmer.getId(), orderId, "Accepted"));
    }
}
