package com.krishiconnect.service.impl;

import com.krishiconnect.dto.*;
import com.krishiconnect.entity.*;
import com.krishiconnect.exception.BadRequestException;
import com.krishiconnect.exception.ForbiddenException;
import com.krishiconnect.exception.ResourceNotFoundException;
import com.krishiconnect.repository.*;
import com.krishiconnect.service.FarmerService;
import com.krishiconnect.service.NotificationService;
import com.krishiconnect.util.FileStorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FarmerServiceImpl implements FarmerService {

    private final HarvestRequirementRepository requirementRepository;
    private final BidOfferRepository bidOfferRepository;
    private final CropListingRepository cropListingRepository;
    private final CropOfferRepository cropOfferRepository;
    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final NotificationService notificationService;

    public FarmerServiceImpl(HarvestRequirementRepository requirementRepository,
                             BidOfferRepository bidOfferRepository,
                             CropListingRepository cropListingRepository,
                             CropOfferRepository cropOfferRepository,
                             PurchaseRepository purchaseRepository,
                             UserRepository userRepository,
                             FileStorageService fileStorageService,
                             NotificationService notificationService) {
        this.requirementRepository = requirementRepository;
        this.bidOfferRepository = bidOfferRepository;
        this.cropListingRepository = cropListingRepository;
        this.cropOfferRepository = cropOfferRepository;
        this.purchaseRepository = purchaseRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional
    public HarvestRequirementResponse postRequirement(Long farmerId, HarvestRequirementRequest req) {
        User farmer = userRepository.findById(farmerId)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found"));

        HarvestRequirement entity = new HarvestRequirement();
        entity.setFarmer(farmer);
        entity.setVehicleType(req.getVehicleType());
        entity.setManpower(req.getManpower() != null ? req.getManpower() : 0);
        entity.setCropType(req.getCropType());
        entity.setQuantityAmount(req.getQuantity().getAmount());
        entity.setQuantityUnit(req.getQuantity().getUnit());
        entity.setDurationValue(req.getDuration().getValue());
        entity.setDurationUnit(req.getDuration().getUnit());
        entity.setAddress(req.getLocation().getAddress());
        entity.setDistrict(req.getLocation().getDistrict());
        entity.setState(req.getLocation().getState());
        entity.setPincode(req.getLocation().getPincode());
        entity.setUrgency(req.getUrgency());
        entity.setNotes(req.getNotes());
        entity.setStatus("open");

        entity = requirementRepository.save(entity);
        return HarvestRequirementResponse.fromEntity(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HarvestRequirementResponse> getMyRequirements(Long farmerId, String status, int page, int limit) {
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<HarvestRequirement> pageResult;
        if (status != null && !status.isBlank()) {
            pageResult = requirementRepository.findByFarmerIdAndStatus(farmerId, status, pageable);
        } else {
            pageResult = requirementRepository.findByFarmerId(farmerId, pageable);
        }
        return pageResult.map(HarvestRequirementResponse::fromEntity);
    }

    @Override
    @Transactional
    public HarvestRequirementResponse updateRequirement(Long farmerId, Long requirementId, String status, String notes) {
        HarvestRequirement req = requirementRepository.findById(requirementId)
                .orElseThrow(() -> new ResourceNotFoundException("Requirement not found"));

        if (!req.getFarmer().getId().equals(farmerId)) {
            throw new ForbiddenException("You do not own this requirement");
        }
        if ("completed".equalsIgnoreCase(req.getStatus())) {
            throw new BadRequestException("Cannot edit a completed requirement");
        }

        if (status != null && !status.isBlank()) req.setStatus(status);
        if (notes != null) req.setNotes(notes);

        req = requirementRepository.save(req);
        return HarvestRequirementResponse.fromEntity(req);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BidResponseDto> getMyOffers(Long farmerId, String status, int page, int limit) {
        List<Long> reqIds = requirementRepository.findIdsByFarmerId(farmerId);
        if (reqIds.isEmpty()) {
            return Page.empty();
        }
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<BidOffer> bids;
        if (status != null && !status.isBlank()) {
            bids = bidOfferRepository.findByRequirementIdInAndStatus(reqIds, status, pageable);
        } else {
            bids = bidOfferRepository.findByRequirementIdIn(reqIds, pageable);
        }
        return bids.map(BidResponseDto::fromEntity);
    }

    @Override
    @Transactional
    public BidResponseDto respondToBid(Long farmerId, Long bidId, String status) {
        if (!"accepted".equalsIgnoreCase(status) && !"rejected".equalsIgnoreCase(status)) {
            throw new BadRequestException("Status must be 'accepted' or 'rejected'");
        }

        BidOffer bid = bidOfferRepository.findById(bidId)
                .orElseThrow(() -> new ResourceNotFoundException("Bid not found"));

        if (!bid.getRequirement().getFarmer().getId().equals(farmerId)) {
            throw new ForbiddenException("You do not own this requirement");
        }

        if (!"pending".equalsIgnoreCase(bid.getStatus())) {
            throw new BadRequestException("Bid is already " + bid.getStatus());
        }

        bid.setStatus(status.toLowerCase());
        bid.setRespondedAt(LocalDateTime.now());
        bid = bidOfferRepository.save(bid);

        if ("accepted".equalsIgnoreCase(status)) {
            HarvestRequirement req = bid.getRequirement();
            req.setStatus("in-progress");
            req.setAcceptedBidId(bid.getId());
            requirementRepository.save(req);

            bidOfferRepository.rejectOtherPendingBids(req.getId(), bid.getId());

            notificationService.createNotification(
                    bid.getProvider().getId(),
                    "Bid Accepted!",
                    "Your bid of ₹" + bid.getPrice() + " on requirement " + req.getCropType() + " was accepted!"
            );
        } else {
            notificationService.createNotification(
                    bid.getProvider().getId(),
                    "Bid Rejected",
                    "Your bid on requirement " + bid.getRequirement().getCropType() + " was rejected."
            );
        }

        return BidResponseDto.fromEntity(bid);
    }

    @Override
    @Transactional
    public CropListingResponse postCropListing(Long farmerId, CropListingRequest req, MultipartFile image) {
        User farmer = userRepository.findById(farmerId)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found"));

        CropListing listing = new CropListing();
        listing.setFarmer(farmer);
        listing.setCropName(req.getCropName());
        listing.setCategory(req.getCategory());
        listing.setQuantityAmount(req.resolveQuantityAmount());
        listing.setQuantityUnit(req.resolveQuantityUnit());
        listing.setPriceAmount(req.resolvePriceAmount());
        listing.setPriceUnit(req.resolvePriceUnit());
        listing.setPriceNegotiable(req.resolvePriceNegotiable());
        listing.setAddress(req.resolveAddress());
        listing.setDistrict(req.resolveDistrict());
        listing.setState(req.resolveState());
        listing.setPincode(req.resolvePincode());
        listing.setHarvestDate(req.getHarvestDate());
        listing.setHarvestSeason(req.getHarvestSeason());
        listing.setHarvestGrade(req.getHarvestGrade() != null ? req.getHarvestGrade() : "A");
        listing.setContactPhone(req.getContactPhone());
        listing.setDescription(req.getDescription());
        listing.setStatus("active");

        if (image != null && !image.isEmpty()) {
            String storedFilename = fileStorageService.storeFile(image, "crops");
            listing.setImage(storedFilename);
        } else if (req.getImage() != null && !req.getImage().isBlank()) {
            listing.setImage(req.getImage());
        }

        listing = cropListingRepository.save(listing);
        return CropListingResponse.fromEntity(listing);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CropListingResponse> getMyCropListings(Long farmerId, String status, int page, int limit) {
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<CropListing> pageResult;
        if (status != null && !status.isBlank()) {
            pageResult = cropListingRepository.findByFarmerIdAndStatus(farmerId, status, pageable);
        } else {
            pageResult = cropListingRepository.findByFarmerId(farmerId, pageable);
        }
        return pageResult.map(CropListingResponse::fromEntity);
    }

    @Override
    @Transactional
    public CropListingResponse updateCropListing(Long farmerId, Long listingId, CropListingRequest req, MultipartFile image) {
        CropListing listing = cropListingRepository.findById(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("Crop listing not found"));

        if (!listing.getFarmer().getId().equals(farmerId)) {
            throw new ForbiddenException("You do not own this listing");
        }

        if (req.getCropName() != null) listing.setCropName(req.getCropName());
        if (req.getCategory() != null) listing.setCategory(req.getCategory());
        if (req.getQuantity() != null || req.getQuantityAmount() != null) {
            listing.setQuantityAmount(req.resolveQuantityAmount());
            listing.setQuantityUnit(req.resolveQuantityUnit());
        }
        if (req.getPrice() != null || req.getPriceAmount() != null) {
            listing.setPriceAmount(req.resolvePriceAmount());
            listing.setPriceUnit(req.resolvePriceUnit());
            listing.setPriceNegotiable(req.resolvePriceNegotiable());
        }
        if (req.getLocation() != null || req.getLocationAddress() != null || req.getAddress() != null) {
            listing.setAddress(req.resolveAddress());
            listing.setDistrict(req.resolveDistrict());
            listing.setState(req.resolveState());
            listing.setPincode(req.resolvePincode());
        }
        if (req.getHarvestDate() != null) listing.setHarvestDate(req.getHarvestDate());
        if (req.getHarvestSeason() != null) listing.setHarvestSeason(req.getHarvestSeason());
        if (req.getHarvestGrade() != null) listing.setHarvestGrade(req.getHarvestGrade());
        if (req.getContactPhone() != null) listing.setContactPhone(req.getContactPhone());
        if (req.getDescription() != null) listing.setDescription(req.getDescription());
        if (req.getStatus() != null) listing.setStatus(req.getStatus());

        if (image != null && !image.isEmpty()) {
            String storedFilename = fileStorageService.storeFile(image, "crops");
            listing.setImage(storedFilename);
        }

        listing = cropListingRepository.save(listing);
        return CropListingResponse.fromEntity(listing);
    }

    @Override
    @Transactional
    public void deleteCropListing(Long farmerId, Long listingId) {
        CropListing listing = cropListingRepository.findById(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("Crop listing not found"));

        if (!listing.getFarmer().getId().equals(farmerId)) {
            throw new ForbiddenException("You do not own this listing");
        }

        cropListingRepository.delete(listing);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CropOfferResponseDto> getBuyerOffers(Long farmerId, String status, int page, int limit) {
        List<Long> listingIds = cropListingRepository.findIdsByFarmerId(farmerId);
        if (listingIds.isEmpty()) {
            return Page.empty();
        }
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<CropOffer> offers;
        if (status != null && !status.isBlank()) {
            offers = cropOfferRepository.findByListingIdInAndStatus(listingIds, status, pageable);
        } else {
            offers = cropOfferRepository.findByListingIdIn(listingIds, pageable);
        }
        return offers.map(CropOfferResponseDto::fromEntity);
    }

    @Override
    @Transactional
    public CropOfferResponseDto respondToCropOffer(Long farmerId, Long offerId, String status, BigDecimal counterPrice, String counterMessage) {
        CropOffer offer = cropOfferRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException("Offer not found"));

        if (!offer.getListing().getFarmer().getId().equals(farmerId)) {
            throw new ForbiddenException("You do not own the listing for this offer");
        }
        if (!"pending".equalsIgnoreCase(offer.getStatus())) {
            throw new BadRequestException("Offer is already " + offer.getStatus());
        }

        offer.setStatus(status.toLowerCase());
        offer.setRespondedAt(LocalDateTime.now());

        if ("counter".equalsIgnoreCase(status)) {
            if (counterPrice == null) {
                throw new BadRequestException("counterPrice is required for counter offers");
            }
            offer.setCounterPrice(counterPrice);
            offer.setCounterMessage(counterMessage);
        }

        offer = cropOfferRepository.save(offer);

        notificationService.createNotification(
                offer.getBuyer().getId(),
                "Offer " + status.toUpperCase(),
                "Farmer responded to your offer on " + offer.getListing().getCropName() + ": " + status
        );

        return CropOfferResponseDto.fromEntity(offer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseResponseDto> getPurchaseRequests(Long farmerId) {
        List<Purchase> purchases = purchaseRepository.findByFarmerIdOrderByCreatedAtDesc(farmerId);
        return purchases.stream().map(PurchaseResponseDto::fromEntity).toList();
    }

    @Override
    @Transactional
    public PurchaseResponseDto updatePurchaseRequestStatus(Long farmerId, Long purchaseId, String status) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase request not found"));

        if (!purchase.getFarmer().getId().equals(farmerId)) {
            throw new ForbiddenException("Not authorized to update this purchase request");
        }

        if ("accepted".equalsIgnoreCase(status)) {
            purchase.setPaymentStatus("verified");
        } else if ("rejected".equalsIgnoreCase(status)) {
            purchase.setPaymentStatus("disputed");
        } else {
            purchase.setPaymentStatus(status);
        }

        purchase = purchaseRepository.save(purchase);
        return PurchaseResponseDto.fromEntity(purchase);
    }
}
