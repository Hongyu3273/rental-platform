package com.hc.rent.dto.request;

import com.hc.rent.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {

    // Login with either email or phone
    private String email;

    private String phone;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Role is required")
    private User.Role role;
}