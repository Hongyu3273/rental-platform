package com.hc.rent.dto.response;

import com.hc.rent.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserProfileResponse {

    private Long userId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String phone;
    private String avatarUrl;
    private User.Role role;
    private LocalDateTime createdAt;
}