package com.ems.electionmanagement.dto.candidate;

public record CandidateResponse(
    Long candidateID,
    Long electionID,
    Long partyID,
    String fullName,
    String gender,
    String photo,
    String biography
) {
}
