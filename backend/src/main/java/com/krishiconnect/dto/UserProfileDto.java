package com.krishiconnect.dto;

public class UserProfileDto {
    private String location;
    private String businessName;
    private String serviceType;
    private Integer vehicleCount = 0;
    private String bio;
    private String avatar;

    public UserProfileDto() {}

    public UserProfileDto(String location, String businessName, String serviceType, Integer vehicleCount, String bio, String avatar) {
        this.location = location;
        this.businessName = businessName;
        this.serviceType = serviceType;
        this.vehicleCount = vehicleCount;
        this.bio = bio;
        this.avatar = avatar;
    }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public Integer getVehicleCount() { return vehicleCount; }
    public void setVehicleCount(Integer vehicleCount) { this.vehicleCount = vehicleCount; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
}
