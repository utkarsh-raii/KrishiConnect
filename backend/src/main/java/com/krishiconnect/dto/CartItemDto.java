package com.krishiconnect.dto;

import com.krishiconnect.entity.CartItem;

import java.math.BigDecimal;

public class CartItemDto {
    private Long id;
    private CropListingResponse product;
    private BigDecimal quantity;

    public CartItemDto() {}

    public static CartItemDto fromEntity(CartItem item) {
        if (item == null) return null;
        CartItemDto dto = new CartItemDto();
        dto.setId(item.getId());
        dto.setProduct(CropListingResponse.fromEntity(item.getCropListing()));
        dto.setQuantity(item.getQuantity());
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public CropListingResponse getProduct() { return product; }
    public void setProduct(CropListingResponse product) { this.product = product; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
}
