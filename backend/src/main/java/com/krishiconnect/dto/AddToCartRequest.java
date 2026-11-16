package com.krishiconnect.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class AddToCartRequest {

    @NotNull(message = "productId is required")
    private Long productId;

    private BigDecimal quantity = BigDecimal.ONE;

    public AddToCartRequest() {}

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public BigDecimal getQuantity() { return quantity != null ? quantity : BigDecimal.ONE; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
}
