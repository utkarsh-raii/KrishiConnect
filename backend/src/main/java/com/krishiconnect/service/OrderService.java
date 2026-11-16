package com.krishiconnect.service;

import com.krishiconnect.dto.CreateOrderRequest;
import com.krishiconnect.dto.OrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    OrderResponseDto createOrder(Long buyerId, CreateOrderRequest req);

    List<OrderResponseDto> getBuyerOrders(Long buyerId);

    Page<OrderResponseDto> getBuyerOrders(Long buyerId, Pageable pageable);

    List<OrderResponseDto> getFarmerOrders(Long farmerId);

    Page<OrderResponseDto> getFarmerOrders(Long farmerId, Pageable pageable);

    /**
     * Farmer updates order status (e.g., Accept, Pack, Ship, Reject).
     */
    OrderResponseDto updateOrderStatus(Long farmerId, Long orderId, String status);

    /**
     * Buyer cancels an order (if not paid or already cancelled).
     */
    OrderResponseDto buyerCancelOrder(Long buyerId, Long orderId);

    /**
     * Buyer confirms delivery after shipment.
     */
    OrderResponseDto buyerConfirmDelivery(Long buyerId, Long orderId);

    OrderResponseDto getOrderById(Long userId, Long orderId);
}
