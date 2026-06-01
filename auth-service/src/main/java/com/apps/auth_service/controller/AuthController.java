package com.apps.auth_service.controller;

import com.apps.auth_service.dto.AuthResponse;
import com.apps.auth_service.dto.LoginRequest;
import com.apps.auth_service.dto.RefreshRequest;
import com.apps.auth_service.dto.RegisterRequest;
import com.apps.auth_service.service.AuthService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller handling authentication endpoints.
 * All responses are wrapped in ResponseEntity for proper HTTP status codes.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LogManager.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    /**
     * POST /auth/register
     * Registers a new user. Returns HTTP 201 CREATED with access + refresh tokens.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("POST /auth/register called for email: {}", request.getEmail());
        AuthResponse response = authService.register(request);
        log.info("Registration response: {}", response.getMessage());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * POST /auth/login
     * Authenticates a user. Returns HTTP 200 OK with access + refresh tokens.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("POST /auth/login called for email: {}", request.getEmail());
        AuthResponse response = authService.login(request);
        log.info("Login response: {}", response.getMessage());
        return ResponseEntity.ok(response);
    }

    /**
     * POST /auth/refresh
     * Issues a new access token from a valid refresh token.
     * Returns HTTP 200 OK with a new access token (refresh token is unchanged).
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        log.info("POST /auth/refresh called");
        AuthResponse response = authService.refreshAccessToken(request.getRefreshToken());
        log.info("Token refreshed successfully");
        return ResponseEntity.ok(response);
    }
}
