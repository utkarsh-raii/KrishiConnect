package com.krishiconnect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krishiconnect.entity.ServiceListing;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class ServiceListingResponse {

    @JsonProperty("_id")
    private Long id;

    @JsonProperty("providerId")
    private UserDto provider;

    private ServiceListingRequest.VehicleDetailsDto vehicleDetails;
    private ServiceListingRequest.ManpowerDetailsDto manpowerDetails;
    private BigDecimal pricePerDay;
    private ServiceListingRequest.AvailabilityDto availability;
    private ServiceListingRequest.ServiceAreaDto serviceArea;
    private String serviceType;
    private BigDecimal rating;
    private Integer reviewCount;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ServiceListingResponse() {}

    public static ServiceListingResponse fromEntity(ServiceListing svc) {
        if (svc == null) return null;
        ServiceListingResponse dto = new ServiceListingResponse();
        dto.setId(svc.getId());
        dto.setProvider(UserDto.fromEntity(svc.getProvider()));

        ServiceListingRequest.VehicleDetailsDto vd = new ServiceListingRequest.VehicleDetailsDto();
        vd.setType(svc.getVehicleType());
        vd.setCount(svc.getVehicleCount());
        vd.setCapacity(svc.getVehicleCapacity());
        vd.setRegistrationNo(svc.getVehicleRegistrationNo());
        vd.setDescription(svc.getVehicleDescription());
        dto.setVehicleDetails(vd);

        ServiceListingRequest.ManpowerDetailsDto md = new ServiceListingRequest.ManpowerDetailsDto();
        md.setAvailable(svc.getManpowerAvailable());
        if (svc.getManpowerSkills() != null && !svc.getManpowerSkills().isBlank()) {
            md.setSkills(Arrays.asList(svc.getManpowerSkills().split(",")));
        }
        md.setDescription(svc.getManpowerDescription());
        dto.setManpowerDetails(md);

        dto.setPricePerDay(svc.getPricePerDay());

        ServiceListingRequest.AvailabilityDto av = new ServiceListingRequest.AvailabilityDto();
        av.setIsAvailable(svc.getIsAvailable());
        av.setFrom(svc.getAvailableFrom());
        av.setTo(svc.getAvailableTo());
        av.setNotes(svc.getAvailabilityNotes());
        dto.setAvailability(av);

        ServiceListingRequest.ServiceAreaDto sa = new ServiceListingRequest.ServiceAreaDto();
        if (svc.getServiceStates() != null && !svc.getServiceStates().isBlank()) {
            sa.setStates(Arrays.asList(svc.getServiceStates().split(",")));
        }
        if (svc.getServiceDistricts() != null && !svc.getServiceDistricts().isBlank()) {
            sa.setDistricts(Arrays.asList(svc.getServiceDistricts().split(",")));
        }
        sa.setRadius(svc.getServiceRadius());
        dto.setServiceArea(sa);

        dto.setServiceType(svc.getServiceType());
        dto.setRating(svc.getRating());
        dto.setReviewCount(svc.getReviewCount());
        dto.setIsActive(svc.getIsActive());
        dto.setCreatedAt(svc.getCreatedAt());
        dto.setUpdatedAt(svc.getUpdatedAt());
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserDto getProvider() { return provider; }
    public void setProvider(UserDto provider) { this.provider = provider; }

    public ServiceListingRequest.VehicleDetailsDto getVehicleDetails() { return vehicleDetails; }
    public void setVehicleDetails(ServiceListingRequest.VehicleDetailsDto vehicleDetails) { this.vehicleDetails = vehicleDetails; }

    public ServiceListingRequest.ManpowerDetailsDto getManpowerDetails() { return manpowerDetails; }
    public void setManpowerDetails(ServiceListingRequest.ManpowerDetailsDto manpowerDetails) { this.manpowerDetails = manpowerDetails; }

    public BigDecimal getPricePerDay() { return pricePerDay; }
    public void setPricePerDay(BigDecimal pricePerDay) { this.pricePerDay = pricePerDay; }

    public ServiceListingRequest.AvailabilityDto getAvailability() { return availability; }
    public void setAvailability(ServiceListingRequest.AvailabilityDto availability) { this.availability = availability; }

    public ServiceListingRequest.ServiceAreaDto getServiceArea() { return serviceArea; }
    public void setServiceArea(ServiceListingRequest.ServiceAreaDto serviceArea) { this.serviceArea = serviceArea; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }

    public Integer getReviewCount() { return reviewCount; }
    public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
