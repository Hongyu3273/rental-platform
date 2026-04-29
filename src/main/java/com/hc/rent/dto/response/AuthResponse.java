package com.hc.rent.dto.response;

import com.hc.rent.entity.User;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class AuthResponse {

    private String token;
    private Long userId;
    private String firstName;
    private String lastName;
    private Set<User.Role> roles;
    private String email;
    private String phone;
}