package com.ems.electionmanagement.dto.election;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ElectionRequest(
    @NotBlank(message = "Election name is required.")
    @Size(min = 3, max = 150, message = "Election name must be between 3 and 150 characters.")
    String electionName,

    @NotNull(message = "Start date is required.")
    LocalDate startDate,

    @NotNull(message = "End date is required.")
    LocalDate endDate,

    @NotBlank(message = "Status is required.")
    @Pattern(regexp = "(?i)Open|Closed|Upcoming", message = "Status must be Open, Closed, or Upcoming.")
    String status
) {
    @AssertTrue(message = "End date must be on or after start date.")
    public boolean isDateRangeValid() {
        return startDate == null || endDate == null || !endDate.isBefore(startDate);
    }
}
