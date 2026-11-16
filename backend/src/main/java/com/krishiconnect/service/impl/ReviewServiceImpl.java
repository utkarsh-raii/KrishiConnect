package com.krishiconnect.service.impl;

import com.krishiconnect.dto.ReviewRequest;
import com.krishiconnect.dto.ReviewResponseDto;
import com.krishiconnect.entity.CropListing;
import com.krishiconnect.entity.Purchase;
import com.krishiconnect.entity.Review;
import com.krishiconnect.entity.User;
import com.krishiconnect.exception.ForbiddenException;
import com.krishiconnect.exception.ResourceNotFoundException;
import com.krishiconnect.repository.CropListingRepository;
import com.krishiconnect.repository.PurchaseRepository;
import com.krishiconnect.repository.ReviewRepository;
import com.krishiconnect.repository.UserRepository;
import com.krishiconnect.service.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final PurchaseRepository purchaseRepository;
    private final CropListingRepository cropListingRepository;
    private final UserRepository userRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             PurchaseRepository purchaseRepository,
                             CropListingRepository cropListingRepository,
                             UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.purchaseRepository = purchaseRepository;
        this.cropListingRepository = cropListingRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ReviewResponseDto addReview(Long userId, ReviewRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Purchase purchase = purchaseRepository.findById(req.getPurchaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found"));

        if (!purchase.getBuyer().getId().equals(userId) || !purchase.getListing().getId().equals(req.getCropId())) {
            throw new ForbiddenException("You can only review crops you have purchased");
        }

        CropListing crop = cropListingRepository.findById(req.getCropId())
                .orElseThrow(() -> new ResourceNotFoundException("Crop listing not found"));

        Review review = new Review();
        review.setUser(user);
        review.setCrop(crop);
        review.setPurchase(purchase);
        review.setRating(req.getRating());
        review.setComment(req.getComment());

        review = reviewRepository.save(review);
        return ReviewResponseDto.fromEntity(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getCropReviews(Long cropId) {
        return reviewRepository.findByCropIdOrderByCreatedAtDesc(cropId)
                .stream()
                .map(ReviewResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}
