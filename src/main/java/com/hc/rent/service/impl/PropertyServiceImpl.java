package com.hc.rent.service.impl;

import com.hc.rent.dto.request.PropertySearchRequest;
import com.hc.rent.dto.response.PageResponse;
import com.hc.rent.dto.response.PropertyDetailResponse;
import com.hc.rent.dto.response.PropertySummaryResponse;
import com.hc.rent.entity.Property;
import com.hc.rent.entity.PropertyImage;
import com.hc.rent.exception.BusinessException;
import com.hc.rent.repository.PropertyRepository;
import com.hc.rent.service.PropertyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;

    @Override
    public PageResponse<PropertySummaryResponse> searchProperties(PropertySearchRequest request) {
        log.info("Searching properties with filters: suburb={}, maxRent={}, bedrooms={}",
                request.getSuburb(), request.getMaxRent(), request.getBedrooms());

        // Build pageable with sort
        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                buildSort(request.getSort())
        );

        // Query database
        Page<Property> propertyPage = propertyRepository.searchProperties(
                request.getSuburb(),
                request.getMinRent(),
                request.getMaxRent(),
                request.getBedrooms(),
                request.getPropertyType(),
                request.getPetsAllowed(),
                request.getFurnished(),
                request.getParking(),
                pageable
        );

        // Convert to response DTO
        Page<PropertySummaryResponse> responsePage = propertyPage
                .map(this::buildSummaryResponse);

        return PageResponse.of(responsePage);
    }


    public PropertyDetailResponse getPropertyDetail(Long id) {
        log.info("Fetching property detail for id: {}", id);

        Property property = propertyRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new BusinessException(404, "Property not found"));

        return buildDetailResponse(property);
    }

    // Build sort from string
    private Sort buildSort(String sort) {
        return switch (sort) {
            case "price_asc"  -> Sort.by("weeklyRent").ascending();
            case "price_desc" -> Sort.by("weeklyRent").descending();
            default           -> Sort.by("createdAt").descending();
        };
    }

    // Convert Property to PropertySummaryResponse
    private PropertySummaryResponse buildSummaryResponse(Property property) {
        String coverImageUrl = property.getImages().stream()
                .filter(PropertyImage::getIsCover)
                .findFirst()
                .map(PropertyImage::getImageUrl)
                .orElse(null);

        return PropertySummaryResponse.builder()
                .id(property.getId())
                .title(property.getTitle())
                .suburb(property.getSuburb())
                .address(property.getAddress())
                .weeklyRent(property.getWeeklyRent())
                .bedrooms(property.getBedrooms())
                .bathrooms(property.getBathrooms())
                .parkingSpaces(property.getParkingSpaces())
                .propertyType(property.getPropertyType())
                .status(property.getStatus())
                .furnishedType(property.getFurnishedType())
                .petsAllowed(property.getPetsAllowed())
                .coverImageUrl(coverImageUrl)
                .availableFrom(property.getAvailableFrom())
                .build();
    }

    // Convert Property to PropertyDetailResponse
    private PropertyDetailResponse buildDetailResponse(Property property) {
        return PropertyDetailResponse.builder()
                .id(property.getId())
                .title(property.getTitle())
                .description(property.getDescription())
                .address(property.getAddress())
                .suburb(property.getSuburb())
                .postcode(property.getPostcode())
                .state(property.getState())
                .weeklyRent(property.getWeeklyRent())
                .bond(property.getBond())
                .availableFrom(property.getAvailableFrom())
                .status(property.getStatus())
                .propertyType(property.getPropertyType())
                .bedrooms(property.getBedrooms())
                .bathrooms(property.getBathrooms())
                .parkingSpaces(property.getParkingSpaces())
                .floorArea(property.getFloorArea())
                .furnishedType(property.getFurnishedType())
                .petsAllowed(property.getPetsAllowed())
                .airConditioning(property.getAirConditioning())
                .dishwasher(property.getDishwasher())
                .pool(property.getPool())
                .gym(property.getGym())
                .balcony(property.getBalcony())
                .internalLaundry(property.getInternalLaundry())
                .waterIncluded(property.getWaterIncluded())
                .electricityIncluded(property.getElectricityIncluded())
                .internetIncluded(property.getInternetIncluded())
                .landlordId(property.getLandlord().getId())
                .landlordName(property.getLandlord().getFirstName()
                        + " " + property.getLandlord().getLastName())
                .images(property.getImages().stream()
                        .map(PropertyImage::getImageUrl)
                        .toList())
                .createdAt(property.getCreatedAt())
                .build();
    }
}