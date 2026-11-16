package com.krishiconnect.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SendMessageRequest {

    @NotNull(message = "receiverId is required")
    private Long receiverId;

    private Long cropId;

    @NotBlank(message = "message is required")
    private String message;

    public SendMessageRequest() {}

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public Long getCropId() { return cropId; }
    public void setCropId(Long cropId) { this.cropId = cropId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
