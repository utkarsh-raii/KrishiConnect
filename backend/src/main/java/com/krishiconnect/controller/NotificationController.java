package com.krishiconnect.controller;

import com.krishiconnect.dto.NotificationDto;
import com.krishiconnect.security.UserPrincipal;
import com.krishiconnect.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getNotifications(@AuthenticationPrincipal UserPrincipal currentUser) {
        List<NotificationDto> notifications = notificationService.getUserNotifications(currentUser.getId());
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("notifications", notifications);
        res.put("data", notifications);
        return ResponseEntity.ok(res);
    }

    @RequestMapping(value = "/read/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<Map<String, Object>> markAsRead(@AuthenticationPrincipal UserPrincipal currentUser,
                                                          @PathVariable Long id) {
        NotificationDto notification = notificationService.markAsRead(currentUser.getId(), id);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("notification", notification);
        res.put("data", notification);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Object>> getUnreadCount(@AuthenticationPrincipal UserPrincipal currentUser) {
        long count = notificationService.getUnreadCount(currentUser.getId());
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("unreadCount", count);
        return ResponseEntity.ok(res);
    }
}
