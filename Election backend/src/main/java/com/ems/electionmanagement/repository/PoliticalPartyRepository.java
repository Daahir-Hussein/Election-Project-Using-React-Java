package com.ems.electionmanagement.repository;

import com.ems.electionmanagement.entity.PoliticalParty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PoliticalPartyRepository extends JpaRepository<PoliticalParty, Long> {
    List<PoliticalParty> findAllByOrderByIdAsc();
    boolean existsByPartyNameIgnoreCase(String partyName);
    boolean existsByPartyNameIgnoreCaseAndIdNot(String partyName, Long id);
}
