package com.hc.rent.service;

import com.hc.rent.dto.request.LoginRequest;
import com.hc.rent.dto.request.RegisterRequest;
import com.hc.rent.dto.response.AuthResponse;

public interface UserService {

    // Register a new user with email or phone
    AuthResponse register(RegisterRequest request);

    // Login with email or phone
    AuthResponse login(LoginRequest request);

    // Add a new role to existing user
    void addRole(Long userId, String role);
}