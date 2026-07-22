package com.ems.electionmanagement.dto.vote;

import java.time.OffsetDateTime;

public record VoteRecordResponse(
    Long voteID,
    String electionName,
    String candidateName,
    String partyName,
    String voterName,
    OffsetDateTime voteDate
) {
}
