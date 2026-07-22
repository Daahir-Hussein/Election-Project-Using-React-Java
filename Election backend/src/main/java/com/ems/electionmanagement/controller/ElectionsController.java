package com.ems.electionmanagement.controller;

import com.ems.electionmanagement.dto.common.MessageResponse;
import com.ems.electionmanagement.dto.election.ElectionRequest;
import com.ems.electionmanagement.dto.election.ElectionResponse;
import com.ems.electionmanagement.service.ElectionService;
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
@RequestMapping("/api/Elections")
@RequiredArgsConstructor
public class ElectionsController {

    private final ElectionService electionService;

    @GetMapping
    public ResponseEntity<List<ElectionResponse>> getAll() {
        return ResponseEntity.ok(electionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ElectionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(electionService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ElectionResponse> create(@Valid @RequestBody ElectionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(electionService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ElectionResponse> update(
        @PathVariable Long id,
        @Valid @RequestBody ElectionRequest request
    ) {
        return ResponseEntity.ok(electionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable Long id) {
        electionService.delete(id);
        return ResponseEntity.ok(new MessageResponse("Election deleted successfully."));
    }
}
