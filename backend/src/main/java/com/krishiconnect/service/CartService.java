package com.krishiconnect.service;

import com.krishiconnect.dto.AddToCartRequest;
import com.krishiconnect.dto.CartResponseDto;

public interface CartService {

    CartResponseDto getCart(Long userId);

    CartResponseDto addToCart(Long userId, AddToCartRequest req);

    CartResponseDto removeFromCart(Long userId, Long productId);
}
