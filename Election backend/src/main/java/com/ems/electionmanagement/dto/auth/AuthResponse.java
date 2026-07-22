package com.ems.electionmanagement.dto.auth;

public record AuthResponse(
    String token,
    String tokenType,
    UserResponse user
) {
}
