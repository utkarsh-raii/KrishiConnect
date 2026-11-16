package com.krishiconnect.repository;

import com.krishiconnect.entity.TreeRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreeRequestRepository extends JpaRepository<TreeRequest, Long> {

    List<TreeRequest> findByUserIdOrderByCreatedAtDesc(Long userId);

    Page<TreeRequest> findByUserId(Long userId, Pageable pageable);

    List<TreeRequest> findAllByOrderByCreatedAtDesc();

    Page<TreeRequest> findAll(Pageable pageable);
}
