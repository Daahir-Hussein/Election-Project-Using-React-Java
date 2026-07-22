package com.ems.electionmanagement.service;

import com.ems.electionmanagement.dto.voter.VoterRequest;
import com.ems.electionmanagement.dto.voter.VoterResponse;
import com.ems.electionmanagement.entity.Voter;
import com.ems.electionmanagement.exception.BadRequestException;
import com.ems.electionmanagement.exception.ConflictException;
import com.ems.electionmanagement.exception.ResourceNotFoundException;
import com.ems.electionmanagement.repository.VoteRepository;
import com.ems.electionmanagement.repository.VoterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoterService {

    private final VoterRepository voterRepository;
    private final VoteRepository voteRepository;

    @Transactional(readOnly = true)
    public List<VoterResponse> getAll() {
        return voterRepository.findAllByOrderByIdAsc().stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public VoterResponse getById(Long id) {
        return toResponse(findEntity(id));
    }

    @Transactional
    public VoterResponse create(VoterRequest request) {
        validateAge(request.dateOfBirth());
        String nationalId = request.nationalID().trim();
        if (voterRepository.existsByNationalId(nationalId)) {
            throw new ConflictException("A voter with this National ID already exists.");
        }

        Voter voter = new Voter();
        apply(voter, request);
        return toResponse(voterRepository.save(voter));
    }

    @Transactional
    public VoterResponse update(Long id, VoterRequest request) {
        validateAge(request.dateOfBirth());
        Voter voter = findEntity(id);
        String nationalId = request.nationalID().trim();
        if (voterRepository.existsByNationalIdAndIdNot(nationalId, id)) {
            throw new ConflictException("A voter with this National ID already exists.");
        }
        apply(voter, request);
        return toResponse(voterRepository.save(voter));
    }

    @Transactional
    public void delete(Long id) {
        Voter voter = findEntity(id);
        if (voteRepository.countByVoterId(id) > 0) {
            throw new ConflictException(
                "Cannot delete this voter because they have already participated in an election."
            );
        }
        voterRepository.delete(voter);
    }

    public Voter findEntity(Long id) {
        return voterRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Voter not found."));
    }

    private void validateAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null || !dateOfBirth.isBefore(LocalDate.now())) {
            throw new BadRequestException("Date of birth must be a past date.");
        }
        if (Period.between(dateOfBirth, LocalDate.now()).getYears() < 18) {
            throw new BadRequestException("Voter must be at least 18 years old.");
        }
    }

    private void apply(Voter voter, VoterRequest request) {
        voter.setNationalId(request.nationalID().trim());
        voter.setFullName(request.fullName().trim());
        voter.setGender(request.gender().trim());
        voter.setDateOfBirth(request.dateOfBirth());
        voter.setPhone(request.phone().trim());
        voter.setAddress(request.address().trim());
    }

    private VoterResponse toResponse(Voter voter) {
        return new VoterResponse(
            voter.getId(),
            voter.getNationalId(),
            voter.getFullName(),
            voter.getGender(),
            voter.getDateOfBirth(),
            voter.getPhone(),
            voter.getAddress()
        );
    }
}
