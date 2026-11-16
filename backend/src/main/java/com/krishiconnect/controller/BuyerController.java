package com.krishiconnect.controller;

import com.krishiconnect.dto.*;
import com.krishiconnect.security.UserPrincipal;
import com.krishiconnect.service.AuthService;
import com.krishiconnect.service.BuyerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/buyer")
public class BuyerController {

    private final BuyerService buyerService;
    private final AuthService authService;

    public BuyerController(BuyerService buyerService, AuthService authService) {
        this.buyerService = buyerService;
        this.authService = authService;
    }

    /* ── Public Crop Browsing ─────────────────────────────────────────── */

    @GetMapping(value = {"/listings", "/crop-listings", "/crops"})
    public ResponseEntity<Map<String, Object>> getCropListings(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) String season,
            @RequestParam(required = false) String grade,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {

        Page<CropListingResponse> pageResult = buyerService.getCropListings(
                status, category, state, district, season, grade, minPrice, maxPrice, search, page, limit
        );

        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("data", pageResult.getContent());
        res.put("pagination", new PaginationMeta(pageResult.getTotalElements(), page, limit));
        return ResponseEntity.ok(res);
    }

    @GetMapping("/crop-listings/{id}")
    public ResponseEntity<Map<String, Object>> getCropListingById(@PathVariable Long id) {
        CropListingResponse crop = buyerService.getCropListingById(id);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("data", crop);
        return ResponseEntity.ok(res);
    }

    /* ── Protected Buyer Routes ───────────────────────────────────────── */

    @PostMapping(value = {"/make-offer", "/offers"})
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<Map<String, Object>> makeOffer(@AuthenticationPrincipal UserPrincipal currentUser,
                                                         @Valid @RequestBody MakeOfferRequest req) {
        CropOfferResponseDto offer = buyerService.makeOffer(currentUser.getId(), req);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Offer submitted successfully");
        res.put("data", offer);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/my-offers")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<Map<String, Object>> getMyOffers(@AuthenticationPrincipal UserPrincipal currentUser) {
        List<CropOfferResponseDto> offers = buyerService.getMyOffers(currentUser.getId());
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("data", offers);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/purchase")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<Map<String, Object>> initiatePurchase(@AuthenticationPrincipal UserPrincipal currentUser,
                                                                @Valid @RequestBody InitiatePurchaseRequest req) {
        PurchaseResponseDto purchase = buyerService.initiatePurchase(currentUser.getId(), req);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Purchase initiated successfully");
        res.put("data", purchase);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PostMapping("/direct-purchase")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<Map<String, Object>> directPurchase(@AuthenticationPrincipal UserPrincipal currentUser,
                                                              @Valid @RequestBody DirectPurchaseRequest req) {
        PurchaseResponseDto purchase = buyerService.directPurchase(currentUser.getId(), req);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Direct purchase created");
        res.put("data", purchase);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PostMapping(value = {"/upload-receipt/{purchaseId}", "/orders/{purchaseId}/receipt"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<Map<String, Object>> uploadReceipt(@AuthenticationPrincipal UserPrincipal currentUser,
                                                             @PathVariable Long purchaseId,
                                                             @RequestParam("receipt") MultipartFile file) {
        PurchaseResponseDto purchase = buyerService.uploadReceipt(currentUser.getId(), purchaseId, file);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Receipt uploaded successfully");
        res.put("data", purchase);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/orders")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<Map<String, Object>> getMyOrders(@AuthenticationPrincipal UserPrincipal currentUser) {
        List<PurchaseResponseDto> orders = buyerService.getMyOrders(currentUser.getId());
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("data", orders);
        res.put("orders", orders);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/orders/{id}")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<Map<String, Object>> getOrderById(@AuthenticationPrincipal UserPrincipal currentUser,
                                                            @PathVariable Long id) {
        PurchaseResponseDto order = buyerService.getOrderById(currentUser.getId(), id);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("data", order);
        res.put("order", order);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<Map<String, Object>> getProfile(@AuthenticationPrincipal UserPrincipal currentUser) {
        UserDto user = authService.getMe(currentUser.getId());
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("data", user);
        res.put("user", user);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<Map<String, Object>> updateProfile(@AuthenticationPrincipal UserPrincipal currentUser,
                                                             @RequestBody UpdateProfileRequest req) {
        UserDto user = authService.updateProfile(currentUser.getId(), req);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("data", user);
        res.put("user", user);
        return ResponseEntity.ok(res);
    }
}
