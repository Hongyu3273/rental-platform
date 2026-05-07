package com.hc.rent.dto.request;

import com.hc.rent.entity.Property;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreatePropertyRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 200)
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Property type is required")
    private Property.PropertyType propertyType;

    @NotNull(message = "Bedrooms is required")
    @Min(value = 0, message = "Bedrooms must be at least 0")
    private Integer bedrooms;

    @NotNull(message = "Bathrooms is required")
    @Min(value = 1, message = "Bathrooms must be at least 1")
    private Integer bathrooms;

    private Integer parkingSpaces;
    private Integer floorArea;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Suburb is required")
    private String suburb;

    @NotBlank(message = "Postcode is required")
    @Size(min = 4, max = 4, message = "Postcode must be 4 digits")
    private String postcode;

    @NotBlank(message = "State is required")
    private String state;

    @NotNull(message = "Weekly rent is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Weekly rent must be greater than 0")
    private BigDecimal weeklyRent;

    private BigDecimal bond;

    @NotNull(message = "Available from date is required")
    @FutureOrPresent(message = "Available date must be today or in the future")
    private LocalDate availableFrom;

    private Property.FurnishedType furnishedType;

    private Boolean petsAllowed = false;
    private Boolean airConditioning = false;
    private Boolean dishwasher = false;
    private Boolean pool = false;
    private Boolean gym = false;
    private Boolean balcony = false;
    private Boolean internalLaundry = false;

    private Boolean waterIncluded = false;
    private Boolean electricityIncluded = false;
    private Boolean internetIncluded = false;
}