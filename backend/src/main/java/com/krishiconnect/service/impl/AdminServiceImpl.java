package com.krishiconnect.service.impl;

import com.krishiconnect.dto.*;
import com.krishiconnect.entity.CropListing;
import com.krishiconnect.entity.Purchase;
import com.krishiconnect.entity.User;
import com.krishiconnect.exception.BadRequestException;
import com.krishiconnect.exception.ResourceNotFoundException;
import com.krishiconnect.repository.*;
import com.krishiconnect.service.AdminService;
import com.krishiconnect.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final CropListingRepository cropListingRepository;
    private final PurchaseRepository purchaseRepository;
    private final BidOfferRepository bidOfferRepository;
    private final HarvestRequirementRepository requirementRepository;
    private final FeedbackRepository feedbackRepository;
    private final NotificationService notificationService;

    public AdminServiceImpl(UserRepository userRepository,
                            CropListingRepository cropListingRepository,
                            PurchaseRepository purchaseRepository,
                            BidOfferRepository bidOfferRepository,
                            HarvestRequirementRepository requirementRepository,
                            FeedbackRepository feedbackRepository,
                            NotificationService notificationService) {
        this.userRepository = userRepository;
        this.cropListingRepository = cropListingRepository;
        this.purchaseRepository = purchaseRepository;
        this.bidOfferRepository = bidOfferRepository;
        this.requirementRepository = requirementRepository;
        this.feedbackRepository = feedbackRepository;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsDto getDashboardStats() {
        DashboardStatsDto stats = new DashboardStatsDto();

        // 1. Users
        long totalUsers = userRepository.count();
        long farmers = userRepository.countByRole("farmer");
        long providers = userRepository.countByRole("provider");
        long buyers = userRepository.countByRole("buyer");
        stats.setUsers(new DashboardStatsDto.UserStats(totalUsers, farmers, providers, buyers));

        // 2. Listings
        long totalListings = cropListingRepository.count();
        long activeListings = cropListingRepository.countByStatus("active");
        stats.setListings(new DashboardStatsDto.ListingStats(totalListings, activeListings));

        // 3. Transactions
        long totalTransactions = purchaseRepository.count();
        long pendingReceipts = purchaseRepository.countByPaymentStatus("receipt-uploaded");
        long completed = purchaseRepository.countByPaymentStatus("completed");
        BigDecimal totalRevenue = purchaseRepository.calculateTotalCompletedRevenue();
        stats.setTransactions(new DashboardStatsDto.TransactionStats(totalTransactions, pendingReceipts, completed, totalRevenue != null ? totalRevenue : BigDecimal.ZERO));

        // 4. Bids
        long totalBids = bidOfferRepository.count();
        long acceptedBids = bidOfferRepository.countByStatus("accepted");
        stats.setBids(new DashboardStatsDto.BidStats(totalBids, acceptedBids));

        // 5. Requirements
        long totalRequirements = requirementRepository.count();
        long openRequirements = requirementRepository.countByStatus("open");
        stats.setRequirements(new DashboardStatsDto.RequirementStats(totalRequirements, openRequirements));

        // 6. Feedback
        long totalFeedbacks = feedbackRepository.count();
        stats.setFeedback(new DashboardStatsDto.FeedbackStats(totalFeedbacks));

        // 7. Recent users
        List<UserDto> recentUsers = userRepository.findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt")))
                .stream().map(UserDto::fromEntity).collect(Collectors.toList());
        stats.setRecentUsers(recentUsers);

        // 8. Recent purchases
        List<PurchaseResponseDto> recentPurchases = purchaseRepository.findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt")))
                .stream().map(PurchaseResponseDto::fromEntity).collect(Collectors.toList());
        stats.setRecentPurchases(recentPurchases);

        // 9. Monthly sales
        List<Object[]> monthlyRows = purchaseRepository.findMonthlySalesSince(LocalDateTime.now().minusMonths(6));
        List<MonthlySaleDto> monthlySales = new ArrayList<>();
        if (monthlyRows != null) {
            for (Object[] row : monthlyRows) {
                int y = ((Number) row[0]).intValue();
                int m = ((Number) row[1]).intValue();
                BigDecimal total = row[2] != null ? new BigDecimal(row[2].toString()) : BigDecimal.ZERO;
                long count = ((Number) row[3]).longValue();
                monthlySales.add(new MonthlySaleDto(y, m, total, count));
            }
        }
        stats.setMonthlySales(monthlySales);

        return stats;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> getUsers(String role, Boolean isBlocked, String search, Pageable pageable) {
        return userRepository.searchUsers(role, isBlocked, search, pageable)
                .map(UserDto::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return UserDto.fromEntity(user);
    }

    @Override
    @Transactional
    public UserDto toggleBlockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setIsBlocked(!Boolean.TRUE.equals(user.getIsBlocked()));
        user = userRepository.save(user);
        return UserDto.fromEntity(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseResponseDto> getTransactions(String paymentStatus, Pageable pageable) {
        return purchaseRepository.searchPurchases(paymentStatus, pageable)
                .map(PurchaseResponseDto::fromEntity);
    }

    @Override
    @Transactional
    public PurchaseResponseDto verifyTransaction(Long purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase transaction not found"));

        purchase.setPaymentStatus("verified");
        purchase = purchaseRepository.save(purchase);

        // Auto-mark crop listing as sold
        CropListing listing = purchase.getListing();
        if (listing != null) {
            listing.setStatus("sold");
            cropListingRepository.save(listing);
        }

        notificationService.createNotification(
                purchase.getBuyer().getId(),
                "Payment Receipt Verified",
                "Your payment for " + purchase.getListing().getCropName() + " has been verified."
        );
        notificationService.createNotification(
                purchase.getFarmer().getId(),
                "Payment Verified by Admin",
                "Payment receipt for " + purchase.getListing().getCropName() + " is verified."
        );

        return PurchaseResponseDto.fromEntity(purchase);
    }

    @Override
    @Transactional
    public PurchaseResponseDto flagDispute(Long purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase transaction not found"));

        purchase.setPaymentStatus("disputed");
        purchase = purchaseRepository.save(purchase);

        notificationService.createNotification(
                purchase.getFarmer().getId(),
                "Transaction Disputed",
                "Transaction #" + purchase.getId() + " has been flagged as disputed by admin."
        );

        return PurchaseResponseDto.fromEntity(purchase);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CropListingResponse> getAllListings(String status, String cropName, Pageable pageable) {
        return cropListingRepository.searchListings(status, null, null, null, null, null, null, null, cropName, pageable)
                .map(CropListingResponse::fromEntity);
    }

    @Override
    @Transactional
    public void removeListing(Long listingId) {
        CropListing listing = cropListingRepository.findById(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found"));
        cropListingRepository.delete(listing);
    }

    @Override
    @Transactional
    public CropListingResponse updateListingStatus(Long listingId, String status) {
        Set<String> allowed = Set.of("active", "withdrawn", "expired", "sold");
        if (!allowed.contains(status.toLowerCase())) {
            throw new BadRequestException("Status must be one of: " + allowed);
        }

        CropListing listing = cropListingRepository.findById(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found"));

        listing.setStatus(status.toLowerCase());
        listing = cropListingRepository.save(listing);
        return CropListingResponse.fromEntity(listing);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseResponseDto> getDisputes(Pageable pageable) {
        return purchaseRepository.findByPaymentStatus("disputed", pageable)
                .map(PurchaseResponseDto::fromEntity);
    }

    @Override
    @Transactional
    public PurchaseResponseDto resolveDispute(Long purchaseId, String resolution) {
        Set<String> allowed = Set.of("completed", "refunded");
        if (!allowed.contains(resolution.toLowerCase())) {
            throw new BadRequestException("Resolution must be: completed or refunded");
        }

        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        purchase.setPaymentStatus(resolution.toLowerCase());
        purchase = purchaseRepository.save(purchase);

        notificationService.createNotification(
                purchase.getBuyer().getId(),
                "Dispute Resolved",
                "Dispute on purchase #" + purchase.getId() + " has been resolved as " + resolution
        );

        return PurchaseResponseDto.fromEntity(purchase);
    }
}
