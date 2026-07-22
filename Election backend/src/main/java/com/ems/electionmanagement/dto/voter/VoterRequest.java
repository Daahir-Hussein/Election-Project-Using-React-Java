package com.ems.electionmanagement.dto.voter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record VoterRequest(
    @NotBlank(message = "National ID is required.")
    @Pattern(regexp = "^[0-9]{5,20}$", message = "National ID must contain 5 to 20 digits.")
    String nationalID,

    @NotBlank(message = "Full name is required.")
    @Size(min = 2, max = 150, message = "Full name must be between 2 and 150 characters.")
    String fullName,

    @NotBlank(message = "Gender is required.")
    @Pattern(regexp = "(?i)Male|Female|Other", message = "Gender must be Male, Female, or Other.")
    String gender,

    @NotNull(message = "Date of birth is required.")
    @Past(message = "Date of birth must be a past date.")
    LocalDate dateOfBirth,

    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = "^[0-9+()\\-\\s]{7,30}$", message = "Enter a valid phone number.")
    String phone,

    @NotBlank(message = "Address is required.")
    @Size(max = 255, message = "Address cannot exceed 255 characters.")
    String address
) {
}
