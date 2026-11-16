package com.krishiconnect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krishiconnect.entity.TreeRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TreeRequestDto {

    @JsonProperty("_id")
    private Long id;

    private TreeDto tree;
    private UserDto user;
    private Integer quantity;
    private BigDecimal totalPrice;
    private String fullName;
    private String phone;
    private String shippingAddress;
    private String status;
    private String paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TreeRequestDto() {}

    public static TreeRequestDto fromEntity(TreeRequest tr) {
        if (tr == null) return null;
        TreeRequestDto dto = new TreeRequestDto();
        dto.setId(tr.getId());
        dto.setTree(TreeDto.fromEntity(tr.getTree()));
        dto.setUser(UserDto.fromEntity(tr.getUser()));
        dto.setQuantity(tr.getQuantity());
        dto.setTotalPrice(tr.getTotalPrice());
        dto.setFullName(tr.getFullName());
        dto.setPhone(tr.getPhone());
        dto.setShippingAddress(tr.getShippingAddress());
        dto.setStatus(tr.getStatus());
        dto.setPaymentStatus(tr.getPaymentStatus());
        dto.setCreatedAt(tr.getCreatedAt());
        dto.setUpdatedAt(tr.getUpdatedAt());
        return dto;
    }

    // Expose both id and _id
    @JsonProperty("id")
    public Long getRawId() { return id; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TreeDto getTree() { return tree; }
    public void setTree(TreeDto tree) { this.tree = tree; }

    public UserDto getUser() { return user; }
    public void setUser(UserDto user) { this.user = user; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
