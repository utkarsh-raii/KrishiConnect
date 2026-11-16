package com.krishiconnect.repository;

import com.krishiconnect.entity.HarvestRequirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HarvestRequirementRepository extends JpaRepository<HarvestRequirement, Long> {

    Page<HarvestRequirement> findByFarmerId(Long farmerId, Pageable pageable);

    Page<HarvestRequirement> findByFarmerIdAndStatus(Long farmerId, String status, Pageable pageable);

    @Query("SELECT r.id FROM HarvestRequirement r WHERE r.farmer.id = :farmerId")
    List<Long> findIdsByFarmerId(@Param("farmerId") Long farmerId);

    long countByStatus(String status);

    @Query("SELECT r FROM HarvestRequirement r JOIN FETCH r.farmer WHERE " +
           "(:status IS NULL OR r.status = :status) AND " +
           "(:cropType IS NULL OR LOWER(r.cropType) LIKE LOWER(CONCAT('%', :cropType, '%'))) AND " +
           "(:vehicleType IS NULL OR r.vehicleType = :vehicleType) AND " +
           "(:urgency IS NULL OR r.urgency = :urgency) AND " +
           "(:state IS NULL OR LOWER(r.state) LIKE LOWER(CONCAT('%', :state, '%')))")
    Page<HarvestRequirement> searchRequirements(@Param("status") String status,
                                                @Param("cropType") String cropType,
                                                @Param("vehicleType") String vehicleType,
                                                @Param("urgency") String urgency,
                                                @Param("state") String state,
                                                Pageable pageable);
}
