package com.krishiconnect.service.impl;

import com.krishiconnect.dto.*;
import com.krishiconnect.entity.*;
import com.krishiconnect.exception.BadRequestException;
import com.krishiconnect.exception.ResourceNotFoundException;
import com.krishiconnect.repository.*;
import com.krishiconnect.service.BuyerService;
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
public class BuyerServiceImpl implements BuyerService {

    private final CropListingRepository cropListingRepository;
    private final CropOfferRepository cropOfferRepository;
    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final NotificationService notificationService;

    public BuyerServiceImpl(CropListingRepository cropListingRepository,
                            CropOfferRepository cropOfferRepository,
                            PurchaseRepository purchaseRepository,
                            UserRepository userRepository,
                            FileStorageService fileStorageService,
                            NotificationService notificationService) {
        this.cropListingRepository = cropListingRepository;
        this.cropOfferRepository = cropOfferRepository;
        this.purchaseRepository = purchaseRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CropListingResponse> getCropListings(String status, String category, String state, String district,
                                                     String season, String grade, BigDecimal minPrice, BigDecimal maxPrice,
                                                     String search, int page, int limit) {
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        String queryStatus = (status != null && !status.isBlank()) ? status : "active";

        Page<CropListing> listings = cropListingRepository.searchListings(
                queryStatus,
                (category != null && !category.isBlank()) ? category : null,
                (state != null && !state.isBlank()) ? state : null,
                (district != null && !district.isBlank()) ? district : null,
                (season != null && !season.isBlank()) ? season : null,
                (grade != null && !grade.isBlank()) ? grade : null,
                minPrice,
                maxPrice,
                (search != null && !search.isBlank()) ? search.trim() : null,
                pageable
        );
        return listings.map(CropListingResponse::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CropListingResponse> getAllActiveListings() {
        List<CropListing> listings = cropListingRepository.findAllActiveWithFarmer();
        return listings.stream().map(CropListingResponse::fromEntity).toList();
    }

    @Override
    @Transactional
    public CropListingResponse getCropListingById(Long id) {
        CropListing listing = cropListingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Crop listing not found"));
        listing.setViews(listing.getViews() + 1);
        cropListingRepository.save(listing);
        return CropListingResponse.fromEntity(listing);
    }

    @Override
    @Transactional
    public CropOfferResponseDto makeOffer(Long buyerId, MakeOfferRequest req) {
        // Prevent a farmer from making an offer on their own listing
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found"));
        CropListing listing = cropListingRepository.findById(req.getListingId())
                .orElseThrow(() -> new ResourceNotFoundException("Crop listing not found"));
        if (listing.getFarmer().getId().equals(buyerId)) {
            throw new BadRequestException("Farmer cannot make an offer on their own listing");
        }
        // Validate quantity does not exceed available stock
        if (req.resolveQuantityAmount().compareTo(listing.getQuantityAmount()) > 0) {
            throw new BadRequestException("Offer quantity exceeds available stock");
        }
        // Existing validation
        if (!"active".equalsIgnoreCase(listing.getStatus())) {
            throw new BadRequestException("This crop listing is not active");
        }
        CropOffer offer = new CropOffer();
        offer.setBuyer(buyer);
        offer.setFarmer(listing.getFarmer());
        offer.setListing(listing);
        offer.setOfferPrice(req.getOfferPrice());
        offer.setQuantityAmount(req.resolveQuantityAmount());
        offer.setQuantityUnit(req.resolveQuantityUnit());
        offer.setMessage(req.getMessage());
        offer.setStatus("pending");

        offer = cropOfferRepository.save(offer);

        listing.setOfferCount(listing.getOfferCount() + 1);
        cropListingRepository.save(listing);

        notificationService.createNotification(
                listing.getFarmer().getId(),
                "New Crop Offer Received",
                buyer.getName() + " made an offer of ₹" + req.getOfferPrice() + " on " + listing.getCropName()
        );

        return CropOfferResponseDto.fromEntity(offer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CropOfferResponseDto> getMyOffers(Long buyerId) {
        List<CropOffer> offers = cropOfferRepository.findByBuyerIdOrderByCreatedAtDesc(buyerId);
        return offers.stream().map(CropOfferResponseDto::fromEntity).toList();
    }

    @Override
    @Transactional
    public PurchaseResponseDto initiatePurchase(Long buyerId, InitiatePurchaseRequest req) {
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found"));

        CropOffer offer = cropOfferRepository.findByIdAndBuyerIdAndStatus(req.getOfferId(), buyerId, "accepted")
                .orElseThrow(() -> new ResourceNotFoundException("Accepted offer not found for id: " + req.getOfferId()));

        CropListing listing = offer.getListing();

        if (purchaseRepository.findByBuyerIdAndListingId(buyerId, listing.getId()).isPresent()) {
            throw new BadRequestException("Purchase already exists for this listing");
        }

        BigDecimal total = offer.getOfferPrice().multiply(offer.getQuantityAmount());

        Purchase purchase = new Purchase();
        purchase.setBuyer(buyer);
        purchase.setFarmer(offer.getFarmer());
        purchase.setListing(listing);
        purchase.setOffer(offer);
        purchase.setQuantityAmount(offer.getQuantityAmount());
        purchase.setQuantityUnit(offer.getQuantityUnit());
        purchase.setAgreedPrice(offer.getOfferPrice());
        purchase.setTotalAmount(total);
        purchase.setPaymentMethod(req.getPaymentMethod() != null ? req.getPaymentMethod() : "NEFT");
        purchase.setTransactionId(req.getTransactionId());
        purchase.setNotes(req.getNotes());
        purchase.setPaymentStatus("pending");

        purchase = purchaseRepository.save(purchase);

        notificationService.createNotification(
                offer.getFarmer().getId(),
                "New Purchase Request",
                buyer.getName() + " initiated purchase for " + listing.getCropName()
        );

        return PurchaseResponseDto.fromEntity(purchase);
    }

    @Override
    @Transactional
    public PurchaseResponseDto uploadReceipt(Long buyerId, Long purchaseId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Receipt file is required");
        }

        Purchase purchase = purchaseRepository.findByIdAndBuyerId(purchaseId, buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found"));

        String filename = fileStorageService.storeFile(file, "receipts");
        purchase.setReceiptFileName(filename);
        purchase.setReceiptFilePath("uploads/receipts/" + filename);
        purchase.setReceiptUploadedAt(LocalDateTime.now());
        purchase.setPaymentStatus("receipt-uploaded");

        purchase = purchaseRepository.save(purchase);

        notificationService.createNotification(
                purchase.getFarmer().getId(),
                "Receipt Uploaded",
                purchase.getBuyer().getName() + " uploaded payment receipt for " + purchase.getListing().getCropName()
        );

        return PurchaseResponseDto.fromEntity(purchase);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseResponseDto> getMyOrders(Long buyerId) {
        List<Purchase> list = purchaseRepository.findByBuyerIdOrderByCreatedAtDesc(buyerId);
        return list.stream().map(PurchaseResponseDto::fromEntity).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseResponseDto getOrderById(Long buyerId, Long purchaseId) {
        Purchase p = purchaseRepository.findByIdAndBuyerId(purchaseId, buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return PurchaseResponseDto.fromEntity(p);
    }

    @Override
    @Transactional
    public PurchaseResponseDto directPurchase(Long buyerId, DirectPurchaseRequest req) {
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found"));
        CropListing listing = cropListingRepository.findById(req.getListingId())
                .orElseThrow(() -> new ResourceNotFoundException("Crop listing not found"));
        if (!"active".equalsIgnoreCase(listing.getStatus())) {
            throw new BadRequestException("Listing is not active");
        }
        // Prevent farmer from buying own listing
        if (listing.getFarmer().getId().equals(buyerId)) {
            throw new BadRequestException("Farmer cannot purchase own listing");
        }
        // Validate quantity does not exceed available amount (simplified)
        if (req.getQuantityAmount().compareTo(listing.getQuantityAmount()) > 0) {
            throw new BadRequestException("Requested quantity exceeds available stock");
        }
        // Existing logic
        BigDecimal qty = req.getQuantityAmount();
        BigDecimal total = listing.getPriceAmount().multiply(qty);

        Purchase purchase = new Purchase();
        purchase.setBuyer(buyer);
        purchase.setFarmer(listing.getFarmer());
        purchase.setListing(listing);
        purchase.setQuantityAmount(qty);
        purchase.setQuantityUnit(listing.getQuantityUnit());
        purchase.setAgreedPrice(listing.getPriceAmount());
        purchase.setTotalAmount(total);
        purchase.setNotes(req.getNotes());
        purchase.setPaymentStatus("pending");

        purchase = purchaseRepository.save(purchase);

        notificationService.createNotification(
                listing.getFarmer().getId(),
                "New Order Received!",
                buyer.getName() + " placed an order for " + qty + " " + listing.getQuantityUnit() + " of " + listing.getCropName()
        );

        return PurchaseResponseDto.fromEntity(purchase);
    }
}
