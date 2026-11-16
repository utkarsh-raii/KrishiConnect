package com.krishiconnect.service.impl;

import com.krishiconnect.dto.*;
import com.krishiconnect.entity.BidOffer;
import com.krishiconnect.entity.HarvestRequirement;
import com.krishiconnect.entity.ServiceListing;
import com.krishiconnect.entity.User;
import com.krishiconnect.exception.BadRequestException;
import com.krishiconnect.exception.ForbiddenException;
import com.krishiconnect.exception.ResourceNotFoundException;
import com.krishiconnect.repository.BidOfferRepository;
import com.krishiconnect.repository.HarvestRequirementRepository;
import com.krishiconnect.repository.ServiceListingRepository;
import com.krishiconnect.repository.UserRepository;
import com.krishiconnect.service.NotificationService;
import com.krishiconnect.service.ProviderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProviderServiceImpl implements ProviderService {

    private final ServiceListingRepository serviceListingRepository;
    private final BidOfferRepository bidOfferRepository;
    private final HarvestRequirementRepository requirementRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public ProviderServiceImpl(ServiceListingRepository serviceListingRepository,
                               BidOfferRepository bidOfferRepository,
                               HarvestRequirementRepository requirementRepository,
                               UserRepository userRepository,
                               NotificationService notificationService) {
        this.serviceListingRepository = serviceListingRepository;
        this.bidOfferRepository = bidOfferRepository;
        this.requirementRepository = requirementRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional
    public ServiceListingResponse postService(Long providerId, ServiceListingRequest req) {
        User provider = userRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));

        ServiceListing svc = new ServiceListing();
        svc.setProvider(provider);
        populateServiceEntity(svc, req);

        svc = serviceListingRepository.save(svc);
        return ServiceListingResponse.fromEntity(svc);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ServiceListingResponse> getMyServices(Long providerId, Pageable pageable) {
        return serviceListingRepository.findByProviderId(providerId, pageable)
                .map(ServiceListingResponse::fromEntity);
    }

    @Override
    @Transactional
    public ServiceListingResponse updateService(Long providerId, Long serviceId, ServiceListingRequest req) {
        ServiceListing svc = serviceListingRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        if (!svc.getProvider().getId().equals(providerId)) {
            throw new ForbiddenException("You are not authorized to update this service");
        }

        populateServiceEntity(svc, req);
        if (req.getIsActive() != null) {
            svc.setIsActive(req.getIsActive());
        }

        svc = serviceListingRepository.save(svc);
        return ServiceListingResponse.fromEntity(svc);
    }

    @Override
    @Transactional
    public void deleteService(Long providerId, Long serviceId) {
        ServiceListing svc = serviceListingRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        if (!svc.getProvider().getId().equals(providerId)) {
            throw new ForbiddenException("You are not authorized to delete this service");
        }

        serviceListingRepository.delete(svc);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HarvestRequirementResponse> getFarmerRequests(String cropType, String vehicleType, String state, String urgency, Pageable pageable) {
        return requirementRepository.searchRequirements("open", cropType, vehicleType, urgency, state, pageable)
                .map(HarvestRequirementResponse::fromEntity);
    }

    @Override
    @Transactional
    public BidResponseDto submitBid(Long providerId, SubmitBidRequest req) {
        User provider = userRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));

        HarvestRequirement reqDoc = requirementRepository.findById(req.getRequirementId())
                .orElseThrow(() -> new ResourceNotFoundException("Harvest requirement not found"));

        if (!"open".equalsIgnoreCase(reqDoc.getStatus())) {
            throw new BadRequestException("This requirement is no longer accepting bids");
        }

        if (bidOfferRepository.findByProviderIdAndRequirementId(providerId, reqDoc.getId()).isPresent()) {
            throw new BadRequestException("You have already submitted a bid for this requirement");
        }

        ServiceListing service = null;
        if (req.getServiceId() != null) {
            service = serviceListingRepository.findById(req.getServiceId()).orElse(null);
        }

        BidOffer bid = new BidOffer();
        bid.setProvider(provider);
        bid.setFarmer(reqDoc.getFarmer());
        bid.setRequirement(reqDoc);
        bid.setService(service);
        bid.setPrice(req.getPrice());
        bid.setPriceType(req.getPriceType() != null ? req.getPriceType() : "per-day");
        bid.setMessage(req.getMessage());
        bid.setStatus("pending");

        bid = bidOfferRepository.save(bid);

        notificationService.createNotification(
                reqDoc.getFarmer().getId(),
                "New Bid Received",
                provider.getName() + " submitted a bid of ₹" + req.getPrice() + " on your " + reqDoc.getCropType() + " requirement"
        );

        return BidResponseDto.fromEntity(bid);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BidResponseDto> getMyBids(Long providerId, String status, Pageable pageable) {
        if (status != null && !status.isBlank()) {
            return bidOfferRepository.findByProviderIdAndStatus(providerId, status, pageable)
                    .map(BidResponseDto::fromEntity);
        }
        return bidOfferRepository.findByProviderId(providerId, pageable)
                .map(BidResponseDto::fromEntity);
    }

    @Override
    @Transactional
    public BidResponseDto withdrawBid(Long providerId, Long bidId) {
        BidOffer bid = bidOfferRepository.findById(bidId)
                .orElseThrow(() -> new ResourceNotFoundException("Bid not found"));

        if (!bid.getProvider().getId().equals(providerId)) {
            throw new ForbiddenException("You are not authorized to withdraw this bid");
        }

        if (!"pending".equalsIgnoreCase(bid.getStatus())) {
            throw new BadRequestException("Cannot withdraw a bid that is " + bid.getStatus());
        }

        bid.setStatus("withdrawn");
        bid = bidOfferRepository.save(bid);
        return BidResponseDto.fromEntity(bid);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BidResponseDto> getBookings(Long providerId, Pageable pageable) {
        return bidOfferRepository.findByProviderIdAndStatusIn(providerId, List.of("accepted", "completed"), pageable)
                .map(BidResponseDto::fromEntity);
    }

    @Override
    @Transactional
    public BidResponseDto completeBooking(Long providerId, Long bidId) {
        BidOffer bid = bidOfferRepository.findById(bidId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!bid.getProvider().getId().equals(providerId)) {
            throw new ForbiddenException("You are not authorized to complete this booking");
        }

        if (!"accepted".equalsIgnoreCase(bid.getStatus())) {
            throw new BadRequestException("Booking not found or not in accepted status");
        }

        bid.setStatus("completed");
        bid = bidOfferRepository.save(bid);

        HarvestRequirement reqDoc = bid.getRequirement();
        reqDoc.setStatus("completed");
        requirementRepository.save(reqDoc);

        notificationService.createNotification(
                bid.getFarmer().getId(),
                "Service Booking Completed",
                bid.getProvider().getName() + " marked the harvest service booking as completed"
        );

        return BidResponseDto.fromEntity(bid);
    }

    private void populateServiceEntity(ServiceListing svc, ServiceListingRequest req) {
        if (req.getVehicleDetails() != null) {
            svc.setVehicleType(req.getVehicleDetails().getType());
            svc.setVehicleCount(req.getVehicleDetails().getCount());
            svc.setVehicleCapacity(req.getVehicleDetails().getCapacity());
            svc.setVehicleRegistrationNo(req.getVehicleDetails().getRegistrationNo());
            svc.setVehicleDescription(req.getVehicleDetails().getDescription());
        }
        if (req.getManpowerDetails() != null) {
            svc.setManpowerAvailable(req.getManpowerDetails().getAvailable());
            if (req.getManpowerDetails().getSkills() != null) {
                svc.setManpowerSkills(String.join(",", req.getManpowerDetails().getSkills()));
            }
            svc.setManpowerDescription(req.getManpowerDetails().getDescription());
        }
        if (req.getPricePerDay() != null) {
            svc.setPricePerDay(req.getPricePerDay());
        }
        if (req.getAvailability() != null) {
            svc.setIsAvailable(req.getAvailability().getIsAvailable());
            svc.setAvailableFrom(req.getAvailability().getFrom());
            svc.setAvailableTo(req.getAvailability().getTo());
            svc.setAvailabilityNotes(req.getAvailability().getNotes());
        }
        if (req.getServiceArea() != null) {
            if (req.getServiceArea().getStates() != null) {
                svc.setServiceStates(String.join(",", req.getServiceArea().getStates()));
            }
            if (req.getServiceArea().getDistricts() != null) {
                svc.setServiceDistricts(String.join(",", req.getServiceArea().getDistricts()));
            }
            svc.setServiceRadius(req.getServiceArea().getRadius());
        }
        if (req.getServiceType() != null) {
            svc.setServiceType(req.getServiceType());
        }
    }
}
