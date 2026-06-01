package com.apps.auth_service.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Generic error response DTO returned by GlobalExceptionHandler.
 * Includes HTTP status code, a human-readable message, and timestamp.
 */
public class ErrorResponse {

    private int    status;
    private String message;
    private String timestamp;

    public ErrorResponse() {}

    public ErrorResponse(int status, String message) {
        this.status    = status;
        this.message   = message;
        this.timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public int    getStatus()    { return status; }
    public void   setStatus(int s) { this.status = s; }

    public String getMessage()             { return message; }
    public void   setMessage(String m)     { this.message = m; }

    public String getTimestamp()           { return timestamp; }
    public void   setTimestamp(String t)   { this.timestamp = t; }
}
