package com.krishiconnect.service;

import com.krishiconnect.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface BuyerService {
    Page<CropListingResponse> getCropListings(String status, String category, String state, String district,
                                             String season, String grade, BigDecimal minPrice, BigDecimal maxPrice,
                                             String search, int page, int limit);

    List<CropListingResponse> getAllActiveListings();

    CropListingResponse getCropListingById(Long id);

    CropOfferResponseDto makeOffer(Long buyerId, MakeOfferRequest req);

    List<CropOfferResponseDto> getMyOffers(Long buyerId);

    PurchaseResponseDto initiatePurchase(Long buyerId, InitiatePurchaseRequest req);

    PurchaseResponseDto uploadReceipt(Long buyerId, Long purchaseId, MultipartFile file);

    List<PurchaseResponseDto> getMyOrders(Long buyerId);

    PurchaseResponseDto getOrderById(Long buyerId, Long purchaseId);

    PurchaseResponseDto directPurchase(Long buyerId, DirectPurchaseRequest req);
}
