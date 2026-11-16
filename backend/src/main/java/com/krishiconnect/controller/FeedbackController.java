package com.krishiconnect.controller;

import com.krishiconnect.dto.FeedbackRequest;
import com.krishiconnect.dto.FeedbackResponseDto;
import com.krishiconnect.dto.PaginationMeta;
import com.krishiconnect.security.UserPrincipal;
import com.krishiconnect.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> submitFeedback(@AuthenticationPrincipal UserPrincipal currentUser,
                                                              @Valid @RequestBody FeedbackRequest req) {
        FeedbackResponseDto feedback = feedbackService.submitFeedback(currentUser.getId(), req);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Feedback submitted successfully");
        res.put("feedback", feedback);
        res.put("data", feedback);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserFeedback(@PathVariable Long userId,
                                                               @RequestParam(defaultValue = "1") int page,
                                                               @RequestParam(defaultValue = "10") int limit) {
        int pageIndex = Math.max(0, page - 1);
        Page<FeedbackResponseDto> pageResult = feedbackService.getUserFeedback(
                userId,
                PageRequest.of(pageIndex, limit, Sort.by(Sort.Direction.DESC, "createdAt"))
        );
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("data", pageResult.getContent());
        res.put("pagination", new PaginationMeta(pageResult.getTotalElements(), page, limit));
        return ResponseEntity.ok(res);
    }

    @GetMapping("/my")
    public ResponseEntity<Map<String, Object>> getMyFeedback(@AuthenticationPrincipal UserPrincipal currentUser,
                                                             @RequestParam(defaultValue = "1") int page,
                                                             @RequestParam(defaultValue = "10") int limit) {
        int pageIndex = Math.max(0, page - 1);
        Page<FeedbackResponseDto> pageResult = feedbackService.getMyFeedback(
                currentUser.getId(),
                PageRequest.of(pageIndex, limit, Sort.by(Sort.Direction.DESC, "createdAt"))
        );
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("data", pageResult.getContent());
        res.put("pagination", new PaginationMeta(pageResult.getTotalElements(), page, limit));
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteFeedback(@AuthenticationPrincipal UserPrincipal currentUser,
                                                              @PathVariable Long id) {
        feedbackService.deleteFeedback(currentUser.getId(), id);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Feedback deleted");
        return ResponseEntity.ok(res);
    }
}
