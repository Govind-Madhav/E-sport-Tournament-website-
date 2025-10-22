package com.esports.tournament.service;


import com.esports.tournament.dto.TournamentDTO;
import com.esports.tournament.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ITournamentService {
    TournamentDTO createTournament(TournamentDTO tournamentDTO, Long hostId, MultipartFile tournamentImage) throws IOException;

    TournamentDTO updateTournament(Long id, TournamentDTO tournamentDTO);

    List<TournamentDTO> getAllTournaments();

    String deleteTournament(Long id);

    List<TournamentDTO> getAllTournamentsByHost(Long hostId);

    TournamentDTO getTournamentById(Long id);

    TournamentDTO toggleTournamentStatus(Long id, boolean isActive);

    List<TournamentDTO> getActivatedTournaments();

    List<TournamentDTO> getNotActivatedTournaments();

    void joinTournament(Long playerId, Long tournamentId);

    void approveOrRejectPlayer(Long playerId, Long tournamentId, boolean isApproved);

    List<User> getPlayersByTournament(Long tournamentId);

    List<User> getParticipantsByTournamentId(Long tournamentId);

    // New method for homepage tournament data
    Map<String, List<TournamentDTO>> getHomepageTournaments();

}

