package com.ems.electionmanagement.dto.party;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PartyRequest(
    @NotBlank(message = "Party name is required.")
    @Size(min = 2, max = 150, message = "Party name must be between 2 and 150 characters.")
    String partyName,

    @NotBlank(message = "Leader name is required.")
    @Size(min = 2, max = 150, message = "Leader name must be between 2 and 150 characters.")
    String leaderName,

    @Size(max = 255, message = "Logo filename cannot exceed 255 characters.")
    String logo
) {
}
