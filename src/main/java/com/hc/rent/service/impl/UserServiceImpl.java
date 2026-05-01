package com.hc.rent.service.impl;

import com.hc.rent.common.UserContext;
import com.hc.rent.dto.request.LoginRequest;
import com.hc.rent.dto.request.RegisterRequest;
import com.hc.rent.dto.request.UpdateProfileRequest;
import com.hc.rent.dto.response.AuthResponse;
import com.hc.rent.dto.response.UserProfileResponse;
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


    /**
     * User register
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        // Validate that at least one of email or phone is provided
        if (!StringUtils.hasText(request.getEmail()) && !StringUtils.hasText(request.getPhone())) {
            throw new BusinessException(400, "Email or phone number is required");
        }

        // Check email + role uniqueness if email provided
        if (StringUtils.hasText(request.getEmail()) &&
                userRepository.existsByEmailAndRole(request.getEmail(), request.getRole())) {
            throw new BusinessException(400, "Email already registered for this role");
        }

        // Check phone + role uniqueness if phone provided
        if (StringUtils.hasText(request.getPhone()) &&
                userRepository.existsByPhoneAndRole(request.getPhone(), request.getRole())) {
            throw new BusinessException(400, "Phone number already registered for this role");
        }

        // Build and save new user
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .middleName(request.getMiddleName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .enabled(true)
                .build();

        userRepository.save(user);
        log.info("New user registered: {}", user.getEmail() != null ? user.getEmail() : user.getPhone());

        String token = jwtUtil.generateToken(user);
        return buildAuthResponse(token, user);
    }




    /**
     * Login Function
     *
     * @param request
     * @return
     */
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


    // Find user by email or phone based on what was provided in request
    private User findUserByIdentifier(LoginRequest request) {
        if (StringUtils.hasText(request.getEmail())) {
            return userRepository.findByEmailAndRole(request.getEmail(), request.getRole())
                    .orElseThrow(() -> new BusinessException(401, "Invalid credentials"));
        }
        return userRepository.findByPhoneAndRole(request.getPhone(), request.getRole())
                .orElseThrow(() -> new BusinessException(401, "Invalid credentials"));
    }


    // Build AuthResponse from token and user
    private AuthResponse buildAuthResponse(String token, User user) {
        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }

    /**
     * get user profile
     *
     * @return
     */
    public UserProfileResponse getUserProfile() {
        log.info("Fetching user profile");
        Long userId = UserContext.getUserId();

        // Find user by id
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(404, "User not found"));

        // Build and return response
        return UserProfileResponse.builder()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .middleName(user.getMiddleName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }


    /**
     * update User Profile
     *
     * @param request
     * @return
     */
    @Transactional
    public UserProfileResponse updateUserProfile(UpdateProfileRequest request) {
        log.info("Updating user profile");
        Long userId = UserContext.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(404, "User not found"));

        if (StringUtils.hasText(request.getFirstName())) {
            user.setFirstName(request.getFirstName());
        }
        if (StringUtils.hasText(request.getLastName())) {
            user.setLastName(request.getLastName());
        }
        if (StringUtils.hasText(request.getMiddleName())) {
            user.setMiddleName(request.getMiddleName());
        }
        if (StringUtils.hasText(request.getPhone())) {
            user.setPhone(request.getPhone());
        }
        if (StringUtils.hasText(request.getAvatarUrl())) {
            user.setAvatarUrl(request.getAvatarUrl());
        }

        userRepository.save(user);
        log.info("User profile updated: {}", userId);

        return UserProfileResponse.builder()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .middleName(user.getMiddleName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }


}