package com.krishiconnect.service;

import com.krishiconnect.dto.FeedbackRequest;
import com.krishiconnect.dto.FeedbackResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedbackService {

    FeedbackResponseDto submitFeedback(Long fromUserId, FeedbackRequest req);

    Page<FeedbackResponseDto> getUserFeedback(Long toUserId, Pageable pageable);

    Page<FeedbackResponseDto> getMyFeedback(Long fromUserId, Pageable pageable);

    void deleteFeedback(Long fromUserId, Long feedbackId);
}
