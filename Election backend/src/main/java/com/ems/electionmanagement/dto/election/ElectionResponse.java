package com.ems.electionmanagement.dto.election;

import java.time.LocalDate;

public record ElectionResponse(
    Long electionID,
    String electionName,
    LocalDate startDate,
    LocalDate endDate,
    String status
) {
}
