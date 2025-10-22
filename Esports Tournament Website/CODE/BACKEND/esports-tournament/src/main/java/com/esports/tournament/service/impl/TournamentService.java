package com.esports.tournament.service.impl;

import com.esports.tournament.dto.TournamentDTO;
import com.esports.tournament.model.Role;
import com.esports.tournament.model.Tournament;
import com.esports.tournament.model.TournamentParticipants;
import com.esports.tournament.model.User;
import com.esports.tournament.repo.ITournamentParticipantsRepository;
import com.esports.tournament.repo.ITournamentRepository;
import com.esports.tournament.repo.IUserRepository;
import com.esports.tournament.service.ITournamentService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

import static java.awt.SystemColor.text;

@Service
public class TournamentService implements ITournamentService {

    @Autowired
    private ITournamentRepository tournamentRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ITournamentParticipantsRepository tournamentParticipantsRepository;

    @Value("${files.directory}")
    private String uploadDirectory;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public TournamentDTO createTournament(TournamentDTO tournamentDTO, Long hostId, MultipartFile tournamentImage) throws IOException {
        String imageUrl = null;

        if (tournamentImage != null && !tournamentImage.isEmpty()) {
            Path path = Path.of(uploadDirectory);
            if (Files.notExists(path)) {
                Files.createDirectories(path);
            }

            String fileName = UUID.randomUUID() + "_" + StringUtils.cleanPath(Objects.requireNonNull(tournamentImage.getOriginalFilename()));
            Path targetPath = path.resolve(fileName);
            Files.copy(tournamentImage.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);


            imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/images/")
                    .path(fileName)
                    .toUriString();
        }

        Tournament tournament = Tournament.builder()
                .name(tournamentDTO.getName())
                .description(tournamentDTO.getDescription())
                .startDate(tournamentDTO.getStartDate())
                .endDate(tournamentDTO.getEndDate())
                .gameType(tournamentDTO.getGameType())
                .isActive(false)
                .hostId(hostId)
                .imageUrl(imageUrl)
                .joiningFee(tournamentDTO.getJoiningFee())
                .build();

        Tournament savedTournament = tournamentRepository.save(tournament);


        TournamentDTO savedTournamentDTO = new TournamentDTO(
                savedTournament.getId(),
                savedTournament.getName(),
                savedTournament.getDescription(),
                savedTournament.getStartDate(),
                savedTournament.getEndDate(),
                savedTournament.getGameType(),
                savedTournament.isActive(),
                savedTournament.getHostId(),
                savedTournament.getImageUrl(),
                savedTournament.getJoiningFee(),
                null
        );

        savedTournamentDTO.calculateRemainingTime();

        return savedTournamentDTO;
    }


    @Override
    public List<TournamentDTO> getAllTournaments() {
        List<Tournament> tournaments = tournamentRepository.findAll();
        List<TournamentDTO> tournamentDTOs = new ArrayList<>();

        for (Tournament tournament : tournaments) {
            tournamentDTOs.add(new TournamentDTO(
                    tournament.getId(),
                    tournament.getName(),
                    tournament.getDescription(),
                    tournament.getStartDate(),
                    tournament.getEndDate(),
                    tournament.getGameType(),
                    tournament.isActive(),
                    tournament.getHostId(),
                    tournament.getImageUrl(),
                    tournament.getJoiningFee(),
                    null
            ));
        }

        return tournamentDTOs;
    }

    @Override
    public TournamentDTO updateTournament(Long id, TournamentDTO tournamentDTO) {
        Optional<Tournament> tournamentOpt = tournamentRepository.findById(id);
        if (tournamentOpt.isEmpty()) {
            throw new RuntimeException("Tournament not found with ID: " + id);
        }

        Tournament tournament = tournamentOpt.get();
        tournament.setName(tournamentDTO.getName());
        tournament.setDescription(tournamentDTO.getDescription());
        tournament.setStartDate(tournamentDTO.getStartDate());
        tournament.setEndDate(tournamentDTO.getEndDate());
        tournament.setGameType(tournamentDTO.getGameType());
        tournament.setJoiningFee(tournamentDTO.getJoiningFee());


        Tournament updatedTournament = tournamentRepository.save(tournament);

        TournamentDTO updatedTournamentDTO = new TournamentDTO(
                updatedTournament.getId(),
                updatedTournament.getName(),
                updatedTournament.getDescription(),
                updatedTournament.getStartDate(),
                updatedTournament.getEndDate(),
                updatedTournament.getGameType(),
                updatedTournament.isActive(),
                updatedTournament.getHostId(),
                updatedTournament.getImageUrl(),
                updatedTournament.getJoiningFee(),
                null
        );


        updatedTournamentDTO.calculateRemainingTime();

        return updatedTournamentDTO;
    }


    @Override
    public String deleteTournament(Long id) {
        if (!tournamentRepository.existsById(id)) {
            throw new RuntimeException("Tournament not found with ID: " + id);
        }
        tournamentRepository.deleteById(id);
        return "Tournament deleted successfully";
    }

    @Override
    public List<TournamentDTO> getAllTournamentsByHost(Long hostId) {


        Optional<User> byId = userRepository.findById(hostId);
        User user = byId.orElseThrow(() -> new RuntimeException("Host not found with ID: " + hostId));

        if (user.getRole() != Role.HOST) {
            throw new RuntimeException("Host not found with id: " + hostId);
        }


        List<Tournament> tournaments = tournamentRepository.findByHostId(hostId);

        List<TournamentDTO> tournamentDTOs = new ArrayList<>();
        for (Tournament tournament : tournaments) {
            TournamentDTO tournamentDTO = new TournamentDTO(
                    tournament.getId(),
                    tournament.getName(),
                    tournament.getDescription(),
                    tournament.getStartDate(),
                    tournament.getEndDate(),
                    tournament.getGameType(),
                    tournament.isActive(),
                    tournament.getHostId(),
                    tournament.getImageUrl(),
                    tournament.getJoiningFee(),
                    null
            );

            tournamentDTO.calculateRemainingTime();

            tournamentDTOs.add(tournamentDTO);
        }


        if (tournamentDTOs.isEmpty()) {
            throw new RuntimeException("No tournaments found for host: " + hostId);
        } else {
            return tournamentDTOs;
        }
    }


    @Override
    public TournamentDTO getTournamentById(Long id) {
        Optional<Tournament> tournamentOpt = tournamentRepository.findById(id);
        if (tournamentOpt.isEmpty()) {
            throw new RuntimeException("Tournament not found with ID: " + id);
        }

        Tournament tournament = tournamentOpt.get();


        TournamentDTO tournamentDTO = new TournamentDTO(
                tournament.getId(),
                tournament.getName(),
                tournament.getDescription(),
                tournament.getStartDate(),
                tournament.getEndDate(),
                tournament.getGameType(),
                tournament.isActive(),
                tournament.getHostId(),
                tournament.getImageUrl(),
                tournament.getJoiningFee(),
                null
        );


        tournamentDTO.calculateRemainingTime();

        return tournamentDTO;
    }

    @Override
    public TournamentDTO toggleTournamentStatus(Long id, boolean isActive) {
        Optional<Tournament> tournamentOpt = tournamentRepository.findById(id);
        if (tournamentOpt.isEmpty()) {
            throw new RuntimeException("Tournament not found with ID: " + id);
        }

        Tournament tournament = tournamentOpt.get();
        tournament.setActive(isActive);
        Tournament updatedTournament = tournamentRepository.save(tournament);


        TournamentDTO tournamentDTO = new TournamentDTO(
                updatedTournament.getId(),
                updatedTournament.getName(),
                updatedTournament.getDescription(),
                updatedTournament.getStartDate(),
                updatedTournament.getEndDate(),
                updatedTournament.getGameType(),
                updatedTournament.isActive(),
                updatedTournament.getHostId(),
                updatedTournament.getImageUrl(),
                updatedTournament.getJoiningFee(),
                null
        );


        tournamentDTO.calculateRemainingTime();

        return tournamentDTO;
    }

    @Override
    public List<TournamentDTO> getActivatedTournaments() {
        List<Tournament> activatedTournaments = tournamentRepository.findByIsActive(true);
        if(activatedTournaments.isEmpty()){
            throw new RuntimeException("No active Tournaments found");
        }
        List<TournamentDTO> tournamentDTOs = new ArrayList<>();

        for (Tournament tournament : activatedTournaments) {
            TournamentDTO tournamentDTO = new TournamentDTO(
                    tournament.getId(),
                    tournament.getName(),
                    tournament.getDescription(),
                    tournament.getStartDate(),
                    tournament.getEndDate(),
                    tournament.getGameType(),
                    tournament.isActive(),
                    tournament.getHostId(),
                    tournament.getImageUrl(),
                    tournament.getJoiningFee(),
                    null
            );


            tournamentDTO.calculateRemainingTime();

            tournamentDTOs.add(tournamentDTO);
        }

        return tournamentDTOs;
    }

    @Override
    public List<TournamentDTO> getNotActivatedTournaments() {
        List<Tournament> notActivatedTournaments = tournamentRepository.findByIsActive(false);

        if(notActivatedTournaments.isEmpty()){
            throw new RuntimeException("All tournaments activated");
        }
        List<TournamentDTO> tournamentDTOs = new ArrayList<>();

        for (Tournament tournament : notActivatedTournaments) {
            TournamentDTO tournamentDTO = new TournamentDTO(
                    tournament.getId(),
                    tournament.getName(),
                    tournament.getDescription(),
                    tournament.getStartDate(),
                    tournament.getEndDate(),
                    tournament.getGameType(),
                    tournament.isActive(),
                    tournament.getHostId(),
                    tournament.getImageUrl(),
                    tournament.getJoiningFee(),
                    null
            );


            tournamentDTO.calculateRemainingTime();

            tournamentDTOs.add(tournamentDTO);
        }

        return tournamentDTOs;
    }


    @Override
    public void joinTournament(Long playerId, Long tournamentId) {
        Optional<User> playerOpt = userRepository.findById(playerId);
        Optional<Tournament> tournamentOpt = tournamentRepository.findById(tournamentId);

        if (!playerOpt.isPresent() || !tournamentOpt.isPresent()) {
            throw new RuntimeException("Player or Tournament not found");
        }

        User player = playerOpt.get();
        Tournament tournament = tournamentOpt.get();

        if (player.getRole() != Role.PLAYER) {
            throw new RuntimeException("The user is not a player.");
        }

        if (!player.isVerified()) {
            throw new RuntimeException("Player is not verified.");
        }

        if (!tournament.isActive()) {
            throw new RuntimeException("Tournament is not active. Player cannot join.");
        }

        Optional<TournamentParticipants> existingParticipantOpt = tournamentParticipantsRepository
                .findByUserAndTournament(player, tournament);

        if (existingParticipantOpt.isPresent()) {
            TournamentParticipants existingParticipant = existingParticipantOpt.get();


            if (existingParticipant.getStatus() != TournamentParticipants.ParticipantStatus.PENDING) {
                throw new RuntimeException("Player has already been approved or rejected for this tournament.");
            }

            throw new RuntimeException("Player has already requested to join this tournament and is pending approval.");
        }

        TournamentParticipants participant = new TournamentParticipants();
        participant.setUser(player);
        participant.setTournament(tournament);
        participant.setStatus(TournamentParticipants.ParticipantStatus.PENDING);

        LocalDate now = LocalDate.now();
        participant.setCreatedDate(now);
        participant.setUpdatedDate(now);

        tournamentParticipantsRepository.save(participant);
    }


    @Override
    public List<User> getPlayersByTournament(Long tournamentId) {
        Optional<Tournament> tournamentOpt = tournamentRepository.findById(tournamentId);

        if (!tournamentOpt.isPresent()) {
            throw new RuntimeException("Tournament not found");
        }

        Tournament tournament = tournamentOpt.get();
        return tournamentParticipantsRepository.findByTournament(tournament).stream()
                .map(TournamentParticipants::getUser)
                .toList();
    }

    @Override
    public void approveOrRejectPlayer(Long playerId, Long tournamentId, boolean isApproved) {
        Optional<User> playerOpt = userRepository.findById(playerId);
        Optional<Tournament> tournamentOpt = tournamentRepository.findById(tournamentId);

        if (!playerOpt.isPresent() || !tournamentOpt.isPresent()) {
            throw new RuntimeException("Player or Tournament not found");
        }

        User player = playerOpt.get();
        Tournament tournament = tournamentOpt.get();

        if (player.getRole() != Role.PLAYER) {
            throw new RuntimeException("The user is not a player.");
        }

        if (!player.isVerified()) {
            throw new RuntimeException("The player is not verified.");
        }

        if (!tournament.isActive()) {
            throw new RuntimeException("Tournament is not active. Player cannot be approved or rejected.");
        }

        Optional<TournamentParticipants> participantOpt = tournamentParticipantsRepository
                .findByUserAndTournament(player, tournament);

        if (!participantOpt.isPresent()) {
            throw new RuntimeException("Player is not part of this tournament.");
        }

        TournamentParticipants participant = participantOpt.get();

        if (participant.getStatus() == TournamentParticipants.ParticipantStatus.REJECTED) {
            participant.setStatus(TournamentParticipants.ParticipantStatus.PENDING);
        }

        participant.setStatus(isApproved ? TournamentParticipants.ParticipantStatus.APPROVED : TournamentParticipants.ParticipantStatus.REJECTED);
        participant.setUpdatedDate(LocalDate.now());

        TournamentParticipants save = tournamentParticipantsRepository.save(participant);


        sendEmail(player, tournament, isApproved);
    }

    private void sendEmail(User player, Tournament tournament, boolean isApproved) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Set the "From" address explicitly
            helper.setFrom("govindmadhav003@gmail.com"); // Your Gmail address or any other sender email

            helper.setTo(player.getEmail());

            String subject = isApproved
                    ? "Congratulations! You're Approved for " + tournament.getName()
                    : "Update on Your Tournament Request: " + tournament.getName();
            helper.setSubject(subject);

            StringBuilder emailBody = new StringBuilder();
            emailBody.append("<html><body style='font-family: Arial, sans-serif;'>");
            emailBody.append("<p>Dear <strong>").append(player.getFullName()).append("</strong>,</p>");

            if (isApproved) {
                emailBody.append("<h2 style='color: green;'>Congratulations!</h2>")
                        .append("<p>Your request to join the tournament <strong>")
                        .append(tournament.getName())
                        .append("</strong> has been <strong>approved</strong>.</p>")
                        .append("<p>We wish you the best of luck!</p>");
            } else {
                emailBody.append("<h2 style='color: red;'>Sorry!</h2>")
                        .append("<p>Your request to join the tournament <strong>")
                        .append(tournament.getName())
                        .append("</strong> has been <strong>rejected</strong>.</p>")
                        .append("<p>Better luck next time!</p>");
            }

            emailBody.append("<hr>")
                    .append("<p style='font-size:12px;color:gray;'>This is an automated message from the E-Sports Tournament Portal.</p>")
                    .append("</body></html>");

            helper.setText(emailBody.toString(), true); // true = HTML content

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Something went wrong while sending the email. Please try again later.", e);
        }
    }

    @Override
    public List<User> getParticipantsByTournamentId(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found with ID: " + tournamentId));

        List<TournamentParticipants> participants = tournamentParticipantsRepository.findByTournament(tournament);

        if (participants.isEmpty()) {
            throw new RuntimeException("No participants found for this tournament.");
        }

        return participants.stream()
                .map(TournamentParticipants::getUser)
                .toList();
    }

    /**
     * Get tournaments for homepage - separates ongoing and upcoming tournaments
     * @return Map with "ongoing" and "upcoming" tournament lists
     */
    @Override
    public Map<String, List<TournamentDTO>> getHomepageTournaments() {
        LocalDate currentDate = LocalDate.now();
        List<Tournament> allActiveTournaments = tournamentRepository.findByIsActive(true);
        
        List<TournamentDTO> ongoingTournaments = new ArrayList<>();
        List<TournamentDTO> upcomingTournaments = new ArrayList<>();
        
        for (Tournament tournament : allActiveTournaments) {
            TournamentDTO tournamentDTO = new TournamentDTO(
                    tournament.getId(),
                    tournament.getName(),
                    tournament.getDescription(),
                    tournament.getStartDate(),
                    tournament.getEndDate(),
                    tournament.getGameType(),
                    tournament.isActive(),
                    tournament.getHostId(),
                    tournament.getImageUrl(),
                    tournament.getJoiningFee(),
                    null
            );
            
            tournamentDTO.calculateRemainingTime();
            
            // Categorize tournaments based on dates
            if (currentDate.isAfter(tournament.getStartDate()) && currentDate.isBefore(tournament.getEndDate())) {
                // Tournament is ongoing (current date is between start and end date)
                ongoingTournaments.add(tournamentDTO);
            } else if (currentDate.isBefore(tournament.getStartDate())) {
                // Tournament is upcoming (current date is before start date)
                upcomingTournaments.add(tournamentDTO);
            }
        }
        
        Map<String, List<TournamentDTO>> result = new HashMap<>();
        result.put("ongoing", ongoingTournaments);
        result.put("upcoming", upcomingTournaments);
        
        return result;
    }


}











