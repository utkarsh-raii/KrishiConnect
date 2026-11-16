package com.krishiconnect.service;

import com.krishiconnect.dto.ReviewRequest;
import com.krishiconnect.dto.ReviewResponseDto;

import java.util.List;

public interface ReviewService {

    ReviewResponseDto addReview(Long userId, ReviewRequest req);

    List<ReviewResponseDto> getCropReviews(Long cropId);
}
