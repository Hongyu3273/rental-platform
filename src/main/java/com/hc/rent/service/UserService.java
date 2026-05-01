package com.hc.rent.service;

import com.hc.rent.dto.request.LoginRequest;
import com.hc.rent.dto.request.RegisterRequest;
import com.hc.rent.dto.request.UpdateProfileRequest;
import com.hc.rent.dto.response.AuthResponse;
import com.hc.rent.dto.response.UserProfileResponse;

public interface UserService {

    // Register a new user with email or phone
    AuthResponse register(RegisterRequest request);

    // Login with email or phone
    AuthResponse login(LoginRequest request);


    // Get user profile
    UserProfileResponse getUserProfile();


    // Update UserProfile
    UserProfileResponse updateUserProfile(UpdateProfileRequest request);

    // log out
    void logout(String token);
}