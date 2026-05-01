package com.hc.rent.common;

public class UserContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> USER_ROLE = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    public static Long getUserId() {
        return USER_ID.get();
    }

    public static void setUserRole(String role) {
        USER_ROLE.set(role);
    }

    public static String getUserRole() {
        return USER_ROLE.get();
    }

    public static boolean hasRole(String role) {
        return role != null && role.equals(USER_ROLE.get());
    }

    // Must clear ThreadLocal after each request to prevent memory leaks
    public static void clear() {
        USER_ID.remove();
        USER_ROLE.remove();
    }
}