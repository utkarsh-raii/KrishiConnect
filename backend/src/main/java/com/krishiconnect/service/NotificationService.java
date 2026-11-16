package com.krishiconnect.service;

import com.krishiconnect.dto.NotificationDto;
import com.krishiconnect.dto.PaginationMeta;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {

    void createNotification(Long userId, String title, String message);

    List<NotificationDto> getUserNotifications(Long userId);

    NotificationDto markAsRead(Long userId, Long notificationId);

    long getUnreadCount(Long userId);
}
