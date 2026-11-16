package com.krishiconnect.repository;

import com.krishiconnect.entity.CropOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CropOfferRepository extends JpaRepository<CropOffer, Long> {

    Page<CropOffer> findByListingIdIn(Collection<Long> listingIds, Pageable pageable);

    Page<CropOffer> findByListingIdInAndStatus(Collection<Long> listingIds, String status, Pageable pageable);

    Page<CropOffer> findByBuyerId(Long buyerId, Pageable pageable);

    Page<CropOffer> findByBuyerIdAndStatus(Long buyerId, String status, Pageable pageable);

    List<CropOffer> findByBuyerIdOrderByCreatedAtDesc(Long buyerId);

    Optional<CropOffer> findByIdAndBuyerIdAndStatus(Long id, Long buyerId, String status);
}
