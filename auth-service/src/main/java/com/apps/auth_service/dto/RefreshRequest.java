package com.apps.auth_service.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO used for the POST /auth/refresh endpoint.
 * Carries the refresh token issued at login / register.
 */
public class RefreshRequest {

    @NotBlank(message = "Refresh token is required")
    private String refreshToken;

    public RefreshRequest() {}

    public RefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken()            { return refreshToken; }
    public void setRefreshToken(String token)  { this.refreshToken = token; }
}
