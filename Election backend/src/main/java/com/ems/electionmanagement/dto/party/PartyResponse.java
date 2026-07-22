package com.ems.electionmanagement.dto.party;

public record PartyResponse(
    Long partyID,
    String partyName,
    String leaderName,
    String logo
) {
}
