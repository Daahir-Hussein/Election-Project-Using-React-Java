package com.ems.electionmanagement.service;

import com.ems.electionmanagement.dto.vote.VoteRequest;
import com.ems.electionmanagement.dto.voter.VoterCandidateResponse;
import com.ems.electionmanagement.dto.voter.VoterElectionResponse;
import com.ems.electionmanagement.dto.voter.VoterLoginResponse;
import com.ems.electionmanagement.entity.Candidate;
import com.ems.electionmanagement.entity.Election;
import com.ems.electionmanagement.entity.Vote;
import com.ems.electionmanagement.entity.Voter;
import com.ems.electionmanagement.exception.BadRequestException;
import com.ems.electionmanagement.exception.ResourceNotFoundException;
import com.ems.electionmanagement.repository.CandidateRepository;
import com.ems.electionmanagement.repository.ElectionRepository;
import com.ems.electionmanagement.repository.VoteRepository;
import com.ems.electionmanagement.repository.VoterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoterPortalService {

    private final VoterRepository voterRepository;
    private final ElectionRepository electionRepository;
    private final CandidateRepository candidateRepository;
    private final VoteRepository voteRepository;

    @Transactional(readOnly = true)
    public VoterLoginResponse login(String nationalId) {
        Voter voter = voterRepository.findByNationalId(nationalId.trim())
            .orElseThrow(() -> new ResourceNotFoundException("Voter not found."));
        return new VoterLoginResponse(voter.getId(), voter.getNationalId(), voter.getFullName());
    }

    @Transactional(readOnly = true)
    public List<VoterElectionResponse> getOpenElections() {
        return electionRepository.findByStatusIgnoreCaseOrderByStartDateAsc("Open").stream()
            .map(election -> new VoterElectionResponse(
                election.getId(),
                election.getElectionName(),
                election.getStartDate(),
                election.getEndDate()
            ))
            .toList();
    }

    @Transactional(readOnly = true)
    public List<VoterCandidateResponse> getCandidates(Long electionId) {
        if (!electionRepository.existsById(electionId)) {
            throw new ResourceNotFoundException("Election not found.");
        }
        return candidateRepository.findByElectionIdOrderByIdAsc(electionId).stream()
            .map(candidate -> new VoterCandidateResponse(
                candidate.getId(),
                candidate.getFullName(),
                candidate.getBiography(),
                candidate.getPhoto(),
                candidate.getParty().getPartyName(),
                candidate.getParty().getLogo()
            ))
            .toList();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void castVote(VoteRequest request) {
        Election election = electionRepository.findById(request.electionID())
            .orElseThrow(() -> new ResourceNotFoundException("Election not found."));

        if (!"Open".equalsIgnoreCase(election.getStatus())) {
            throw new BadRequestException("This election is not open for voting.");
        }

        Candidate candidate = candidateRepository.findById(request.candidateID())
            .orElseThrow(() -> new ResourceNotFoundException("Candidate not found."));
        if (!candidate.getElection().getId().equals(election.getId())) {
            throw new BadRequestException("Candidate does not belong to the selected election.");
        }

        Voter voter = voterRepository.findById(request.voterID())
            .orElseThrow(() -> new ResourceNotFoundException("Voter not found."));

        if (voteRepository.existsByVoterIdAndElectionId(voter.getId(), election.getId())) {
            throw new BadRequestException("You have already voted in this election.");
        }

        Vote vote = new Vote();
        vote.setVoter(voter);
        vote.setCandidate(candidate);
        vote.setElection(election);

        try {
            voteRepository.saveAndFlush(vote);
        } catch (DataIntegrityViolationException ex) {
            throw new BadRequestException("You have already voted in this election.");
        }
    }
}
