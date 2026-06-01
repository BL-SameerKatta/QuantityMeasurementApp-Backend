package com.apps.auth_service.service;

import com.apps.auth_service.dto.AuthResponse;
import com.apps.auth_service.dto.LoginRequest;
import com.apps.auth_service.dto.RegisterRequest;
import com.apps.auth_service.entity.User;
import com.apps.auth_service.repository.UserRepository;
import com.apps.auth_service.security.JwtUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Business logic for user registration, login, and token refresh.
 */
@Service
public class AuthService {

    private static final Logger log = LogManager.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // ─── Register ─────────────────────────────────────────────────────────────

    /**
     * Registers a new user and returns both access and refresh tokens.
     * Throws {@link RuntimeException} if the email is already taken.
     */
    public AuthResponse register(RegisterRequest request) {
        log.info("Register attempt for email: {}", request.getEmail());

        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            log.warn("Registration failed — email already exists: {}", request.getEmail());
            throw new RuntimeException("User with this email already exists!");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setProvider("LOCAL");
        userRepository.save(user);

        String accessToken  = jwtUtil.generateAccessToken(user.getEmail(), user.getName());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        log.info("Registration successful for email: {}", request.getEmail());
        return new AuthResponse(accessToken, refreshToken, "Registration successful!");
    }

    // ─── Login ────────────────────────────────────────────────────────────────

    /**
     * Authenticates a user and returns both access and refresh tokens.
     * Throws {@link RuntimeException} on invalid credentials.
     */
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            log.warn("Login failed — email not found: {}", request.getEmail());
            throw new RuntimeException("Invalid email or password!");
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Login failed — wrong password for email: {}", request.getEmail());
            throw new RuntimeException("Invalid email or password!");
        }

        String accessToken  = jwtUtil.generateAccessToken(user.getEmail(), user.getName());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        log.info("Login successful for email: {}", request.getEmail());
        return new AuthResponse(accessToken, refreshToken, "Login successful!");
    }

    // ─── Refresh ──────────────────────────────────────────────────────────────

    /**
     * Validates the provided refresh token and issues a new access token.
     * Throws {@link RuntimeException} if the token is invalid or not a refresh token.
     */
    public AuthResponse refreshAccessToken(String refreshToken) {
        log.info("Token refresh requested");

        if (!jwtUtil.validateToken(refreshToken)) {
            log.warn("Token refresh failed — invalid or expired token");
            throw new RuntimeException("Invalid or expired refresh token!");
        }

        if (!jwtUtil.isRefreshToken(refreshToken)) {
            log.warn("Token refresh failed — token is not a refresh token");
            throw new RuntimeException("Provided token is not a refresh token!");
        }

        String email       = jwtUtil.extractEmail(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found for token!"));

        String newAccessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getName());
        log.info("Token refreshed successfully for email: {}", email);

        return new AuthResponse(newAccessToken, refreshToken, "Token refreshed successfully!");
    }
}
