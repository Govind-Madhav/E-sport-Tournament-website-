package com.esports.tournament.repo;

import com.esports.tournament.model.TournamentWinner;
import com.esports.tournament.model.Tournament;
import com.esports.tournament.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITournamentWinnerRepository extends JpaRepository<TournamentWinner, Long> {

    List<TournamentWinner> findByTournament(Tournament tournament);

    List<TournamentWinner> findByPlayer(User player);

}
