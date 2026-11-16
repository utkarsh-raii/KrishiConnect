package com.krishiconnect.dto;

import java.math.BigDecimal;

public class OrderItemRequest {
    private Long product;
    private BigDecimal quantity;
    private BigDecimal price;

    public OrderItemRequest() {}

    public Long getProduct() { return product; }
    public void setProduct(Long product) { this.product = product; }

    public BigDecimal getQuantity() { return quantity != null ? quantity : BigDecimal.ONE; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
