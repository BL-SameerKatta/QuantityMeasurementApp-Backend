package com.apps.auth_service.dto;

/**
 * Response DTO returned on login / register / token-refresh.
 * Contains both an access token (short-lived) and a refresh token (long-lived).
 */
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private String message;

    public AuthResponse() {}

    public AuthResponse(String accessToken, String refreshToken, String message) {
        this.accessToken  = accessToken;
        this.refreshToken = refreshToken;
        this.message      = message;
    }

    /** Convenience constructor for error responses (no tokens). */
    public AuthResponse(String message) {
        this.message = message;
    }

    public String getAccessToken()  { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public String getMessage()      { return message; }
    public void setMessage(String message) { this.message = message; }

    // Legacy helper so existing code that checks getToken() != null still compiles
    public String getToken()        { return accessToken; }
    public void setToken(String token) { this.accessToken = token; }
}
