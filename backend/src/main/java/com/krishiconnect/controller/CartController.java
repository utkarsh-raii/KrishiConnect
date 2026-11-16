package com.krishiconnect.controller;

import com.krishiconnect.dto.AddToCartRequest;
import com.krishiconnect.dto.CartResponseDto;
import com.krishiconnect.security.UserPrincipal;
import com.krishiconnect.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getCart(@AuthenticationPrincipal UserPrincipal currentUser) {
        CartResponseDto cart = cartService.getCart(currentUser.getId());
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("cart", cart);
        res.put("data", cart);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addToCart(@AuthenticationPrincipal UserPrincipal currentUser,
                                                         @Valid @RequestBody AddToCartRequest req) {
        CartResponseDto cart = cartService.addToCart(currentUser.getId(), req);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Item added to cart");
        res.put("cart", cart);
        res.put("data", cart);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Map<String, Object>> removeFromCart(@AuthenticationPrincipal UserPrincipal currentUser,
                                                              @PathVariable Long productId) {
        CartResponseDto cart = cartService.removeFromCart(currentUser.getId(), productId);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Item removed from cart");
        res.put("cart", cart);
        res.put("data", cart);
        return ResponseEntity.ok(res);
    }
}
