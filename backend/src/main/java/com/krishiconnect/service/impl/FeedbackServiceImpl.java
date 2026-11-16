package com.krishiconnect.service.impl;

import com.krishiconnect.dto.FeedbackRequest;
import com.krishiconnect.dto.FeedbackResponseDto;
import com.krishiconnect.entity.Feedback;
import com.krishiconnect.entity.User;
import com.krishiconnect.exception.BadRequestException;
import com.krishiconnect.exception.ResourceNotFoundException;
import com.krishiconnect.repository.FeedbackRepository;
import com.krishiconnect.repository.UserRepository;
import com.krishiconnect.service.FeedbackService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository,
                               UserRepository userRepository) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public FeedbackResponseDto submitFeedback(Long fromUserId, FeedbackRequest req) {
        if (fromUserId.equals(req.getToUser())) {
            throw new BadRequestException("You cannot review yourself");
        }

        User fromUser = userRepository.findById(fromUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        User toUser = userRepository.findById(req.getToUser())
                .orElseThrow(() -> new ResourceNotFoundException("Target user not found"));

        Feedback feedback = new Feedback();
        feedback.setFromUser(fromUser);
        feedback.setToUser(toUser);
        feedback.setRating(req.getRating());
        feedback.setComment(req.getComment());
        feedback.setReferenceType(req.getReferenceType());
        feedback.setReferenceId(req.getReferenceId());
        if (req.getTags() != null && !req.getTags().isEmpty()) {
            feedback.setTags(String.join(",", req.getTags()));
        }
        feedback.setIsPublic(true);

        feedback = feedbackRepository.save(feedback);

        // Update target user's rolling average
        Object[] stats = feedbackRepository.getRatingStatsForUser(toUser.getId());
        if (stats != null && stats.length > 0 && stats[0] != null) {
            Object[] row = (Object[]) stats[0];
            if (row != null && row.length >= 2 && row[0] != null) {
                double avg = ((Number) row[0]).doubleValue();
                long count = ((Number) row[1]).longValue();
                toUser.setAverageRating(BigDecimal.valueOf(avg).setScale(1, RoundingMode.HALF_UP));
                toUser.setTotalRatings((int) count);
                userRepository.save(toUser);
            }
        }

        return FeedbackResponseDto.fromEntity(feedback);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedbackResponseDto> getUserFeedback(Long toUserId, Pageable pageable) {
        return feedbackRepository.findByToUserIdAndIsPublicTrue(toUserId, pageable)
                .map(FeedbackResponseDto::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedbackResponseDto> getMyFeedback(Long fromUserId, Pageable pageable) {
        return feedbackRepository.findByFromUserId(fromUserId, pageable)
                .map(FeedbackResponseDto::fromEntity);
    }

    @Override
    @Transactional
    public void deleteFeedback(Long fromUserId, Long feedbackId) {
        Feedback fb = feedbackRepository.findByIdAndFromUserId(feedbackId, fromUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found"));
        feedbackRepository.delete(fb);
    }
}
