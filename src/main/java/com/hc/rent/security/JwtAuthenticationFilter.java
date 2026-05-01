package com.hc.rent.security;

import com.hc.rent.common.UserContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final RedisTemplate<String, String> redisTemplate;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = extractToken(request);

        if (StringUtils.hasText(token) && jwtUtil.isTokenValid(token)) {
            try {
                // Check if token is blacklisted in Redis
                if (Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + token))) {
                    log.warn("Blacklisted token attempt detected");
                    SecurityContextHolder.clearContext();
                    filterChain.doFilter(request, response);
                    return;
                }


                Long userId = jwtUtil.getUserId(token);
                String subject = jwtUtil.getSubject(token);
                String role = jwtUtil.getRole(token);

                // Store user info in ThreadLocal for easy access in Service layer
                UserContext.setUserId(userId);
                UserContext.setUserRole(role);

                // Set authentication in Spring Security context
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userId,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + role))
                        );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Authenticated user: {}, role: {}", subject, role);

            } catch (Exception e) {
                log.warn("Invalid JWT token: {}", e.getMessage());
                SecurityContextHolder.clearContext();
                UserContext.clear();
            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            // Must clear ThreadLocal after each request to prevent memory leaks
            UserContext.clear();
        }
    }

    // Extract Bearer token from Authorization header
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}