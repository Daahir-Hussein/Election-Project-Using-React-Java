package com.ems.electionmanagement.controller;

import com.ems.electionmanagement.dto.common.MessageResponse;
import com.ems.electionmanagement.dto.voter.VoterRequest;
import com.ems.electionmanagement.dto.voter.VoterResponse;
import com.ems.electionmanagement.service.VoterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/Voters")
@RequiredArgsConstructor
public class VotersController {

    private final VoterService voterService;

    @GetMapping
    public ResponseEntity<List<VoterResponse>> getAll() {
        return ResponseEntity.ok(voterService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VoterResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(voterService.getById(id));
    }

    @PostMapping
    public ResponseEntity<VoterResponse> create(@Valid @RequestBody VoterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(voterService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VoterResponse> update(
        @PathVariable Long id,
        @Valid @RequestBody VoterRequest request
    ) {
        return ResponseEntity.ok(voterService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable Long id) {
        voterService.delete(id);
        return ResponseEntity.ok(new MessageResponse("Voter deleted successfully."));
    }
}
