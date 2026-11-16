package com.krishiconnect.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ServiceListingRequest {

    public static class VehicleDetailsDto {
        private String type;
        private Integer count = 1;
        private String capacity;
        private String registrationNo;
        private String description;

        public VehicleDetailsDto() {}
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public Integer getCount() { return count; }
        public void setCount(Integer count) { this.count = count; }
        public String getCapacity() { return capacity; }
        public void setCapacity(String capacity) { this.capacity = capacity; }
        public String getRegistrationNo() { return registrationNo; }
        public void setRegistrationNo(String registrationNo) { this.registrationNo = registrationNo; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class ManpowerDetailsDto {
        private Integer available = 0;
        private List<String> skills;
        private String description;

        public ManpowerDetailsDto() {}
        public Integer getAvailable() { return available; }
        public void setAvailable(Integer available) { this.available = available; }
        public List<String> getSkills() { return skills; }
        public void setSkills(List<String> skills) { this.skills = skills; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class AvailabilityDto {
        private Boolean isAvailable = true;
        private LocalDate from;
        private LocalDate to;
        private String notes;

        public AvailabilityDto() {}
        public Boolean getIsAvailable() { return isAvailable; }
        public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }
        public LocalDate getFrom() { return from; }
        public void setFrom(LocalDate from) { this.from = from; }
        public LocalDate getTo() { return to; }
        public void setTo(LocalDate to) { this.to = to; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }

    public static class ServiceAreaDto {
        private List<String> states;
        private List<String> districts;
        private Integer radius = 50;

        public ServiceAreaDto() {}
        public List<String> getStates() { return states; }
        public void setStates(List<String> states) { this.states = states; }
        public List<String> getDistricts() { return districts; }
        public void setDistricts(List<String> districts) { this.districts = districts; }
        public Integer getRadius() { return radius; }
        public void setRadius(Integer radius) { this.radius = radius; }
    }

    private VehicleDetailsDto vehicleDetails;
    private ManpowerDetailsDto manpowerDetails;
    private AvailabilityDto availability;
    private ServiceAreaDto serviceArea;

    @NotNull(message = "pricePerDay is required")
    private BigDecimal pricePerDay;

    private String serviceType; // "transport", "manpower", "harvester", "cold-storage", "both"
    private Boolean isActive = true;

    public ServiceListingRequest() {}

    public VehicleDetailsDto getVehicleDetails() { return vehicleDetails; }
    public void setVehicleDetails(VehicleDetailsDto vehicleDetails) { this.vehicleDetails = vehicleDetails; }

    public ManpowerDetailsDto getManpowerDetails() { return manpowerDetails; }
    public void setManpowerDetails(ManpowerDetailsDto manpowerDetails) { this.manpowerDetails = manpowerDetails; }

    public AvailabilityDto getAvailability() { return availability; }
    public void setAvailability(AvailabilityDto availability) { this.availability = availability; }

    public ServiceAreaDto getServiceArea() { return serviceArea; }
    public void setServiceArea(ServiceAreaDto serviceArea) { this.serviceArea = serviceArea; }

    public BigDecimal getPricePerDay() { return pricePerDay; }
    public void setPricePerDay(BigDecimal pricePerDay) { this.pricePerDay = pricePerDay; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
