package com.ems.electionmanagement.service;

import com.ems.electionmanagement.dto.vote.VoteRecordResponse;
import com.ems.electionmanagement.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;

    @Transactional(readOnly = true)
    public List<VoteRecordResponse> getAll() {
        return voteRepository.findAllByOrderByVoteDateDesc().stream()
            .map(vote -> new VoteRecordResponse(
                vote.getId(),
                vote.getElection().getElectionName(),
                vote.getCandidate().getFullName(),
                vote.getCandidate().getParty().getPartyName(),
                vote.getVoter().getFullName(),
                vote.getVoteDate()
            ))
            .toList();
    }
}
