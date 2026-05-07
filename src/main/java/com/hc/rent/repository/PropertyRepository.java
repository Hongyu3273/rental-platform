package com.hc.rent.repository;

import com.hc.rent.entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    // Fetch property detail with landlord and images in one query
    @Query("SELECT p FROM Property p " +
            "LEFT JOIN FETCH p.landlord " +
            "LEFT JOIN FETCH p.images " +
            "WHERE p.id = :id")
    Optional<Property> findByIdWithDetails(@Param("id") Long id);

    // Search with filters and pagination
    @Query("SELECT p FROM Property p " +
            "LEFT JOIN FETCH p.landlord " +
            "WHERE p.status = 'AVAILABLE' " +
            "AND (:suburb IS NULL OR LOWER(p.suburb) LIKE LOWER(CONCAT('%', CAST(:suburb AS string), '%'))) " +
            "AND (:minRent IS NULL OR p.weeklyRent >= :minRent) " +
            "AND (:maxRent IS NULL OR p.weeklyRent <= :maxRent) " +
            "AND (:bedrooms IS NULL OR p.bedrooms = :bedrooms) " +
            "AND (:propertyType IS NULL OR p.propertyType = :propertyType) " +
            "AND (:petsAllowed IS NULL OR p.petsAllowed = :petsAllowed) " +
            "AND (:furnished IS NULL OR p.furnishedType = :furnished) " +
            "AND (:parking IS NULL OR p.parkingSpaces >= :parking)")
    Page<Property> searchProperties(
            @Param("suburb") String suburb,
            @Param("minRent") BigDecimal minRent,
            @Param("maxRent") BigDecimal maxRent,
            @Param("bedrooms") Integer bedrooms,
            @Param("propertyType") Property.PropertyType propertyType,
            @Param("petsAllowed") Boolean petsAllowed,
            @Param("furnished") Property.FurnishedType furnished,
            @Param("parking") Integer parking,
            Pageable pageable
    );
}