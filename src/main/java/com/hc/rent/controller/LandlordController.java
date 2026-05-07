package com.hc.rent.controller;

import com.hc.rent.common.Result;
import com.hc.rent.dto.request.CreatePropertyRequest;
import com.hc.rent.dto.response.PropertyDetailResponse;
import com.hc.rent.service.LandlordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Landlord", description = "Landlord property management endpoints")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/landlord")
@RequiredArgsConstructor
public class LandlordController {

    private final LandlordService landlordService;

    @Operation(summary = "Create a new property listing")
    @PostMapping("/properties")
    public Result<PropertyDetailResponse> createProperty(
            @Valid @RequestBody CreatePropertyRequest request) {
        return Result.success(landlordService.createProperty(request));
    }
}