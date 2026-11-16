package com.krishiconnect.controller;

import com.krishiconnect.dto.ReviewRequest;
import com.krishiconnect.dto.ReviewResponseDto;
import com.krishiconnect.security.UserPrincipal;
import com.krishiconnect.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addReview(@AuthenticationPrincipal UserPrincipal currentUser,
                                                         @Valid @RequestBody ReviewRequest req) {
        ReviewResponseDto review = reviewService.addReview(currentUser.getId(), req);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Review added successfully");
        res.put("review", review);
        res.put("data", review);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/{cropId}")
    public ResponseEntity<Map<String, Object>> getCropReviews(@PathVariable Long cropId) {
        List<ReviewResponseDto> reviews = reviewService.getCropReviews(cropId);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("reviews", reviews);
        res.put("data", reviews);
        return ResponseEntity.ok(res);
    }
}
