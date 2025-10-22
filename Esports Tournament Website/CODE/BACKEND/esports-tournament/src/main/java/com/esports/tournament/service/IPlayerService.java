package com.esports.tournament.service;

import com.esports.tournament.dto.PlayerDTO;
import com.esports.tournament.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IPlayerService {
    PlayerDTO registerPlayer(PlayerDTO playerDTO, MultipartFile imageFile) throws IOException;

    PlayerDTO loginPlayer(String email, String password);

    String forgotPassword(String email, String newPassword);

    List<PlayerDTO> getAllPlayers();

    PlayerDTO getPlayerById(Long id);

    String deletePlayer(Long id);

    List<User> getAllPendingPlayers();

    User approvePlayer(Long id);

    List<User> getAllApprovedPlayers();

    PlayerDTO updatePlayer(Long id, PlayerDTO playerDTO, MultipartFile imageFile) throws IOException;
}

