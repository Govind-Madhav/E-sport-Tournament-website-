package com.esports.tournament.repo;

import com.esports.tournament.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITournamentRepository extends JpaRepository<Tournament, Long> {
    List<Tournament> findByHostId(Long hostId);

    List<Tournament> findByIsActive(boolean isActive);
}
