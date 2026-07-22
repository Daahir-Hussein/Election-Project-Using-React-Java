package com.ems.electionmanagement.dto.voter;

import java.time.LocalDate;

public record VoterElectionResponse(
    Long electionID,
    String electionName,
    LocalDate startDate,
    LocalDate endDate
) {
}
