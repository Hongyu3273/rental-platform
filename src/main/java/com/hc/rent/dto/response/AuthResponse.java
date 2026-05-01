package com.hc.rent.dto.response;

import com.hc.rent.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {

    private String token;
    private Long userId;
    private String firstName;
    private String lastName;
    private User.Role role;
    private String email;
    private String phone;
}