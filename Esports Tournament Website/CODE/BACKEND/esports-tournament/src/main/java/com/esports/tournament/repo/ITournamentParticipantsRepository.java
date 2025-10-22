package com.esports.tournament.repo;

import com.esports.tournament.model.Tournament;
import com.esports.tournament.model.TournamentParticipants;
import com.esports.tournament.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ITournamentParticipantsRepository extends JpaRepository<TournamentParticipants, Long> {

    List<TournamentParticipants> findByTournament(Tournament tournament);


    Optional<TournamentParticipants> findByTournamentAndUser(Tournament tournament, User user);


    Optional<TournamentParticipants> findByUserAndTournament(User user, Tournament tournament);


    List<TournamentParticipants> findByTournamentAndStatus(Tournament tournament, TournamentParticipants.ParticipantStatus status);
}
