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

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "political_parties")
@Getter
@Setter
@NoArgsConstructor
public class PoliticalParty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_id")
    private Long id;

    @Column(name = "party_name", nullable = false, unique = true, length = 150)
    private String partyName;

    @Column(name = "leader_name", nullable = false, length = 150)
    private String leaderName;

    @Column(length = 255)
    private String logo;

    @JsonIgnore
    @OneToMany(mappedBy = "party")
    private List<Candidate> candidates = new ArrayList<>();
}
