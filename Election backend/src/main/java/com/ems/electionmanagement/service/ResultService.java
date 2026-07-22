package com.ems.electionmanagement.service;

import com.ems.electionmanagement.dto.result.ResultResponse;
import com.ems.electionmanagement.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResultService {

    private final ElectionService electionService;
    private final CandidateRepository candidateRepository;

    @Transactional(readOnly = true)
    public List<ResultResponse> getByElectionId(Long electionId) {
        electionService.findEntity(electionId);
        return candidateRepository.findResultsByElectionId(electionId).stream()
            .map(result -> new ResultResponse(
                result.getCandidateName(),
                result.getPartyName(),
                result.getTotalVotes()
            ))
            .toList();
    }
}
