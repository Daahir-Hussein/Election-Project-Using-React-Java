package com.ems.electionmanagement.service;

import com.ems.electionmanagement.dto.party.PartyRequest;
import com.ems.electionmanagement.dto.party.PartyResponse;
import com.ems.electionmanagement.entity.PoliticalParty;
import com.ems.electionmanagement.exception.ConflictException;
import com.ems.electionmanagement.exception.ResourceNotFoundException;
import com.ems.electionmanagement.repository.CandidateRepository;
import com.ems.electionmanagement.repository.PoliticalPartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PoliticalPartyService {

    private final PoliticalPartyRepository partyRepository;
    private final CandidateRepository candidateRepository;

    @Transactional(readOnly = true)
    public List<PartyResponse> getAll() {
        return partyRepository.findAllByOrderByIdAsc().stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public PartyResponse getById(Long id) {
        return toResponse(findEntity(id));
    }

    @Transactional
    public PartyResponse create(PartyRequest request) {
        String name = request.partyName().trim();
        if (partyRepository.existsByPartyNameIgnoreCase(name)) {
            throw new ConflictException("A political party with this name already exists.");
        }

        PoliticalParty party = new PoliticalParty();
        apply(party, request);
        return toResponse(partyRepository.save(party));
    }

    @Transactional
    public PartyResponse update(Long id, PartyRequest request) {
        PoliticalParty party = findEntity(id);
        String name = request.partyName().trim();
        if (partyRepository.existsByPartyNameIgnoreCaseAndIdNot(name, id)) {
            throw new ConflictException("A political party with this name already exists.");
        }
        apply(party, request);
        return toResponse(partyRepository.save(party));
    }

    @Transactional
    public void delete(Long id) {
        PoliticalParty party = findEntity(id);
        if (candidateRepository.countByPartyId(id) > 0) {
            throw new ConflictException(
                "Cannot delete this political party because candidates are registered under it."
            );
        }
        partyRepository.delete(party);
    }

    public PoliticalParty findEntity(Long id) {
        return partyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Political party not found."));
    }

    private void apply(PoliticalParty party, PartyRequest request) {
        party.setPartyName(request.partyName().trim());
        party.setLeaderName(request.leaderName().trim());
        party.setLogo(blankToNull(request.logo()));
    }

    private PartyResponse toResponse(PoliticalParty party) {
        return new PartyResponse(
            party.getId(),
            party.getPartyName(),
            party.getLeaderName(),
            party.getLogo()
        );
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
