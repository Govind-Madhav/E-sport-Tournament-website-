package com.esports.tournament.service.impl;

import com.esports.tournament.dto.LeaderboardDTO;
import com.esports.tournament.dto.WinnerDTO;
import com.esports.tournament.model.Tournament;
import com.esports.tournament.model.TournamentWinner;
import com.esports.tournament.model.User;
import com.esports.tournament.repo.ITournamentRepository;
import com.esports.tournament.repo.ITournamentWinnerRepository;
import com.esports.tournament.repo.IUserRepository;
import com.esports.tournament.service.ITournamentWinnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TournamentWinnerService implements ITournamentWinnerService {

    @Autowired
    private ITournamentWinnerRepository winnerRepo;

    @Autowired
    private ITournamentRepository tournamentRepo;

    @Autowired
    private IUserRepository userRepo;

    @Override
    public String declareWinners(Long tournamentId, List<WinnerDTO> winners) {
        Tournament tournament = tournamentRepo.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));

        for (WinnerDTO dto : winners) {
            User player = userRepo.findById(dto.getPlayerId())
                    .orElseThrow(() -> new RuntimeException("Player not found"));

            TournamentWinner winner = new TournamentWinner();
            winner.setTournament(tournament);
            winner.setPlayer(player);
            winner.setRank(dto.getRank());
            winner.setPrizeAmount(dto.getPrizeAmount());
            winner.setRemarks(dto.getRemarks());
            winner.setSubmittedDate(LocalDate.now());

            winnerRepo.save(winner);
        }

        return "Winners declared successfully.";
    }

    @Override
    public List<LeaderboardDTO> getLeaderboard(Long tournamentId) {

        Tournament tournament = tournamentRepo.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found with ID: " + tournamentId));

        List<TournamentWinner> winners = winnerRepo.findByTournament(tournament);

        if (winners.isEmpty()) {
            throw new RuntimeException("No winners found for tournament ID: " + tournamentId);
        }

        List<LeaderboardDTO> leaderboard = new ArrayList<>();

        for (TournamentWinner winner : winners) {
            LeaderboardDTO dto = new LeaderboardDTO();
            dto.setPlayerId(winner.getPlayer().getId());
            dto.setFullName(winner.getPlayer().getFullName());
            dto.setRank(winner.getRank());
            dto.setPrizeAmount(winner.getPrizeAmount());
            dto.setRemarks(winner.getRemarks());
            dto.setProfileImage(winner.getPlayer().getImageUrl());
            leaderboard.add(dto);
        }

        leaderboard = leaderboard.stream()
                .sorted(Comparator.comparingInt(LeaderboardDTO::getRank))
                .collect(Collectors.toList());

        return leaderboard;
    }


}

