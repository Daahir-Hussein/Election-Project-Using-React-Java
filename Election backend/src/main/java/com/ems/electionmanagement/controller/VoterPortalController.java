package com.ems.electionmanagement.controller;

import com.ems.electionmanagement.dto.vote.VoteRequest;
import com.ems.electionmanagement.dto.voter.VoterCandidateResponse;
import com.ems.electionmanagement.dto.voter.VoterElectionResponse;
import com.ems.electionmanagement.dto.voter.VoterLoginRequest;
import com.ems.electionmanagement.dto.voter.VoterLoginResponse;
import com.ems.electionmanagement.service.VoterPortalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/VoterPortal")
@RequiredArgsConstructor
public class VoterPortalController {

    private final VoterPortalService voterPortalService;

    @PostMapping("/Login")
    public ResponseEntity<VoterLoginResponse> login(
        @Valid @RequestBody VoterLoginRequest request
    ) {
        return ResponseEntity.ok(voterPortalService.login(request.nationalID()));
    }

    @GetMapping("/Elections")
    public ResponseEntity<List<VoterElectionResponse>> getElections() {
        return ResponseEntity.ok(voterPortalService.getOpenElections());
    }

    @GetMapping("/Candidates/{electionId}")
    public ResponseEntity<List<VoterCandidateResponse>> getCandidates(
        @PathVariable Long electionId
    ) {
        return ResponseEntity.ok(voterPortalService.getCandidates(electionId));
    }

    @PostMapping("/Vote")
    public ResponseEntity<String> vote(@Valid @RequestBody VoteRequest request) {
        voterPortalService.castVote(request);
        return ResponseEntity.ok("Vote submitted successfully.");
    }
}
