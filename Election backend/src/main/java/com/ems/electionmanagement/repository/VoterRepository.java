package com.ems.electionmanagement.repository;

import com.ems.electionmanagement.entity.Voter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VoterRepository extends JpaRepository<Voter, Long> {
    List<Voter> findAllByOrderByIdAsc();
    Optional<Voter> findByNationalId(String nationalId);
    boolean existsByNationalId(String nationalId);
    boolean existsByNationalIdAndIdNot(String nationalId, Long id);
}
