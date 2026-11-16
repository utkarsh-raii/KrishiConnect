package com.krishiconnect.service;

import com.krishiconnect.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProviderService {

    ServiceListingResponse postService(Long providerId, ServiceListingRequest req);

    Page<ServiceListingResponse> getMyServices(Long providerId, Pageable pageable);

    ServiceListingResponse updateService(Long providerId, Long serviceId, ServiceListingRequest req);

    void deleteService(Long providerId, Long serviceId);

    Page<HarvestRequirementResponse> getFarmerRequests(String cropType, String vehicleType, String state, String urgency, Pageable pageable);

    BidResponseDto submitBid(Long providerId, SubmitBidRequest req);

    Page<BidResponseDto> getMyBids(Long providerId, String status, Pageable pageable);

    BidResponseDto withdrawBid(Long providerId, Long bidId);

    Page<BidResponseDto> getBookings(Long providerId, Pageable pageable);

    BidResponseDto completeBooking(Long providerId, Long bidId);
}
