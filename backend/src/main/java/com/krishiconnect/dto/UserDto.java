package com.krishiconnect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krishiconnect.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class UserDto {
    @JsonProperty("_id")
    private Long id;

    private String name;
    private String email;
    private String phone;
    private String role;
    private UserProfileDto profile;
    private Boolean isVerified;
    private Boolean isBlocked;
    private BigDecimal averageRating;
    private Integer totalRatings;
    private LocalDateTime createdAt;

    public UserDto() {}

    public static UserDto fromEntity(User user) {
        if (user == null) return null;
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setIsVerified(user.getIsVerified());
        dto.setIsBlocked(user.getIsBlocked());
        dto.setAverageRating(user.getAverageRating());
        dto.setTotalRatings(user.getTotalRatings());
        dto.setCreatedAt(user.getCreatedAt());

        UserProfileDto prof = new UserProfileDto(
                user.getLocation(),
                user.getBusinessName(),
                user.getServiceType(),
                user.getVehicleCount(),
                user.getBio(),
                user.getAvatar()
        );
        dto.setProfile(prof);
        return dto;
    }

    // Expose both id and _id for full frontend compatibility
    @JsonProperty("id")
    public Long getRawId() { return id; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public UserProfileDto getProfile() { return profile; }
    public void setProfile(UserProfileDto profile) { this.profile = profile; }

    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean verified) { isVerified = verified; }

    public Boolean getIsBlocked() { return isBlocked; }
    public void setIsBlocked(Boolean blocked) { isBlocked = blocked; }

    public BigDecimal getAverageRating() { return averageRating; }
    public void setAverageRating(BigDecimal averageRating) { this.averageRating = averageRating; }

    public Integer getTotalRatings() { return totalRatings; }
    public void setTotalRatings(Integer totalRatings) { this.totalRatings = totalRatings; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
