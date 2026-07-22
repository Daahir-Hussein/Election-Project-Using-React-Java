package com.ems.electionmanagement.controller;

import com.ems.electionmanagement.dto.result.ResultResponse;
import com.ems.electionmanagement.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/Results")
@RequiredArgsConstructor
public class ResultsController {

    private final ResultService resultService;

    @GetMapping("/{electionId}")
    public ResponseEntity<List<ResultResponse>> getResults(@PathVariable Long electionId) {
        return ResponseEntity.ok(resultService.getByElectionId(electionId));
    }
}
