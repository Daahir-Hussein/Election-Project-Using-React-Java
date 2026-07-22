package com.ems.electionmanagement.dto.voter;

import java.time.LocalDate;

public record VoterResponse(
    Long voterID,
    String nationalID,
    String fullName,
    String gender,
    LocalDate dateOfBirth,
    String phone,
    String address
) {
}
