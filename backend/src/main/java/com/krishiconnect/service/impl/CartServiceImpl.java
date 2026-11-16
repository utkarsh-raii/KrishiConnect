package com.krishiconnect.service.impl;

import com.krishiconnect.dto.AddToCartRequest;
import com.krishiconnect.dto.CartResponseDto;
import com.krishiconnect.entity.Cart;
import com.krishiconnect.entity.CartItem;
import com.krishiconnect.entity.CropListing;
import com.krishiconnect.entity.User;
import com.krishiconnect.exception.ResourceNotFoundException;
import com.krishiconnect.repository.CartRepository;
import com.krishiconnect.repository.CropListingRepository;
import com.krishiconnect.repository.UserRepository;
import com.krishiconnect.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CropListingRepository cropListingRepository;
    private final UserRepository userRepository;

    public CartServiceImpl(CartRepository cartRepository,
                           CropListingRepository cropListingRepository,
                           UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cropListingRepository = cropListingRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public CartResponseDto getCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        return CartResponseDto.fromEntity(cart);
    }

    @Override
    @Transactional
    public CartResponseDto addToCart(Long userId, AddToCartRequest req) {
        Cart cart = getOrCreateCart(userId);
        CropListing crop = cropListingRepository.findById(req.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Crop listing not found"));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getCropListing().getId().equals(req.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity().add(req.getQuantity()));
        } else {
            CartItem newItem = new CartItem(cart, crop, req.getQuantity());
            cart.addItem(newItem);
        }

        cart = cartRepository.save(cart);
        return CartResponseDto.fromEntity(cart);
    }

    @Override
    @Transactional
    public CartResponseDto removeFromCart(Long userId, Long productId) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().removeIf(item -> item.getCropListing().getId().equals(productId));
        cart = cartRepository.save(cart);
        return CartResponseDto.fromEntity(cart);
    }

    private Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            Cart newCart = new Cart(user);
            return cartRepository.save(newCart);
        });
    }
}
