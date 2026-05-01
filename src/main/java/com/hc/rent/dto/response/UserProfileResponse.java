package com.hc.rent.dto.response;


import com.hc.rent.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;


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
    private Set<User.Role> roles;
    private LocalDateTime createdAt;
}
