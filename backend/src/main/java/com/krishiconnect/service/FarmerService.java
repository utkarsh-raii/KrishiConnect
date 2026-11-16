package com.krishiconnect.service;

import com.krishiconnect.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface FarmerService {
    HarvestRequirementResponse postRequirement(Long farmerId, HarvestRequirementRequest req);
    Page<HarvestRequirementResponse> getMyRequirements(Long farmerId, String status, int page, int limit);
    HarvestRequirementResponse updateRequirement(Long farmerId, Long requirementId, String status, String notes);

    Page<BidResponseDto> getMyOffers(Long farmerId, String status, int page, int limit);
    BidResponseDto respondToBid(Long farmerId, Long bidId, String status);

    CropListingResponse postCropListing(Long farmerId, CropListingRequest req, MultipartFile image);
    Page<CropListingResponse> getMyCropListings(Long farmerId, String status, int page, int limit);
    CropListingResponse updateCropListing(Long farmerId, Long listingId, CropListingRequest req, MultipartFile image);
    void deleteCropListing(Long farmerId, Long listingId);

    Page<CropOfferResponseDto> getBuyerOffers(Long farmerId, String status, int page, int limit);
    CropOfferResponseDto respondToCropOffer(Long farmerId, Long offerId, String status, BigDecimal counterPrice, String counterMessage);

    List<PurchaseResponseDto> getPurchaseRequests(Long farmerId);
    PurchaseResponseDto updatePurchaseRequestStatus(Long farmerId, Long purchaseId, String status);
}
