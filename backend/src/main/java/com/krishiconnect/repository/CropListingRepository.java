package com.krishiconnect.repository;

import com.krishiconnect.entity.CropListing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CropListingRepository extends JpaRepository<CropListing, Long> {

    Page<CropListing> findByFarmerId(Long farmerId, Pageable pageable);

    Page<CropListing> findByFarmerIdAndStatus(Long farmerId, String status, Pageable pageable);

    List<CropListing> findByFarmerId(Long farmerId);

    @Query("SELECT c.id FROM CropListing c WHERE c.farmer.id = :farmerId")
    List<Long> findIdsByFarmerId(@Param("farmerId") Long farmerId);

    long countByStatus(String status);

    @Query("SELECT c FROM CropListing c JOIN FETCH c.farmer WHERE " +
           "(:status IS NULL OR c.status = :status) AND " +
           "(:category IS NULL OR c.category = :category) AND " +
           "(:state IS NULL OR LOWER(c.state) = LOWER(:state)) AND " +
           "(:district IS NULL OR LOWER(c.district) = LOWER(:district)) AND " +
           "(:season IS NULL OR LOWER(c.harvestSeason) = LOWER(:season)) AND " +
           "(:grade IS NULL OR LOWER(c.harvestGrade) = LOWER(:grade)) AND " +
           "(:minPrice IS NULL OR c.priceAmount >= :minPrice) AND " +
           "(:maxPrice IS NULL OR c.priceAmount <= :maxPrice) AND " +
           "(:search IS NULL OR LOWER(c.cropName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.address) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<CropListing> searchListings(@Param("status") String status,
                                     @Param("category") String category,
                                     @Param("state") String state,
                                     @Param("district") String district,
                                     @Param("season") String season,
                                     @Param("grade") String grade,
                                     @Param("minPrice") BigDecimal minPrice,
                                     @Param("maxPrice") BigDecimal maxPrice,
                                     @Param("search") String search,
                                     Pageable pageable);

    @Query("SELECT c FROM CropListing c JOIN FETCH c.farmer WHERE c.status = 'active' ORDER BY c.createdAt DESC")
    List<CropListing> findAllActiveWithFarmer();
}
