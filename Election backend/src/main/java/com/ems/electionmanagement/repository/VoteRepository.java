package com.ems.electionmanagement.repository;

import com.ems.electionmanagement.entity.Vote;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsByVoterIdAndElectionId(Long voterId, Long electionId);
    long countByElectionId(Long electionId);
    long countByCandidateId(Long candidateId);
    long countByVoterId(Long voterId);

    @EntityGraph(attributePaths = {"election", "candidate", "candidate.party", "voter"})
    List<Vote> findAllByOrderByVoteDateDesc();
}
