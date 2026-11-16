package com.krishiconnect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krishiconnect.entity.Feedback;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class FeedbackResponseDto {

    @JsonProperty("_id")
    private Long id;

    private UserDto fromUser;
    private UserDto toUser;
    private String referenceType;
    private Long referenceId;
    private Integer rating;
    private String comment;
    private List<String> tags;
    private Boolean isPublic;
    private LocalDateTime createdAt;

    public FeedbackResponseDto() {}

    public static FeedbackResponseDto fromEntity(Feedback fb) {
        if (fb == null) return null;
        FeedbackResponseDto dto = new FeedbackResponseDto();
        dto.setId(fb.getId());
        dto.setFromUser(UserDto.fromEntity(fb.getFromUser()));
        dto.setToUser(UserDto.fromEntity(fb.getToUser()));
        dto.setReferenceType(fb.getReferenceType());
        dto.setReferenceId(fb.getReferenceId());
        dto.setRating(fb.getRating());
        dto.setComment(fb.getComment());
        if (fb.getTags() != null && !fb.getTags().isBlank()) {
            dto.setTags(Arrays.asList(fb.getTags().split(",")));
        }
        dto.setIsPublic(fb.getIsPublic());
        dto.setCreatedAt(fb.getCreatedAt());
        return dto;
    }

    // Expose both id and _id
    @JsonProperty("id")
    public Long getRawId() { return id; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserDto getFromUser() { return fromUser; }
    public void setFromUser(UserDto fromUser) { this.fromUser = fromUser; }

    public UserDto getToUser() { return toUser; }
    public void setToUser(UserDto toUser) { this.toUser = toUser; }

    public String getReferenceType() { return referenceType; }
    public void setReferenceType(String referenceType) { this.referenceType = referenceType; }

    public Long getReferenceId() { return referenceId; }
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public Boolean getIsPublic() { return isPublic; }
    public void setIsPublic(Boolean aPublic) { isPublic = aPublic; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
