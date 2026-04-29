package com.hc.rent.security;

import com.hc.rent.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration}")
    private long expiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // Generate token, roles stored as a list of strings
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());

        // Convert Set<Role> to List<String> e.g. ["LANDLORD", "TENANT"]
        List<String> roles = user.getRoles()
                .stream()
                .map(User.Role::name)
                .collect(Collectors.toList());
        claims.put("roles", roles);

        // Use email as subject if available, otherwise use phone
        String subject = user.getEmail() != null ? user.getEmail() : user.getPhone();

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    // Parse token and return claims
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Get subject (email or phone) from token
    public String getSubject(String token) {
        return parseToken(token).getSubject();
    }

    // Get userId from token
    public Long getUserId(String token) {
        return parseToken(token).get("userId", Long.class);
    }

    // Get roles as List<String> from token e.g. ["LANDLORD", "TENANT"]
    @SuppressWarnings("unchecked")
    public List<String> getRoles(String token) {
        return (List<String>) parseToken(token).get("roles");
    }

    // Validate token (signature + expiration)
    public boolean isTokenValid(String token) {
        try {
            return !parseToken(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}