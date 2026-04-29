package com.hc.rent.security;

import com.hc.rent.common.UserContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = extractToken(request);

        if (StringUtils.hasText(token) && jwtUtil.isTokenValid(token)) {
            try {
                Long userId = jwtUtil.getUserId(token);
                String subject = jwtUtil.getSubject(token);
                List<String> roles = jwtUtil.getRoles(token);

                // Store user info in ThreadLocal for easy access in Service layer
                UserContext.setUserId(userId);
                UserContext.setUserRoles(roles);

                // Convert List<String> roles to Spring Security authorities
                // e.g. "LANDLORD" → ROLE_LANDLORD
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());

                // Set authentication in Spring Security context
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userId,
                                null,
                                authorities
                        );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Authenticated user: {}, roles: {}", subject, roles);

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