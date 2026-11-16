package com.krishiconnect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krishiconnect.entity.Notification;

import java.time.LocalDateTime;

public class NotificationDto {

    @JsonProperty("_id")
    private Long id;

    private Long user;
    private String title;
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;

    public NotificationDto() {}

    public static NotificationDto fromEntity(Notification n) {
        if (n == null) return null;
        NotificationDto dto = new NotificationDto();
        dto.setId(n.getId());
        dto.setUser(n.getUser().getId());
        dto.setTitle(n.getTitle());
        dto.setMessage(n.getMessage());
        dto.setIsRead(n.getIsRead());
        dto.setCreatedAt(n.getCreatedAt());
        return dto;
    }

    // Expose both id and _id
    @JsonProperty("id")
    public Long getRawId() { return id; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUser() { return user; }
    public void setUser(Long user) { this.user = user; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean read) { isRead = read; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
