package com.hc.rent.dto.response;

import com.hc.rent.entity.Property;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PropertyDetailResponse {

    private Long id;
    private String title;
    private String description;
    private String address;
    private String suburb;
    private String postcode;
    private String state;
    private BigDecimal weeklyRent;
    private BigDecimal bond;
    private LocalDate availableFrom;
    private Property.PropertyStatus status;
    private Property.PropertyType propertyType;
    private Integer bedrooms;
    private Integer bathrooms;
    private Integer parkingSpaces;
    private Integer floorArea;
    private Property.FurnishedType furnishedType;

    // Amenities
    private Boolean petsAllowed;
    private Boolean airConditioning;
    private Boolean dishwasher;
    private Boolean pool;
    private Boolean gym;
    private Boolean balcony;
    private Boolean internalLaundry;

    // Bills
    private Boolean waterIncluded;
    private Boolean electricityIncluded;
    private Boolean internetIncluded;

    // Landlord info (safe fields only)
    private Long landlordId;
    private String landlordName;

    // Images
    private List<String> images;

    private LocalDateTime createdAt;
}