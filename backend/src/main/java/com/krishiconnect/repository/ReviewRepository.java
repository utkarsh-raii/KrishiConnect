package com.krishiconnect.repository;

import com.krishiconnect.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByCropIdOrderByCreatedAtDesc(Long cropId);

    Page<Review> findByCropId(Long cropId, Pageable pageable);

    Optional<Review> findByPurchaseIdAndUserId(Long purchaseId, Long userId);

    boolean existsByPurchaseIdAndUserId(Long purchaseId, Long userId);
}
