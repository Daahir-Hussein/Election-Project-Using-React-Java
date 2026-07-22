package com.ems.electionmanagement.repository;

public interface CandidateResultProjection {
    String getCandidateName();
    String getPartyName();
    Long getTotalVotes();
}
