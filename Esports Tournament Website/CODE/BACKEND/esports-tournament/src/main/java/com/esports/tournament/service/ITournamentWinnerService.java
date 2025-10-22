package com.esports.tournament.service;

import com.esports.tournament.dto.LeaderboardDTO;
import com.esports.tournament.dto.WinnerDTO;
import com.esports.tournament.model.TournamentWinner;

import java.util.List;

public interface ITournamentWinnerService {

    String declareWinners(Long tournamentId, List<WinnerDTO> winners);


    public List<LeaderboardDTO> getLeaderboard(Long tournamentId);
}
