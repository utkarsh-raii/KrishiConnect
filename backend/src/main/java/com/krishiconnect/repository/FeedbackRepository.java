package com.krishiconnect.repository;

import com.krishiconnect.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    Page<Feedback> findByToUserIdAndIsPublicTrue(Long toUserId, Pageable pageable);

    Page<Feedback> findByFromUserId(Long fromUserId, Pageable pageable);

    Optional<Feedback> findByIdAndFromUserId(Long id, Long fromUserId);

    @Query("SELECT AVG(f.rating), COUNT(f) FROM Feedback f WHERE f.toUser.id = :toUserId")
    Object[] getRatingStatsForUser(@Param("toUserId") Long toUserId);
}
