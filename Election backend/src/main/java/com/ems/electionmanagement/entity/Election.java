package com.ems.electionmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "elections")
@Getter
@Setter
@NoArgsConstructor
public class Election {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "election_id")
    private Long id;

    @Column(name = "election_name", nullable = false, unique = true, length = 150)
    private String electionName;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, length = 20)
    private String status;

    @JsonIgnore
    @OneToMany(mappedBy = "election")
    private List<Candidate> candidates = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "election")
    private List<Vote> votes = new ArrayList<>();
}
