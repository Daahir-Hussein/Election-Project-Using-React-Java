package com.ems.electionmanagement.dto.dashboard;

public record DashboardResponse(
    long totalElections,
    long totalParties,
    long totalCandidates,
    long totalVoters,
    long totalVotes
) {
}
