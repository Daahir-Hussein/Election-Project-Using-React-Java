package com.ems.electionmanagement.controller;

import com.ems.electionmanagement.dto.candidate.CandidateRequest;
import com.ems.electionmanagement.dto.candidate.CandidateResponse;
import com.ems.electionmanagement.dto.common.MessageResponse;
import com.ems.electionmanagement.dto.common.UploadResponse;
import com.ems.electionmanagement.service.CandidateService;
import com.ems.electionmanagement.service.FileStorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/Candidates")
@RequiredArgsConstructor
public class CandidatesController {

    private final CandidateService candidateService;
    private final FileStorageService fileStorageService;

    @GetMapping
    public ResponseEntity<List<CandidateResponse>> getAll() {
        return ResponseEntity.ok(candidateService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(candidateService.getById(id));
    }

    @PostMapping
    public ResponseEntity<CandidateResponse> create(@Valid @RequestBody CandidateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(candidateService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CandidateResponse> update(
        @PathVariable Long id,
        @Valid @RequestBody CandidateRequest request
    ) {
        return ResponseEntity.ok(candidateService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable Long id) {
        candidateService.delete(id);
        return ResponseEntity.ok(new MessageResponse("Candidate deleted successfully."));
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadResponse> upload(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeImage(file, "candidates");
        return ResponseEntity.ok(new UploadResponse(fileName));
    }
}
