package com.krishiconnect.dto;

import com.krishiconnect.entity.OrderItem;

import java.math.BigDecimal;

public class OrderItemResponseDto {
    private Long id;
    private CropListingResponse product;
    private BigDecimal quantity;
    private BigDecimal price;

    public OrderItemResponseDto() {}

    public static OrderItemResponseDto fromEntity(OrderItem item) {
        if (item == null) return null;
        OrderItemResponseDto dto = new OrderItemResponseDto();
        dto.setId(item.getId());
        dto.setProduct(CropListingResponse.fromEntity(item.getCropListing()));
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public CropListingResponse getProduct() { return product; }
    public void setProduct(CropListingResponse product) { this.product = product; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
