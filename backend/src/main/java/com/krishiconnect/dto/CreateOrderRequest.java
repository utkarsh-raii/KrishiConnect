package com.krishiconnect.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class CreateOrderRequest {

    @NotNull(message = "Farmer ID is required")
    private Long farmer;

    @NotEmpty(message = "Items list cannot be empty")
    private List<OrderItemRequest> items;

    @NotNull(message = "Shipping address is required")
    private String shippingAddress;

    private String paymentMethod = "COD";

    public CreateOrderRequest() {}

    public Long getFarmer() { return farmer; }
    public void setFarmer(Long farmer) { this.farmer = farmer; }

    public List<OrderItemRequest> getItems() { return items; }
    public void setItems(List<OrderItemRequest> items) { this.items = items; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getPaymentMethod() { return paymentMethod != null ? paymentMethod : "COD"; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
