package com.ems.electionmanagement.dto.voter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record VoterLoginRequest(
    @NotBlank(message = "National ID is required.")
    @Pattern(regexp = "^[0-9]{5,20}$", message = "National ID must contain 5 to 20 digits.")
    String nationalID
) {
}
