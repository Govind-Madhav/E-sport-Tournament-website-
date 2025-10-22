package com.esports.tournament.service.impl;

import com.esports.tournament.dto.PlayerDTO;
import com.esports.tournament.model.Role;
import com.esports.tournament.model.User;
import com.esports.tournament.repo.IUserRepository;
import com.esports.tournament.service.IPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Service
public class PlayerService implements IPlayerService {

    @Autowired
    private IUserRepository userRepository;

    @Value("${files.directory}")
    private String uploadDirectory;

    @Override
    public PlayerDTO registerPlayer(PlayerDTO dto, MultipartFile imageFile) throws IOException {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Player already registered with this email.");
        }

        Path path = Path.of(uploadDirectory);
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }

        String fileName = UUID.randomUUID() + "_" + StringUtils.cleanPath(Objects.requireNonNull(imageFile.getOriginalFilename()));
        Path targetPath = path.resolve(fileName);
        Files.copy(imageFile.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/images/")
                .path(fileName)
                .toUriString();

        User player = User.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .mobile(dto.getMobile())
                .role(Role.PLAYER)
                .verified(false)
                .imageUrl(imageUrl)
                .build();

        User saved = userRepository.save(player);

        return new PlayerDTO(
                saved.getFullName(),
                saved.getEmail(),
                saved.getPassword(),
                saved.getMobile(),
                saved.getImageUrl()
        );
    }

    @Override
    public PlayerDTO loginPlayer(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty() || userOpt.get().getRole() != Role.PLAYER) {
            throw new RuntimeException("Player not found or not authorized.");
        }
        User player = userOpt.get();

        if (!player.isVerified()) {
            throw new RuntimeException("Player is not yet verified by admin.");
        }
        if (!player.getPassword().equals(password)) {
            throw new RuntimeException("Incorrect password.");
        }

        return new PlayerDTO(
                player.getFullName(),
                player.getEmail(),
                player.getPassword(),
                player.getMobile(),
                player.getImageUrl()
        );
    }

    @Override
    public String forgotPassword(String email, String newPassword) {
        User player = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Player not found with email: " + email));

        if (player.getRole() != Role.PLAYER) {
            throw new RuntimeException("This user is not registered as a player.");
        }

        player.setPassword(newPassword);
        userRepository.save(player);

        return "Password updated successfully for player.";
    }


    @Override
    public List<PlayerDTO> getAllPlayers() {
        List<User> players = userRepository.findByRole(Role.PLAYER);
        if (players.isEmpty()) {
            throw new RuntimeException("No players found");
        }
        List<PlayerDTO> list = new ArrayList<>();
        for (User u : players) {
            list.add(new PlayerDTO(
                    u.getFullName(),
                    u.getEmail(),
                    u.getPassword(),
                    u.getMobile(),
                    u.getImageUrl()
            ));
        }

        return list;
    }

    @Override
    public List<User> getAllApprovedPlayers() {
        List<User> players = userRepository.findByRoleAndVerified(Role.PLAYER, true);
        if (players.isEmpty()) {
            throw new RuntimeException("No approved players found.");
        }
        return players;
    }

    @Override
    public PlayerDTO getPlayerById(Long id) {
        Optional<User> playerOpt = userRepository.findById(id);
        if (playerOpt.isEmpty() || playerOpt.get().getRole() != Role.PLAYER) {
            throw new RuntimeException("Player not found with ID: " + id);
        }
        User u = playerOpt.get();
        return new PlayerDTO(
                u.getFullName(),
                u.getEmail(),
                u.getPassword(),
                u.getMobile(),
                u.getImageUrl()
        );
    }


    public List<User> getAllPendingPlayers() {
        List<User> byRoleAndVerified = userRepository.findByRoleAndVerified(Role.PLAYER, false);
        if (byRoleAndVerified.isEmpty()) {
            throw new RuntimeException("no pending players found");
        }
        return byRoleAndVerified;
    }

    public User approvePlayer(Long id) {
        User player = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Player not found"));
        player.setVerified(true);
        return userRepository.save(player);
    }

    @Override
    public String deletePlayer(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Player not found");
        }
        userRepository.deleteById(id);
        return "Player deleted successfully";
    }

    @Override
    public PlayerDTO updatePlayer(Long id, PlayerDTO dto, MultipartFile imageFile) throws IOException {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty() || userOpt.get().getRole() != Role.PLAYER) {
            throw new RuntimeException("Player not found with ID: " + id);
        }
        User player = userOpt.get();
        player.setFullName(dto.getFullName());
        player.setEmail(dto.getEmail());
        player.setPassword(dto.getPassword());
        player.setMobile(dto.getMobile());

        if (imageFile != null && !imageFile.isEmpty()) {
            Path path = Path.of(uploadDirectory);
            if (Files.notExists(path)) {
                Files.createDirectories(path);
            }

            String fileName = UUID.randomUUID() + "_" + StringUtils.cleanPath(Objects.requireNonNull(imageFile.getOriginalFilename()));
            Path targetPath = path.resolve(fileName);
            Files.copy(imageFile.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/images/")
                    .path(fileName)
                    .toUriString();

            player.setImageUrl(imageUrl);
        }
        User updatedPlayer = userRepository.save(player);
        return new PlayerDTO(
                updatedPlayer.getFullName(),
                updatedPlayer.getEmail(),
                updatedPlayer.getPassword(),
                updatedPlayer.getMobile(),
                updatedPlayer.getImageUrl()
        );
    }

}