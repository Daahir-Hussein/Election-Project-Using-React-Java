package com.ems.electionmanagement.dto.voter;

public record VoterCandidateResponse(
    Long candidateID,
    String candidateName,
    String biography,
    String photo,
    String partyName,
    String partyLogo
) {
}
