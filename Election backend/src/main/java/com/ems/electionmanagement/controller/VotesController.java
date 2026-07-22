package com.ems.electionmanagement.controller;

import com.ems.electionmanagement.dto.vote.VoteRecordResponse;
import com.ems.electionmanagement.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/Votes")
@RequiredArgsConstructor
public class VotesController {

    private final VoteService voteService;

    @GetMapping
    public ResponseEntity<List<VoteRecordResponse>> getAll() {
        return ResponseEntity.ok(voteService.getAll());
    }
}
