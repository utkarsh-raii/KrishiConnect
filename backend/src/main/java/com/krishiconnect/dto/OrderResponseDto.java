package com.krishiconnect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krishiconnect.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderResponseDto {

    @JsonProperty("_id")
    private Long id;

    private UserDto buyer;
    private UserDto farmer;
    private List<OrderItemResponseDto> items = new ArrayList<>();
    private BigDecimal totalAmount;
    private String shippingAddress;
    private String paymentMethod;
    private String paymentStatus;
    private String orderStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public OrderResponseDto() {}

    public static OrderResponseDto fromEntity(Order order) {
        if (order == null) return null;
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setBuyer(UserDto.fromEntity(order.getBuyer()));
        dto.setFarmer(UserDto.fromEntity(order.getFarmer()));
        if (order.getItems() != null) {
            dto.setItems(order.getItems().stream().map(OrderItemResponseDto::fromEntity).toList());
        }
        dto.setTotalAmount(order.getTotalAmount());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        return dto;
    }

    // Expose both id and _id, as well as buyer/buyerId, farmer/farmerId
    @JsonProperty("id")
    public Long getRawId() { return id; }

    @JsonProperty("buyerId")
    public UserDto getBuyerId() { return buyer; }

    @JsonProperty("farmerId")
    public UserDto getFarmerId() { return farmer; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserDto getBuyer() { return buyer; }
    public void setBuyer(UserDto buyer) { this.buyer = buyer; }

    public UserDto getFarmer() { return farmer; }
    public void setFarmer(UserDto farmer) { this.farmer = farmer; }

    public List<OrderItemResponseDto> getItems() { return items; }
    public void setItems(List<OrderItemResponseDto> items) { this.items = items; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
