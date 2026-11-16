package com.krishiconnect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krishiconnect.entity.Message;

import java.time.LocalDateTime;

public class MessageDto {

    @JsonProperty("_id")
    private Long id;

    private Long senderId;
    private Long receiverId;
    private String senderRole;
    private Long cropId;
    private String message;
    private LocalDateTime createdAt;

    public MessageDto() {}

    public static MessageDto fromEntity(Message m) {
        if (m == null) return null;
        MessageDto dto = new MessageDto();
        dto.setId(m.getId());
        dto.setSenderId(m.getSender().getId());
        dto.setReceiverId(m.getReceiver().getId());
        dto.setSenderRole(m.getSenderRole());
        if (m.getCrop() != null) dto.setCropId(m.getCrop().getId());
        dto.setMessage(m.getMessage());
        dto.setCreatedAt(m.getCreatedAt());
        return dto;
    }

    // Expose both id and _id
    @JsonProperty("id")
    public Long getRawId() { return id; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public String getSenderRole() { return senderRole; }
    public void setSenderRole(String senderRole) { this.senderRole = senderRole; }

    public Long getCropId() { return cropId; }
    public void setCropId(Long cropId) { this.cropId = cropId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
