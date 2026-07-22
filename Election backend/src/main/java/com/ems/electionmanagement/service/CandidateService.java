package com.ems.electionmanagement.service;

import com.ems.electionmanagement.dto.candidate.CandidateRequest;
import com.ems.electionmanagement.dto.candidate.CandidateResponse;
import com.ems.electionmanagement.entity.Candidate;
import com.ems.electionmanagement.entity.Election;
import com.ems.electionmanagement.entity.PoliticalParty;
import com.ems.electionmanagement.exception.ConflictException;
import com.ems.electionmanagement.exception.ResourceNotFoundException;
import com.ems.electionmanagement.repository.CandidateRepository;
import com.ems.electionmanagement.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final ElectionService electionService;
    private final PoliticalPartyService partyService;
    private final VoteRepository voteRepository;

    @Transactional(readOnly = true)
    public List<CandidateResponse> getAll() {
        return candidateRepository.findAllByOrderByIdAsc().stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public CandidateResponse getById(Long id) {
        return toResponse(findEntity(id));
    }

    @Transactional
    public CandidateResponse create(CandidateRequest request) {
        Candidate candidate = new Candidate();
        apply(candidate, request);
        return toResponse(candidateRepository.save(candidate));
    }

    @Transactional
    public CandidateResponse update(Long id, CandidateRequest request) {
        Candidate candidate = findEntity(id);
        apply(candidate, request);
        return toResponse(candidateRepository.save(candidate));
    }

    @Transactional
    public void delete(Long id) {
        Candidate candidate = findEntity(id);
        if (voteRepository.countByCandidateId(id) > 0) {
            throw new ConflictException(
                "Cannot delete this candidate because vote records exist for this candidate."
            );
        }
        candidateRepository.delete(candidate);
    }

    public Candidate findEntity(Long id) {
        return candidateRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Candidate not found."));
    }

    private void apply(Candidate candidate, CandidateRequest request) {
        Election election = electionService.findEntity(request.electionID());
        PoliticalParty party = partyService.findEntity(request.partyID());

        candidate.setElection(election);
        candidate.setParty(party);
        candidate.setFullName(request.fullName().trim());
        candidate.setGender(request.gender().trim());
        candidate.setPhoto(blankToNull(request.photo()));
        candidate.setBiography(blankToNull(request.biography()));
    }

    private CandidateResponse toResponse(Candidate candidate) {
        return new CandidateResponse(
            candidate.getId(),
            candidate.getElection().getId(),
            candidate.getParty().getId(),
            candidate.getFullName(),
            candidate.getGender(),
            candidate.getPhoto(),
            candidate.getBiography()
        );
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
