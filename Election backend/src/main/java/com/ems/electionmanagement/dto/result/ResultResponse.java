package com.ems.electionmanagement.dto.result;

public record ResultResponse(
    String candidateName,
    String partyName,
    Long totalVotes
) {
}
