package com.apps.auth_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Utility class for JWT operations in auth-service.
 * Generates short-lived access tokens (15 min) and long-lived refresh tokens (7 days).
 */
@Component
public class JwtUtil {

    private static final Logger log = LogManager.getLogger(JwtUtil.class);

    // Shared secret — must match across services
    private final Key SECRET = Keys.hmacShaKeyFor(
            "mysecretkeymysecretkeymysecretkey".getBytes());

    private static final long ACCESS_TOKEN_EXPIRY  = 15 * 60 * 1000L;        // 15 minutes
    private static final long REFRESH_TOKEN_EXPIRY = 7  * 24 * 60 * 60 * 1000L; // 7 days

    // ─── Access Token ─────────────────────────────────────────────────────────

    /**
     * Generates a short-lived access token (15 minutes).
     */
    public String generateAccessToken(String email, String name) {
        log.debug("Generating access token for email: {}", email);
        return Jwts.builder()
                .setSubject(email)
                .claim("name", name)
                .claim("type", "ACCESS")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY))
                .signWith(SECRET)
                .compact();
    }

    // ─── Refresh Token ────────────────────────────────────────────────────────

    /**
     * Generates a long-lived refresh token (7 days).
     */
    public String generateRefreshToken(String email) {
        log.debug("Generating refresh token for email: {}", email);
        return Jwts.builder()
                .setSubject(email)
                .claim("type", "REFRESH")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRY))
                .signWith(SECRET)
                .compact();
    }

    // ─── Extract / Validate ───────────────────────────────────────────────────

    /**
     * Extracts the subject (email) from any token.
     */
    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * Validates a token — checks signature and expiry.
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Checks whether the token is a REFRESH type token.
     */
    public boolean isRefreshToken(String token) {
        String type = (String) parseClaims(token).get("type");
        return "REFRESH".equals(type);
    }

    // ─── Internal ─────────────────────────────────────────────────────────────

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
