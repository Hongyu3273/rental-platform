package com.hc.rent.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "properties")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Basic information
    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    // Address
    @Column(nullable = false, length = 200)
    private String address;

    @Column(nullable = false, length = 100)
    private String suburb;

    @Column(nullable = false, length = 10)
    private String postcode;

    @Column(nullable = false, length = 10)
    private String state;

    // Pricing
    @Column(name = "weekly_rent", nullable = false, precision = 10, scale = 2)
    private BigDecimal weeklyRent;

    @Column(name = "bond", precision = 10, scale = 2)
    private BigDecimal bond;

    // Availability
    @Column(name = "available_from", nullable = false)
    private LocalDate availableFrom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PropertyStatus status;

    // Property specs
    @Enumerated(EnumType.STRING)
    @Column(name = "property_type", nullable = false, length = 20)
    private PropertyType propertyType;

    @Column(nullable = false)
    private Integer bedrooms;

    @Column(nullable = false)
    private Integer bathrooms;

    @Column(name = "parking_spaces")
    private Integer parkingSpaces;

    @Column(name = "floor_area")
    private Integer floorArea;

    // Furnished status
    @Enumerated(EnumType.STRING)
    @Column(name = "furnished_type", length = 30)
    private FurnishedType furnishedType;

    // Bills included
    @Column(name = "water_included", nullable = false)
    private Boolean waterIncluded = false;

    @Column(name = "electricity_included", nullable = false)
    private Boolean electricityIncluded = false;

    @Column(name = "internet_included", nullable = false)
    private Boolean internetIncluded = false;

    // Amenities
    @Column(name = "pets_allowed", nullable = false)
    private Boolean petsAllowed = false;

    @Column(name = "air_conditioning", nullable = false)
    private Boolean airConditioning = false;

    @Column(name = "dishwasher", nullable = false)
    private Boolean dishwasher = false;

    @Column(name = "pool", nullable = false)
    private Boolean pool = false;

    @Column(name = "gym", nullable = false)
    private Boolean gym = false;

    @Column(name = "balcony", nullable = false)
    private Boolean balcony = false;

    @Column(name = "internal_laundry", nullable = false)
    private Boolean internalLaundry = false;

    // Landlord relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "landlord_id", nullable = false)
    private User landlord;

    // Images (one-to-many)
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PropertyImage> images = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Enums
    public enum PropertyStatus {
        AVAILABLE,
        RENTED,
        WITHDRAWN
    }

    public enum PropertyType {
        APARTMENT,
        HOUSE,
        TOWNHOUSE,
        STUDIO,
        UNIT
    }

    public enum FurnishedType {
        FURNISHED,
        UNFURNISHED,
        PARTIALLY_FURNISHED
    }
}