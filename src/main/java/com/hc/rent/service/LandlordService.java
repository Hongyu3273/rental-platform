package com.hc.rent.service;

import com.hc.rent.dto.request.CreatePropertyRequest;
import com.hc.rent.dto.response.PropertyDetailResponse;

public interface LandlordService {

    // Create a new property listing
    PropertyDetailResponse createProperty(CreatePropertyRequest request);
}