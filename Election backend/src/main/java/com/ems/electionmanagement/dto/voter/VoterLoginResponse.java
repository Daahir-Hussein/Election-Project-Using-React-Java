package com.ems.electionmanagement.dto.voter;

public record VoterLoginResponse(
    Long voterID,
    String nationalID,
    String fullName
) {
}
