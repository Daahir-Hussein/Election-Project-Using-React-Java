package com.ems.electionmanagement.dto.auth;

public record UserResponse(
    Long id,
    String username,
    String email,
    String role
) {
}
