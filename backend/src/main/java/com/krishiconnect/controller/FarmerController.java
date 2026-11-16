package com.krishiconnect.controller;

import com.krishiconnect.dto.*;
import com.krishiconnect.security.UserPrincipal;
import com.krishiconnect.service.AuthService;
import com.krishiconnect.service.FarmerService;
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
import java.util.Map;

@RestController
@RequestMapping("/api/farmer")
@PreAuthorize("hasRole('FARMER')")
public class FarmerController {

    private final FarmerService farmerService;
    private final AuthService authService;

    public FarmerController(FarmerService farmerService, AuthService authService) {
        this.farmerService = farmerService;
        this.authService = authService;
    }

    /* ── Harvest Requirements ─────────────────────────────────────────── */

    @PostMapping(value = { "/requirements", "/harvest" })
    public ResponseEntity<Map<String, Object>> postRequirement(@AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody HarvestRequirementRequest req) {
        HarvestRequirementResponse response = farmerService.postRequirement(currentUser.getId(), req);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Requirement posted successfully");
        res.put("data", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping(value = { "/requirements/my", "/requirements" })
    public ResponseEntity<Map<String, Object>> getMyRequirements(@AuthenticationPrincipal UserPrincipal currentUser,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        Page<HarvestRequirementResponse> pageResult = farmerService.getMyRequirements(currentUser.getId(), status, page,
                limit);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("data", pageResult.getContent());
        res.put("crops", pageResult.getContent());
        res.put("pagination", new PaginationMeta(pageResult.getTotalElements(), page, limit));
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/requirements/{id}")
    public ResponseEntity<Map<String, Object>> updateRequirement(@AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String status = body.get("status");
        String notes = body.get("notes");
        HarvestRequirementResponse response = farmerService.updateRequirement(currentUser.getId(), id, status, notes);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Requirement updated");
        res.put("data", response);
        return ResponseEntity.ok(res);
    }

    /* ── Service Offers (Bids from Providers) ─────────────────────────── */

    @GetMapping("/offers")
    public ResponseEntity<Map<String, Object>> getMyOffers(@AuthenticationPrincipal UserPrincipal currentUser,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        Page<BidResponseDto> pageResult = farmerService.getMyOffers(currentUser.getId(), status, page, limit);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("data", pageResult.getContent());
        res.put("crops", pageResult.getContent());
        res.put("pagination", new PaginationMeta(pageResult.getTotalElements(), page, limit));
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/offers/{bidId}")
    public ResponseEntity<Map<String, Object>> respondToBid(@AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable Long bidId,
            @RequestBody Map<String, String> body) {
        String status = body.get("status");
        BidResponseDto response = farmerService.respondToBid(currentUser.getId(), bidId, status);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Bid " + status);
        res.put("data", response);
        return ResponseEntity.ok(res);
    }

    /* ── Crop Listings ────────────────────────────────────────────────── */

    @PostMapping(value = { "/crop-listing", "/crops" }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Map<String, Object>> postCropListingMultipart(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestPart("cropName") String cropName,
            @RequestPart(value = "category", required = false) String category,
            @RequestPart(value = "variety", required = false) String variety,
            @RequestPart(value = "quantityAmount", required = false) String quantityAmount,
            @RequestPart(value = "quantityUnit", required = false) String quantityUnit,
            @RequestPart(value = "priceAmount", required = false) String priceAmount,
            @RequestPart(value = "priceUnit", required = false) String priceUnit,
            @RequestPart(value = "priceNegotiable", required = false) String priceNegotiable,
            @RequestPart(value = "harvestDate", required = false) String harvestDate,
            @RequestPart(value = "harvestSeason", required = false) String harvestSeason,
            @RequestPart(value = "shelfLifeDays", required = false) String shelfLifeDays,
            @RequestPart(value = "state", required = false) String state,
            @RequestPart(value = "district", required = false) String district,
            @RequestPart(value = "address", required = false) String address,
            @RequestPart(value = "pincode", required = false) String pincode,
            @RequestPart(value = "organic", required = false) String organic,
            @RequestPart(value = "grade", required = false) String grade,
            @RequestPart(value = "description", required = false) String description,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        CropListingRequest req = new CropListingRequest();
        req.setCropName(cropName);
        req.setCategory(category != null ? category : "Other");
        if (quantityAmount != null)
            req.setQuantity(
                    new QuantityDto(new BigDecimal(quantityAmount), quantityUnit != null ? quantityUnit : "Quintal"));
        if (priceAmount != null)
            req.setPrice(new PriceDto(new BigDecimal(priceAmount), priceUnit != null ? priceUnit : "per Quintal",
                    Boolean.parseBoolean(priceNegotiable)));
        req.setHarvestSeason(harvestSeason != null ? harvestSeason : "Kharif");
        req.setLocation(new LocationDto(state != null ? state : "Madhya Pradesh", district, address, pincode));
        if (grade != null)
            req.setHarvestGrade(grade);

        StringBuilder desc = new StringBuilder();
        if (variety != null && !variety.isBlank())
            desc.append("Variety: ").append(variety).append(". ");
        if (shelfLifeDays != null && !shelfLifeDays.isBlank())
            desc.append("Shelf life: ").append(shelfLifeDays).append(" days. ");
        if (organic != null && Boolean.parseBoolean(organic))
            desc.append("Organic certified. ");
        if (description != null && !description.isBlank())
            desc.append(description);
        req.setDescription(desc.toString().trim());

        CropListingResponse response = farmerService.postCropListing(currentUser.getId(), req, image);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Crop listed successfully");
        res.put("data", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PostMapping(value = { "/crop-listing", "/crops" }, consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Map<String, Object>> postCropListingJson(@AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody CropListingRequest req) {
        CropListingResponse response = farmerService.postCropListing(currentUser.getId(), req, null);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Crop listed successfully");
        res.put("data", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping(value = { "/crop-listing/my", "/crops" })
    public ResponseEntity<Map<String, Object>> getMyCropListings(@AuthenticationPrincipal UserPrincipal currentUser,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        Page<CropListingResponse> pageResult = farmerService.getMyCropListings(currentUser.getId(), status, page,
                limit);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("data", pageResult.getContent());
        res.put("crops", pageResult.getContent());
        res.put("pagination", new PaginationMeta(pageResult.getTotalElements(), page, limit));
        return ResponseEntity.ok(res);
    }

    @PatchMapping(value = "/crop-listing/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Map<String, Object>> updateCropListingMultipart(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable Long id,
            @ModelAttribute CropListingRequest req,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        CropListingResponse response = farmerService.updateCropListing(currentUser.getId(), id, req, image);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Crop listing updated");
        res.put("data", response);
        return ResponseEntity.ok(res);
    }

    @PatchMapping(value = "/crop-listing/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Map<String, Object>> updateCropListingJson(@AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable Long id,
            @RequestBody CropListingRequest req) {
        CropListingResponse response = farmerService.updateCropListing(currentUser.getId(), id, req, null);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Crop listing updated");
        res.put("data", response);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/crop-listing/{id}")
    public ResponseEntity<Map<String, Object>> deleteCropListing(@AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable Long id) {
        farmerService.deleteCropListing(currentUser.getId(), id);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Crop listing deleted");
        return ResponseEntity.ok(res);
    }

    /* ── Buyer Offers on My Crops ─────────────────────────────────────── */

    @GetMapping("/buyer-offers")
    public ResponseEntity<Map<String, Object>> getBuyerOffers(@AuthenticationPrincipal UserPrincipal currentUser,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        Page<CropOfferResponseDto> pageResult = farmerService.getBuyerOffers(currentUser.getId(), status, page, limit);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("data", pageResult.getContent());
        res.put("crops", pageResult.getContent());
        res.put("pagination", new PaginationMeta(pageResult.getTotalElements(), page, limit));
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/buyer-offers/{offerId}")
    public ResponseEntity<Map<String, Object>> respondToCropOffer(@AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable Long offerId,
            @RequestBody Map<String, Object> body) {
        String status = (String) body.get("status");
        BigDecimal counterPrice = body.get("counterPrice") != null ? new BigDecimal(body.get("counterPrice").toString())
                : null;
        String counterMessage = (String) body.get("counterMessage");

        CropOfferResponseDto response = farmerService.respondToCropOffer(currentUser.getId(), offerId, status,
                counterPrice, counterMessage);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Offer response recorded");
        res.put("data", response);
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
