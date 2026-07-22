package com.ems.electionmanagement.service;

import com.ems.electionmanagement.dto.dashboard.DashboardResponse;
import com.ems.electionmanagement.repository.CandidateRepository;
import com.ems.electionmanagement.repository.ElectionRepository;
import com.ems.electionmanagement.repository.PoliticalPartyRepository;
import com.ems.electionmanagement.repository.VoteRepository;
import com.ems.electionmanagement.repository.VoterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ElectionRepository electionRepository;
    private final PoliticalPartyRepository partyRepository;
    private final CandidateRepository candidateRepository;
    private final VoterRepository voterRepository;
    private final VoteRepository voteRepository;

    @Transactional(readOnly = true)
    public DashboardResponse getDashboard() {
        return new DashboardResponse(
            electionRepository.count(),
            partyRepository.count(),
            candidateRepository.count(),
            voterRepository.count(),
            voteRepository.count()
        );
    }
}
