package com.esports.tournament.controller;

import com.esports.tournament.dto.*;
import com.esports.tournament.model.Role;
import com.esports.tournament.model.User;
import com.esports.tournament.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/host")
public class HostController {

    @Autowired
    private IHostService hostService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ITournamentService tournamentService;

    @Autowired
    private IPaymentService paymentService;

    @Autowired
    private ITournamentWinnerService tournamentWinnerService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerHost(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String mobile,
            @RequestParam MultipartFile file
    ) {
        Map<String, Object> response = new HashMap<>();

        try {
            User host = new User();
            host.setFullName(fullName);
            host.setEmail(email);
            host.setPassword(password);
            host.setMobile(mobile);

            User savedHost = hostService.registerHost(host, file);

            response.put("status", "success");
            response.put("message", "Host registered successfully, forwarded for approval.");
            response.put("data", savedHost);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to register host: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginHost(@RequestBody LoginDTO loginDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            User host = hostService.loginHost(loginDTO.getEmail(), loginDTO.getPassword());
            response.put("status", "success");
            response.put("message", "Host login successful");
            response.put("data", host);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PutMapping("/forgot-password")
    public ResponseEntity<Map<String, Object>> forgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (!forgotPasswordDTO.getNewPassword().equals(forgotPasswordDTO.getConfirmPassword())) {
                response.put("status", "error");
                response.put("message", "New Password and Confirm Password do not match.");
                return ResponseEntity.badRequest().body(response);
            }

            String message = hostService.forgotPassword(forgotPasswordDTO.getEmail(), forgotPasswordDTO.getNewPassword());
            response.put("status", "success");
            response.put("message", message);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    @GetMapping("/get-host/{id}")
    public ResponseEntity<Map<String, Object>> getHostById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            User host = hostService.getHostById(id);
            response.put("status", "success");
            response.put("message", "Host fetched successfully");
            response.put("data", host);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/create-tournament/{hostId}")
    public ResponseEntity<Map<String, Object>> createTournament(
            @PathVariable Long hostId,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam String gameType,
            @RequestParam Integer joiningFee,
            @RequestParam(required = false) MultipartFile file
    ) {
        Map<String, Object> response = new HashMap<>();

        User user = userService.getUserById(hostId);
        if (user == null || !user.getRole().equals(Role.HOST)) {
            response.put("status", "error");
            response.put("message", "Only hosts are allowed to create tournaments.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        try {
            TournamentDTO tournamentDTO = new TournamentDTO(
                    null, name, description, startDate, endDate, gameType, true, hostId, joiningFee, null
            );
            tournamentDTO.calculateRemainingTime();
            TournamentDTO savedTournament = tournamentService.createTournament(tournamentDTO, hostId, file);
            response.put("status", "success");
            response.put("message", "Tournament created successfully,forwared to approval from admin");
            response.put("data", savedTournament);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/get-tournament/{id}")
    public ResponseEntity<Map<String, Object>> getTournamentById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            TournamentDTO tournament = tournamentService.getTournamentById(id);
            response.put("status", "success");
            response.put("message", "Tournament fetched successfully");
            response.put("data", tournament);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/get-tournaments/{hostId}")
    public ResponseEntity<Map<String, Object>> getAllTournamentsByHost(@PathVariable Long hostId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<TournamentDTO> tournaments = tournamentService.getAllTournamentsByHost(hostId);
            response.put("status", "success");
            response.put("message", "Tournaments fetched successfully");
            response.put("data", tournaments);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/update-tournament/{id}")
    public ResponseEntity<Map<String, Object>> updateTournament(
            @PathVariable Long id,
            @RequestBody TournamentDTO tournamentDTO
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            TournamentDTO updatedTournament = tournamentService.updateTournament(id, tournamentDTO);
            response.put("status", "success");
            response.put("message", "Tournament updated successfully");
            response.put("data", updatedTournament);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/active-tournaments")
    public ResponseEntity<Map<String, Object>> getActivatedTournaments() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<TournamentDTO> activatedTournaments = tournamentService.getActivatedTournaments();
            response.put("status", "success");
            response.put("message", "Activated tournaments fetched successfully");
            response.put("data", activatedTournaments);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    @GetMapping("/players/{tournamentId}")
    public ResponseEntity<Map<String, Object>> getPlayers(@PathVariable Long tournamentId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<User> players = tournamentService.getPlayersByTournament(tournamentId);
            response.put("status", "success");
            response.put("message", "Players fetched successfully");
            response.put("data", players);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/payments/{hostId}")
    public ResponseEntity<?> getPaymentsByHost(@PathVariable Long hostId) {

        List<PaymentSummaryDTO> payments = paymentService.getPaymentsByHost(hostId);

        if (payments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(payments);
    }

    @PutMapping("/approve-reject/{playerId}/{tournamentId}")
    public ResponseEntity<Map<String, Object>> approveOrRejectPlayer(@PathVariable Long playerId, @PathVariable Long tournamentId, @RequestParam boolean isApproved) {
        Map<String, Object> response = new HashMap<>();
        try {
            tournamentService.approveOrRejectPlayer(playerId, tournamentId, isApproved);
            response.put("status", "success");
            response.put("message", isApproved ? "Player approved" : "Player rejected");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PostMapping("/announce-winners/{tournamentId}")
    public ResponseEntity<Map<String, Object>> declareWinners(
            @PathVariable Long tournamentId,
            @RequestBody List<WinnerDTO> winners
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            String message = tournamentWinnerService.declareWinners(tournamentId, winners);
            response.put("status", "success");
            response.put("message", message);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to declare winners: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }


    @GetMapping("/participants/{tournamentId}")
    public ResponseEntity<Map<String, Object>> getParticipantsByTournament(@PathVariable Long tournamentId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<User> participants = tournamentService.getParticipantsByTournamentId(tournamentId);
            response.put("status", "success");
            response.put("message", "Participants fetched successfully");
            response.put("data", participants);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/view-leaderboard/{tournamentId}")
    public ResponseEntity<Map<String, Object>> getLeaderboard(@PathVariable Long tournamentId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<LeaderboardDTO> leaderboard = tournamentWinnerService.getLeaderboard(tournamentId);
            response.put("status", "success");
            response.put("message", "Leaderboard fetched successfully");
            response.put("data", leaderboard);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to fetch leaderboard: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


}

