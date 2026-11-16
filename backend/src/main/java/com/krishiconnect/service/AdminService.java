package com.krishiconnect.service;

import com.krishiconnect.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {

    DashboardStatsDto getDashboardStats();

    Page<UserDto> getUsers(String role, Boolean isBlocked, String search, Pageable pageable);

    UserDto getUserById(Long userId);

    UserDto toggleBlockUser(Long userId);

    void deleteUser(Long userId);

    Page<PurchaseResponseDto> getTransactions(String paymentStatus, Pageable pageable);

    PurchaseResponseDto verifyTransaction(Long purchaseId);

    PurchaseResponseDto flagDispute(Long purchaseId);

    Page<CropListingResponse> getAllListings(String status, String cropName, Pageable pageable);

    void removeListing(Long listingId);

    CropListingResponse updateListingStatus(Long listingId, String status);

    Page<PurchaseResponseDto> getDisputes(Pageable pageable);

    PurchaseResponseDto resolveDispute(Long purchaseId, String resolution);
}
