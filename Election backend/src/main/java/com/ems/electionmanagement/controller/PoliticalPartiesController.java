package com.ems.electionmanagement.controller;

import com.ems.electionmanagement.dto.common.MessageResponse;
import com.ems.electionmanagement.dto.common.UploadResponse;
import com.ems.electionmanagement.dto.party.PartyRequest;
import com.ems.electionmanagement.dto.party.PartyResponse;
import com.ems.electionmanagement.service.FileStorageService;
import com.ems.electionmanagement.service.PoliticalPartyService;
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
@RequestMapping("/api/PoliticalParties")
@RequiredArgsConstructor
public class PoliticalPartiesController {

    private final PoliticalPartyService partyService;
    private final FileStorageService fileStorageService;

    @GetMapping
    public ResponseEntity<List<PartyResponse>> getAll() {
        return ResponseEntity.ok(partyService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartyResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(partyService.getById(id));
    }

    @PostMapping
    public ResponseEntity<PartyResponse> create(@Valid @RequestBody PartyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(partyService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartyResponse> update(
        @PathVariable Long id,
        @Valid @RequestBody PartyRequest request
    ) {
        return ResponseEntity.ok(partyService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable Long id) {
        partyService.delete(id);
        return ResponseEntity.ok(new MessageResponse("Political party deleted successfully."));
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadResponse> upload(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeImage(file, "parties");
        return ResponseEntity.ok(new UploadResponse(fileName));
    }
}
