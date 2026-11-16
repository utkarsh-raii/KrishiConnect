package com.krishiconnect.repository;

import com.krishiconnect.entity.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    Page<Purchase> findByBuyerId(Long buyerId, Pageable pageable);

    List<Purchase> findByBuyerIdOrderByCreatedAtDesc(Long buyerId);

    Page<Purchase> findByFarmerId(Long farmerId, Pageable pageable);

    List<Purchase> findByFarmerIdOrderByCreatedAtDesc(Long farmerId);

    Optional<Purchase> findByBuyerIdAndListingId(Long buyerId, Long listingId);

    Optional<Purchase> findByIdAndBuyerId(Long id, Long buyerId);

    Optional<Purchase> findByIdAndFarmerId(Long id, Long farmerId);

    long countByPaymentStatus(String paymentStatus);

    @Query("SELECT COALESCE(SUM(p.totalAmount), 0) FROM Purchase p WHERE p.paymentStatus = 'completed'")
    BigDecimal calculateTotalCompletedRevenue();

    Page<Purchase> findByPaymentStatus(String paymentStatus, Pageable pageable);

    @Query("SELECT p FROM Purchase p WHERE (:paymentStatus IS NULL OR p.paymentStatus = :paymentStatus)")
    Page<Purchase> searchPurchases(@Param("paymentStatus") String paymentStatus, Pageable pageable);

    @Query("SELECT FUNCTION('YEAR', p.createdAt) as year, FUNCTION('MONTH', p.createdAt) as month, " +
           "SUM(p.totalAmount) as total, COUNT(p) as count " +
           "FROM Purchase p WHERE p.paymentStatus = 'completed' AND p.createdAt >= :since " +
           "GROUP BY FUNCTION('YEAR', p.createdAt), FUNCTION('MONTH', p.createdAt) " +
           "ORDER BY year ASC, month ASC")
    List<Object[]> findMonthlySalesSince(@Param("since") LocalDateTime since);
}
