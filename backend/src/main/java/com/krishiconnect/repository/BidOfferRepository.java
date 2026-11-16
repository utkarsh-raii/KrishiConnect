package com.krishiconnect.repository;

import com.krishiconnect.entity.BidOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BidOfferRepository extends JpaRepository<BidOffer, Long> {

    Optional<BidOffer> findByProviderIdAndRequirementId(Long providerId, Long requirementId);

    Page<BidOffer> findByRequirementIdIn(Collection<Long> requirementIds, Pageable pageable);

    Page<BidOffer> findByRequirementIdInAndStatus(Collection<Long> requirementIds, String status, Pageable pageable);

    Page<BidOffer> findByProviderId(Long providerId, Pageable pageable);

    Page<BidOffer> findByProviderIdAndStatus(Long providerId, String status, Pageable pageable);

    Page<BidOffer> findByProviderIdAndStatusIn(Long providerId, Collection<String> statuses, Pageable pageable);

    long countByStatus(String status);

    @Modifying
    @Query("UPDATE BidOffer b SET b.status = 'rejected' WHERE b.requirement.id = :requirementId AND b.id != :acceptedBidId AND b.status = 'pending'")
    void rejectOtherPendingBids(@Param("requirementId") Long requirementId, @Param("acceptedBidId") Long acceptedBidId);
}
