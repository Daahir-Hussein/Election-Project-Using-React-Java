package com.ems.electionmanagement.repository;

import com.ems.electionmanagement.entity.Candidate;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    @Override
    @EntityGraph(attributePaths = {"election", "party"})
    List<Candidate> findAll();

    @EntityGraph(attributePaths = {"election", "party"})
    List<Candidate> findAllByOrderByIdAsc();

    @EntityGraph(attributePaths = {"party"})
    List<Candidate> findByElectionIdOrderByIdAsc(Long electionId);

    long countByElectionId(Long electionId);
    long countByPartyId(Long partyId);

    @Query("""
        select c.fullName as candidateName,
               p.partyName as partyName,
               count(v.id) as totalVotes
        from Candidate c
        join c.party p
        left join c.votes v
        where c.election.id = :electionId
        group by c.id, c.fullName, p.partyName
        order by count(v.id) desc, c.fullName asc
        """)
    List<CandidateResultProjection> findResultsByElectionId(@Param("electionId") Long electionId);
}
