package com.krishiconnect.controller;

import com.krishiconnect.dto.*;
import com.krishiconnect.security.UserPrincipal;
import com.krishiconnect.service.AuthService;
import com.krishiconnect.service.ProviderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/provider")
@PreAuthorize("hasRole('PROVIDER')")
public class ProviderController {

    private final ProviderService providerService;
    private final AuthService authService;

    public ProviderController(ProviderService providerService, AuthService authService) {
        this.providerService = providerService;
        this.authService = authService;
    }

    /* ── Service Listings ─────────────────────────────────────────────── */

    @PostMapping(value = {"/service", "/services"})
    public ResponseEntity<Map<String, Object>> postService(@AuthenticationPrincipal UserPrincipal currentUser,
                                                           @Valid @RequestBody ServiceListingRequest req) {
        ServiceListingResponse svc = providerService.postService(currentUser.getId(), req);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Service posted successfully");
        res.put("data", svc);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping(value = {"/service/my", "/services"})
    public ResponseEntity<Map<String, Object>> getMyServices(@AuthenticationPrincipal UserPrincipal currentUser,
                                                             @RequestParam(defaultValue = "1") int page,
                                                             @RequestParam(defaultValue = "10") int limit) {
        int pageIndex = Math.max(0, page - 1);
        Page<ServiceListingResponse> pageResult = providerService.getMyServices(
                currentUser.getId(),
                PageRequest.of(pageIndex, limit, Sort.by(Sort.Direction.DESC, "createdAt"))
        );
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("data", pageResult.getContent());
        res.put("services", pageResult.getContent());
        res.put("pagination", new PaginationMeta(pageResult.getTotalElements(), page, limit));
        return ResponseEntity.ok(res);
    }

    @PutMapping(value = {"/service/{id}", "/services/{id}"})
    public ResponseEntity<Map<String, Object>> updateService(@AuthenticationPrincipal UserPrincipal currentUser,
                                                             @PathVariable Long id,
                                                             @Valid @RequestBody ServiceListingRequest req) {
        ServiceListingResponse svc = providerService.updateService(currentUser.getId(), id, req);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Service updated");
        res.put("data", svc);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping(value = {"/service/{id}", "/services/{id}"})
    public ResponseEntity<Map<String, Object>> deleteService(@AuthenticationPrincipal UserPrincipal currentUser,
                                                             @PathVariable Long id) {
        providerService.deleteService(currentUser.getId(), id);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Service deleted");
        return ResponseEntity.ok(res);
    }

    /* ── Open Farmer Requirements ─────────────────────────────────────── */

    @GetMapping(value = {"/farmer-requests", "/requests"})
    public ResponseEntity<Map<String, Object>> getFarmerRequests(
            @RequestParam(required = false) String cropType,
            @RequestParam(required = false) String vehicleType,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String urgency,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {

        int pageIndex = Math.max(0, page - 1);
        Page<HarvestRequirementResponse> pageResult = providerService.getFarmerRequests(
                cropType, vehicleType, state, urgency,
                PageRequest.of(pageIndex, limit, Sort.by(Sort.Direction.DESC, "createdAt"))
        );
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("data", pageResult.getContent());
        res.put("services", pageResult.getContent());
        res.put("pagination", new PaginationMeta(pageResult.getTotalElements(), page, limit));
        return ResponseEntity.ok(res);
    }

    /* ── Bids ─────────────────────────────────────────────────────────── */

    @PostMapping(value = {"/bid", "/bids"})
    public ResponseEntity<Map<String, Object>> submitBid(@AuthenticationPrincipal UserPrincipal currentUser,
                                                         @Valid @RequestBody SubmitBidRequest req) {
        BidResponseDto bid = providerService.submitBid(currentUser.getId(), req);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Bid submitted successfully");
        res.put("data", bid);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/bids/my")
    public ResponseEntity<Map<String, Object>> getMyBids(@AuthenticationPrincipal UserPrincipal currentUser,
                                                         @RequestParam(required = false) String status,
                                                         @RequestParam(defaultValue = "1") int page,
                                                         @RequestParam(defaultValue = "10") int limit) {
        int pageIndex = Math.max(0, page - 1);
        Page<BidResponseDto> pageResult = providerService.getMyBids(
                currentUser.getId(), status,
                PageRequest.of(pageIndex, limit, Sort.by(Sort.Direction.DESC, "createdAt"))
        );
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("data", pageResult.getContent());
        res.put("services", pageResult.getContent());
        res.put("pagination", new PaginationMeta(pageResult.getTotalElements(), page, limit));
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/bids/{bidId}")
    public ResponseEntity<Map<String, Object>> withdrawBid(@AuthenticationPrincipal UserPrincipal currentUser,
                                                           @PathVariable Long bidId) {
        BidResponseDto bid = providerService.withdrawBid(currentUser.getId(), bidId);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Bid withdrawn");
        res.put("data", bid);
        return ResponseEntity.ok(res);
    }

    /* ── Bookings ─────────────────────────────────────────────────────── */

    @GetMapping("/bookings")
    public ResponseEntity<Map<String, Object>> getBookings(@AuthenticationPrincipal UserPrincipal currentUser,
                                                           @RequestParam(defaultValue = "1") int page,
                                                           @RequestParam(defaultValue = "10") int limit) {
        int pageIndex = Math.max(0, page - 1);
        Page<BidResponseDto> pageResult = providerService.getBookings(
                currentUser.getId(),
                PageRequest.of(pageIndex, limit, Sort.by(Sort.Direction.DESC, "updatedAt"))
        );
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("data", pageResult.getContent());
        res.put("services", pageResult.getContent());
        res.put("pagination", new PaginationMeta(pageResult.getTotalElements(), page, limit));
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/bookings/{bidId}/complete")
    public ResponseEntity<Map<String, Object>> completeBooking(@AuthenticationPrincipal UserPrincipal currentUser,
                                                               @PathVariable Long bidId) {
        BidResponseDto bid = providerService.completeBooking(currentUser.getId(), bidId);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Booking marked as completed");
        res.put("data", bid);
        return ResponseEntity.ok(res);
    }

    /* ── Profile ──────────────────────────────────────────────────────── */

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile(@AuthenticationPrincipal UserPrincipal currentUser) {
        UserDto user = authService.getMe(currentUser.getId());
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("data", user);
        res.put("user", user);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/profile")
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
