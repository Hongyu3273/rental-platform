package com.hc.rent.service.impl;

import com.hc.rent.dto.request.LoginRequest;
import com.hc.rent.dto.request.RegisterRequest;
import com.hc.rent.dto.response.AuthResponse;
import com.hc.rent.entity.User;
import com.hc.rent.exception.BusinessException;
import com.hc.rent.repository.UserRepository;
import com.hc.rent.security.JwtUtil;
import com.hc.rent.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        // Validate that at least one of email or phone is provided
        if (!StringUtils.hasText(request.getEmail()) && !StringUtils.hasText(request.getPhone())) {
            throw new BusinessException(400, "Email or phone number is required");
        }

        // Check email uniqueness if provided
        if (StringUtils.hasText(request.getEmail()) &&
                userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(400, "Email already exists");
        }

        // Check phone uniqueness if provided
        if (StringUtils.hasText(request.getPhone()) &&
                userRepository.existsByPhone(request.getPhone())) {
            throw new BusinessException(400, "Phone number already exists");
        }

        // Build initial roles set
        Set<User.Role> roles = new HashSet<>();
        roles.add(request.getRole());

        // Build and save new user
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .middleName(request.getMiddleName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .enabled(true)
                .build();

        userRepository.save(user);
        log.info("New user registered: {}", user.getEmail() != null ? user.getEmail() : user.getPhone());

        // Generate JWT token
        String token = jwtUtil.generateToken(user);

        return buildAuthResponse(token, user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        // Validate that at least one of email or phone is provided
        if (!StringUtils.hasText(request.getEmail()) && !StringUtils.hasText(request.getPhone())) {
            throw new BusinessException(400, "Email or phone number is required");
        }

        // Find user by email or phone
        User user = findUserByIdentifier(request);

        // Check if account is enabled
        if (!user.getEnabled()) {
            throw new BusinessException(403, "Account is disabled");
        }

        // Validate password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(401, "Invalid credentials");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user);
        log.info("User logged in: {}", user.getEmail() != null ? user.getEmail() : user.getPhone());

        return buildAuthResponse(token, user);
    }

    @Override
    @Transactional
    public void addRole(Long userId, String role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(404, "User not found"));

        // Convert string to Role enum
        User.Role newRole;
        try {
            newRole = User.Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(400, "Invalid role: " + role);
        }

        // Check if user already has this role
        if (user.getRoles().contains(newRole)) {
            throw new BusinessException(400, "User already has role: " + role);
        }

        user.getRoles().add(newRole);
        userRepository.save(user);
        log.info("Role {} added to user {}", role, userId);
    }

    // Find user by email or phone based on what was provided in request
    private User findUserByIdentifier(LoginRequest request) {
        if (StringUtils.hasText(request.getEmail())) {
            return userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new BusinessException(401, "Invalid credentials"));
        }
        return userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new BusinessException(401, "Invalid credentials"));
    }

    // Build AuthResponse from token and user
    private AuthResponse buildAuthResponse(String token, User user) {
        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(user.getRoles())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }
}