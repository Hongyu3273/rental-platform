package com.hc.rent.service.impl;

import com.hc.rent.common.UserContext;
import com.hc.rent.dto.request.CreatePropertyRequest;
import com.hc.rent.dto.response.PropertyDetailResponse;
import com.hc.rent.entity.Property;
import com.hc.rent.entity.User;
import com.hc.rent.exception.BusinessException;
import com.hc.rent.repository.PropertyRepository;
import com.hc.rent.repository.UserRepository;
import com.hc.rent.service.LandlordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class LandlordServiceImpl implements LandlordService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public PropertyDetailResponse createProperty(CreatePropertyRequest request) {
        log.info("Creating property for landlord: {}", UserContext.getUserId());

        Long landlordId = UserContext.getUserId();
        User landlord = userRepository.findById(landlordId)
                .orElseThrow(() -> new BusinessException(404, "Landlord not found"));

        // Verify user is a landlord
        if (landlord.getRole() != User.Role.LANDLORD) {
            throw new BusinessException(403, "Only landlords can create properties");
        }

        // Calculate bond if not provided (default 4 weeks rent)
        BigDecimal bond = request.getBond() != null
                ? request.getBond()
                : request.getWeeklyRent().multiply(BigDecimal.valueOf(4));

        // Build property entity
        Property property = Property.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .address(request.getAddress())
                .suburb(request.getSuburb())
                .postcode(request.getPostcode())
                .state(request.getState())
                .weeklyRent(request.getWeeklyRent())
                .bond(bond)
                .availableFrom(request.getAvailableFrom())
                .status(Property.PropertyStatus.AVAILABLE)
                .propertyType(request.getPropertyType())
                .bedrooms(request.getBedrooms())
                .bathrooms(request.getBathrooms())
                .parkingSpaces(request.getParkingSpaces())
                .floorArea(request.getFloorArea())
                .furnishedType(request.getFurnishedType())
                .petsAllowed(request.getPetsAllowed())
                .airConditioning(request.getAirConditioning())
                .dishwasher(request.getDishwasher())
                .pool(request.getPool())
                .gym(request.getGym())
                .balcony(request.getBalcony())
                .internalLaundry(request.getInternalLaundry())
                .waterIncluded(request.getWaterIncluded())
                .electricityIncluded(request.getElectricityIncluded())
                .internetIncluded(request.getInternetIncluded())
                .landlord(landlord)
                .images(new ArrayList<>())
                .build();

        propertyRepository.save(property);
        log.info("Property created successfully with id: {}", property.getId());

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
                .landlordId(landlord.getId())
                .landlordName(landlord.getFirstName() + " " + landlord.getLastName())
                .images(new ArrayList<>())
                .createdAt(property.getCreatedAt())
                .build();
    }
}