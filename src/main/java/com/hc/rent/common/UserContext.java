package com.hc.rent.common;

import java.util.List;

public class UserContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<List<String>> USER_ROLES = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    public static Long getUserId() {
        return USER_ID.get();
    }

    public static void setUserRoles(List<String> roles) {
        USER_ROLES.set(roles);
    }

    public static List<String> getUserRoles() {
        return USER_ROLES.get();
    }

    // Check if current user has a specific role
    public static boolean hasRole(String role) {
        List<String> roles = USER_ROLES.get();
        return roles != null && roles.contains(role);
    }

    // Must clear ThreadLocal after each request to prevent memory leaks
    public static void clear() {
        USER_ID.remove();
        USER_ROLES.remove();
    }
}