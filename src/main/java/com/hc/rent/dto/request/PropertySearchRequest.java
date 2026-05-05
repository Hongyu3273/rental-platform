package com.hc.rent.dto.request;

import com.hc.rent.entity.Property;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PropertySearchRequest {

    private String suburb;

    private BigDecimal minRent;

    private BigDecimal maxRent;

    private Integer bedrooms;

    private Property.PropertyType propertyType;

    private Boolean petsAllowed;

    private Property.FurnishedType furnished;

    private Integer parking;

    // Pagination
    private Integer page = 0;

    private Integer size = 9;

    // Sort: newest / price_asc / price_desc
    private String sort = "newest";
}