package com.ems.electionmanagement.dto.candidate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CandidateRequest(
    @NotNull(message = "Election is required.")
    @Positive(message = "Election ID must be positive.")
    Long electionID,

    @NotNull(message = "Political party is required.")
    @Positive(message = "Party ID must be positive.")
    Long partyID,

    @NotBlank(message = "Full name is required.")
    @Size(min = 2, max = 150, message = "Full name must be between 2 and 150 characters.")
    String fullName,

    @NotBlank(message = "Gender is required.")
    @Pattern(regexp = "(?i)Male|Female|Other", message = "Gender must be Male, Female, or Other.")
    String gender,

    @Size(max = 255, message = "Photo filename cannot exceed 255 characters.")
    String photo,

    @Size(max = 2000, message = "Biography cannot exceed 2000 characters.")
    String biography
) {
}
