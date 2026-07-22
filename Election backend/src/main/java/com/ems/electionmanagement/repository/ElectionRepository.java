package com.ems.electionmanagement.repository;

import com.ems.electionmanagement.entity.Election;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ElectionRepository extends JpaRepository<Election, Long> {
    List<Election> findAllByOrderByIdAsc();
    List<Election> findByStatusIgnoreCaseOrderByStartDateAsc(String status);
    boolean existsByElectionNameIgnoreCase(String electionName);
    boolean existsByElectionNameIgnoreCaseAndIdNot(String electionName, Long id);
}
