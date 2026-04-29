package com.hc.rent.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    // Login with either email or phone
    private String email;

    private String phone;

    @NotBlank(message = "Password is required")
    private String password;
}