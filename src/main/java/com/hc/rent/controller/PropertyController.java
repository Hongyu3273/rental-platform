package com.hc.rent.controller;

import com.hc.rent.common.Result;
import com.hc.rent.dto.request.PropertySearchRequest;
import com.hc.rent.dto.response.PageResponse;
import com.hc.rent.dto.response.PropertyDetailResponse;
import com.hc.rent.dto.response.PropertySummaryResponse;
import com.hc.rent.service.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Property", description = "Property search and detail endpoints")
@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @Operation(summary = "Search properties with filters and pagination")
    @GetMapping
    public Result<PageResponse<PropertySummaryResponse>> searchProperties(
            PropertySearchRequest request) {
        return Result.success(propertyService.searchProperties(request));
    }


    @Operation(summary = "Get property detail by id")
    @GetMapping("/{id}")
    public Result<PropertyDetailResponse> getPropertyDetail(@PathVariable Long id) {
        return Result.success(propertyService.getPropertyDetail(id));
    }
}