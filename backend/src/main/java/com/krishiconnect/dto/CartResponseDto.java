package com.krishiconnect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krishiconnect.entity.Cart;

import java.util.ArrayList;
import java.util.List;

public class CartResponseDto {

    @JsonProperty("_id")
    private Long id;

    private Long user;
    private List<CartItemDto> items = new ArrayList<>();

    public CartResponseDto() {}

    public static CartResponseDto fromEntity(Cart cart) {
        if (cart == null) return null;
        CartResponseDto dto = new CartResponseDto();
        dto.setId(cart.getId());
        dto.setUser(cart.getUser().getId());
        if (cart.getItems() != null) {
            dto.setItems(cart.getItems().stream().map(CartItemDto::fromEntity).toList());
        }
        return dto;
    }

    // Expose both id and _id
    @JsonProperty("id")
    public Long getRawId() { return id; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUser() { return user; }
    public void setUser(Long user) { this.user = user; }

    public List<CartItemDto> getItems() { return items; }
    public void setItems(List<CartItemDto> items) { this.items = items; }
}
