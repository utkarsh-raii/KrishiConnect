package com.krishiconnect.dto;

public class UpdateProfileRequest {
    private String name;
    private String phone;
    private UserProfileDto profile;

    public UpdateProfileRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public UserProfileDto getProfile() { return profile; }
    public void setProfile(UserProfileDto profile) { this.profile = profile; }
}
