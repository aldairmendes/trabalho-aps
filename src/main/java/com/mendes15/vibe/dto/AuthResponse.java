package com.mendes15.vibe.dto;

import java.util.List;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn,        // ms
        String username,
        List<String> roles
) {
    public static AuthResponse of(String access, String refresh,
                                  long expiresIn, String username,
                                  List<String> roles) {
        return new AuthResponse(access, refresh, "Bearer", expiresIn, username, roles);
    }
}