package com.krishiconnect.repository;

import com.krishiconnect.entity.Tree;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreeRepository extends JpaRepository<Tree, Long> {

    List<Tree> findAllByOrderByCreatedAtDesc();

    Page<Tree> findByCategory(String category, Pageable pageable);
}
