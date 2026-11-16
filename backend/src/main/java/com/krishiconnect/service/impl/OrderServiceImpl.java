package com.krishiconnect.service.impl;

import com.krishiconnect.dto.CreateOrderRequest;
import com.krishiconnect.dto.OrderItemRequest;
import com.krishiconnect.dto.OrderResponseDto;
import com.krishiconnect.entity.CropListing;
import com.krishiconnect.entity.Order;
import com.krishiconnect.entity.OrderItem;
import com.krishiconnect.entity.User;
import com.krishiconnect.exception.BadRequestException;
import com.krishiconnect.exception.ForbiddenException;
import com.krishiconnect.exception.ResourceNotFoundException;
import com.krishiconnect.repository.CropListingRepository;
import com.krishiconnect.repository.OrderRepository;
import com.krishiconnect.repository.UserRepository;
import com.krishiconnect.service.NotificationService;
import com.krishiconnect.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Set<String> VALID_ORDER_STATUSES = Set.of(
            "Pending", "Confirmed", "Accepted", "Packed", "Shipped", "Delivered", "Cancelled"
    );

    private final OrderRepository orderRepository;
    private final CropListingRepository cropListingRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public OrderServiceImpl(OrderRepository orderRepository,
                            CropListingRepository cropListingRepository,
                            UserRepository userRepository,
                            NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.cropListingRepository = cropListingRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional
    public OrderResponseDto createOrder(Long buyerId, CreateOrderRequest req) {
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found"));
        User farmer = userRepository.findById(req.getFarmer())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found"));
        Order order = new Order();
        order.setBuyer(buyer);
        order.setFarmer(farmer);
        order.setShippingAddress(req.getShippingAddress());
        order.setPaymentMethod(req.getPaymentMethod() != null ? req.getPaymentMethod() : "COD");
        order.setPaymentStatus("Pending");
        order.setOrderStatus("Pending");
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> items = new ArrayList<>();
        for (OrderItemRequest itemReq : req.getItems()) {
            if (itemReq.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BadRequestException("Quantity must be greater than zero");
            }
            CropListing crop = cropListingRepository.findById(itemReq.getProduct())
                    .orElseThrow(() -> new ResourceNotFoundException("Crop product not found: " + itemReq.getProduct()));
            BigDecimal unitPrice = itemReq.getPrice() != null ? itemReq.getPrice() : crop.getPriceAmount();
            BigDecimal lineTotal = unitPrice.multiply(itemReq.getQuantity());
            totalAmount = totalAmount.add(lineTotal);
            OrderItem orderItem = new OrderItem(crop, itemReq.getQuantity(), unitPrice);
            orderItem.setOrder(order);
            items.add(orderItem);
        }
        order.setTotalAmount(totalAmount);
        order.setItems(items);
        order = orderRepository.save(order);
        notificationService.createNotification(farmer.getId(), "New Order Received",
                buyer.getName() + " has placed a new order for ₹" + totalAmount);
        return OrderResponseDto.fromEntity(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getBuyerOrders(Long buyerId) {
        return orderRepository.findByBuyerIdOrderByCreatedAtDesc(buyerId)
                .stream()
                .map(OrderResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getBuyerOrders(Long buyerId, Pageable pageable) {
        return orderRepository.findByBuyerId(buyerId, pageable)
                .map(OrderResponseDto::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getFarmerOrders(Long farmerId) {
        return orderRepository.findByFarmerIdOrderByCreatedAtDesc(farmerId)
                .stream()
                .map(OrderResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getFarmerOrders(Long farmerId, Pageable pageable) {
        return orderRepository.findByFarmerId(farmerId, pageable)
                .map(OrderResponseDto::fromEntity);
    }

    @Override
    @Transactional
    public OrderResponseDto updateOrderStatus(Long farmerId, Long orderId, String status) {
        Order order = orderRepository.findByIdForUpdate(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (!order.getFarmer().getId().equals(farmerId)) {
            throw new ForbiddenException("You cannot update this order");
        }
        String current = order.getOrderStatus();
        String target = status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase();
        if ("Pending".equalsIgnoreCase(current) && "Rejected".equalsIgnoreCase(target)) {
            // allowed rejection
        } else if ("Pending".equalsIgnoreCase(current) && "Accepted".equalsIgnoreCase(target)) {
            // allowed accept
        } else if ("Accepted".equalsIgnoreCase(current) && "Packed".equalsIgnoreCase(target)) {
            // allowed pack
        } else if ("Packed".equalsIgnoreCase(current) && "Shipped".equalsIgnoreCase(target)) {
            // allowed ship
        } else {
            throw new BadRequestException("Invalid status transition from " + current + " to " + target);
        }
        if ("Cancelled".equalsIgnoreCase(current) || "Delivered".equalsIgnoreCase(current)) {
            throw new BadRequestException("Order is finalized and can no longer be updated");
        }
        order.setOrderStatus(target);
        order = orderRepository.save(order);
        notificationService.createNotification(order.getBuyer().getId(), "Order Status Updated",
                "Your order #" + order.getId() + " is now " + target);
        return OrderResponseDto.fromEntity(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (!order.getBuyer().getId().equals(userId) && !order.getFarmer().getId().equals(userId)) {
            throw new ForbiddenException("Unauthorized to view this order");
        }
        return OrderResponseDto.fromEntity(order);
    }

    @Override
    @Transactional
    public OrderResponseDto buyerCancelOrder(Long buyerId, Long orderId) {
        Order order = orderRepository.findByIdForUpdate(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (!order.getBuyer().getId().equals(buyerId)) {
            throw new ForbiddenException("You cannot cancel this order");
        }
        if ("Delivered".equalsIgnoreCase(order.getOrderStatus()) || "Cancelled".equalsIgnoreCase(order.getOrderStatus())) {
            throw new BadRequestException("Order is already finalized and cannot be cancelled");
        }
        if (!"Pending".equalsIgnoreCase(order.getPaymentStatus())) {
            throw new BadRequestException("Cannot cancel an order that has been paid. Contact support for a refund.");
        }
        order.setOrderStatus("Cancelled");
        order = orderRepository.save(order);
        notificationService.createNotification(order.getFarmer().getId(), "Order Cancelled",
                "Buyer " + order.getBuyer().getName() + " cancelled order #" + order.getId());
        return OrderResponseDto.fromEntity(order);
    }

    @Override
    @Transactional
    public OrderResponseDto buyerConfirmDelivery(Long buyerId, Long orderId) {
        Order order = orderRepository.findByIdForUpdate(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (!order.getBuyer().getId().equals(buyerId)) {
            throw new ForbiddenException("You cannot confirm delivery for this order");
        }
        if (!"Shipped".equalsIgnoreCase(order.getOrderStatus())) {
            throw new BadRequestException("Order must be shipped before confirming delivery");
        }
        order.setOrderStatus("Delivered");
        order = orderRepository.save(order);
        notificationService.createNotification(order.getFarmer().getId(), "Delivery Confirmed",
                "Buyer " + order.getBuyer().getName() + " confirmed delivery of order #" + order.getId());
        return OrderResponseDto.fromEntity(order);
    }
}
