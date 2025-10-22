package com.esports.tournament.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "tournament_winners")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TournamentWinner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Tournament tournament;

    @ManyToOne
    private User player;

    @Column(name = "`rank`")
    private int rank;

    private Double prizeAmount;

    private String remarks;

    private LocalDate submittedDate;
}

