package com.krishiconnect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krishiconnect.entity.Review;

import java.time.LocalDateTime;

public class ReviewResponseDto {

    @JsonProperty("_id")
    private Long id;

    private Long cropId;
    private Long purchaseId;
    private UserDto user;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;

    public ReviewResponseDto() {}

    public static ReviewResponseDto fromEntity(Review r) {
        if (r == null) return null;
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.setId(r.getId());
        dto.setCropId(r.getCrop().getId());
        dto.setPurchaseId(r.getPurchase().getId());
        dto.setUser(UserDto.fromEntity(r.getUser()));
        dto.setRating(r.getRating());
        dto.setComment(r.getComment());
        dto.setCreatedAt(r.getCreatedAt());
        return dto;
    }

    // Expose both id and _id
    @JsonProperty("id")
    public Long getRawId() { return id; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCropId() { return cropId; }
    public void setCropId(Long cropId) { this.cropId = cropId; }

    public Long getPurchaseId() { return purchaseId; }
    public void setPurchaseId(Long purchaseId) { this.purchaseId = purchaseId; }

    public UserDto getUser() { return user; }
    public void setUser(UserDto user) { this.user = user; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
