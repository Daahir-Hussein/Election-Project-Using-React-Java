package com.ems.electionmanagement.service;

import com.ems.electionmanagement.dto.election.ElectionRequest;
import com.ems.electionmanagement.dto.election.ElectionResponse;
import com.ems.electionmanagement.entity.Election;
import com.ems.electionmanagement.exception.BadRequestException;
import com.ems.electionmanagement.exception.ConflictException;
import com.ems.electionmanagement.exception.ResourceNotFoundException;
import com.ems.electionmanagement.repository.CandidateRepository;
import com.ems.electionmanagement.repository.ElectionRepository;
import com.ems.electionmanagement.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ElectionService {

    private final ElectionRepository electionRepository;
    private final CandidateRepository candidateRepository;
    private final VoteRepository voteRepository;

    @Transactional(readOnly = true)
    public List<ElectionResponse> getAll() {
        return electionRepository.findAllByOrderByIdAsc().stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public ElectionResponse getById(Long id) {
        return toResponse(findEntity(id));
    }

    @Transactional
    public ElectionResponse create(ElectionRequest request) {
        if (request.startDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Start date cannot be in the past.");
        }
        String name = request.electionName().trim();
        if (electionRepository.existsByElectionNameIgnoreCase(name)) {
            throw new ConflictException("An election with this name already exists.");
        }

        Election election = new Election();
        apply(election, request);
        return toResponse(electionRepository.save(election));
    }

    @Transactional
    public ElectionResponse update(Long id, ElectionRequest request) {
        Election election = findEntity(id);
        String name = request.electionName().trim();
        if (electionRepository.existsByElectionNameIgnoreCaseAndIdNot(name, id)) {
            throw new ConflictException("An election with this name already exists.");
        }
        apply(election, request);
        return toResponse(electionRepository.save(election));
    }

    @Transactional
    public void delete(Long id) {
        Election election = findEntity(id);
        if (candidateRepository.countByElectionId(id) > 0 || voteRepository.countByElectionId(id) > 0) {
            throw new ConflictException(
                "Cannot delete this election because candidates or vote records are linked to it."
            );
        }
        electionRepository.delete(election);
    }

    public Election findEntity(Long id) {
        return electionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Election not found."));
    }

    private void apply(Election election, ElectionRequest request) {
        election.setElectionName(request.electionName().trim());
        election.setStartDate(request.startDate());
        election.setEndDate(request.endDate());
        election.setStatus(normalizeStatus(request.status()));
    }

    private String normalizeStatus(String value) {
        String lower = value.trim().toLowerCase(Locale.ROOT);
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }

    private ElectionResponse toResponse(Election election) {
        return new ElectionResponse(
            election.getId(),
            election.getElectionName(),
            election.getStartDate(),
            election.getEndDate(),
            election.getStatus()
        );
    }
}
