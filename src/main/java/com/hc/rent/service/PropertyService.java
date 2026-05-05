package com.hc.rent.service;

import com.hc.rent.dto.request.PropertySearchRequest;
import com.hc.rent.dto.response.PageResponse;
import com.hc.rent.dto.response.PropertyDetailResponse;
import com.hc.rent.dto.response.PropertySummaryResponse;

public interface PropertyService {

    // Search properties with filters and pagination
    PageResponse<PropertySummaryResponse> searchProperties(PropertySearchRequest request);

    // Get property detail by id
    PropertyDetailResponse getPropertyDetail(Long id);
}