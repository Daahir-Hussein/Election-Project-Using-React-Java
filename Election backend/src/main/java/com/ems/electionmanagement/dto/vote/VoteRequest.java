package com.ems.electionmanagement.dto.vote;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record VoteRequest(
    @NotNull(message = "Voter ID is required.")
    @Positive(message = "Voter ID must be positive.")
    Long voterID,

    @NotNull(message = "Election ID is required.")
    @Positive(message = "Election ID must be positive.")
    Long electionID,

    @NotNull(message = "Candidate ID is required.")
    @Positive(message = "Candidate ID must be positive.")
    Long candidateID
) {
}
