package com.hc.rent.controller;


import com.hc.rent.common.Result;
import com.hc.rent.dto.request.UpdateProfileRequest;
import com.hc.rent.dto.response.UserProfileResponse;
import com.hc.rent.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User", description = "User management endpoints")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    /**
     * Get user profile
     * @return
     */
    @GetMapping("/profile")
    @Operation(summary = "Get the user profile")
    public Result<UserProfileResponse> getProfile() {
        return Result.success(userService.getUserProfile());
    }


    /**
     * update user profile
     * @param request
     * @return
     */
    @PutMapping("")
    @Operation(summary = "update user information")
    public Result<UserProfileResponse> updateProfile(@RequestBody UpdateProfileRequest request){
        return Result.success(userService.updateUserProfile(request));
    }




}
