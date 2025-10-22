package com.esports.tournament.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "tournament_participants")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class TournamentParticipants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private ParticipantStatus status;

    public enum ParticipantStatus {
        PENDING,
        APPROVED,
        REJECTED
    }

    private LocalDate createdDate;
    private LocalDate updatedDate;
}
