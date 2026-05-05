package com.hc.rent.dto.response;

import com.hc.rent.entity.Property;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class PropertySummaryResponse {

    private Long id;
    private String title;
    private String suburb;
    private String address;
    private BigDecimal weeklyRent;
    private Integer bedrooms;
    private Integer bathrooms;
    private Integer parkingSpaces;
    private Property.PropertyType propertyType;
    private Property.PropertyStatus status;
    private Property.FurnishedType furnishedType;
    private Boolean petsAllowed;
    private String coverImageUrl;
    private LocalDate availableFrom;
}