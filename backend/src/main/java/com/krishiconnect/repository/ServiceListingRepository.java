package com.krishiconnect.repository;

import com.krishiconnect.entity.ServiceListing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceListingRepository extends JpaRepository<ServiceListing, Long> {

    Page<ServiceListing> findByProviderId(Long providerId, Pageable pageable);

    List<ServiceListing> findByProviderId(Long providerId);

    Page<ServiceListing> findByIsActiveTrue(Pageable pageable);
}
