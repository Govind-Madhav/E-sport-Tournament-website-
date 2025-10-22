package com.esports.tournament.controller;

import com.esports.tournament.dto.LoginDTO;
import com.esports.tournament.config.AdminConfig;
import com.esports.tournament.dto.PaymentSummaryDTO;
import com.esports.tournament.dto.PlayerDTO;
import com.esports.tournament.dto.TournamentDTO;
import com.esports.tournament.model.User;
import com.esports.tournament.service.IHostService;
import com.esports.tournament.service.IPaymentService;
import com.esports.tournament.service.IPlayerService;
import com.esports.tournament.service.ITournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin")

public class AdminController {

    @Autowired
    private AdminConfig adminConfig;

    @Autowired
    private IHostService hostService;

    @Autowired
    private IPlayerService playerService;

    @Autowired
    private ITournamentService tournamentService;

    @Autowired
    private IPaymentService paymentService;


    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@RequestBody LoginDTO loginDTO) {

        HashMap<String, Object> response = new HashMap<>();

        if (!loginDTO.getEmail().equals(adminConfig.getEmail())) {
            response.put("status", "error");
            response.put("message", "Invalid email");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        if (!loginDTO.getPassword().equals(adminConfig.getPassword())) {
            response.put("status", "error");
            response.put("message", "Invalid password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        response.put("status", "success");
        response.put("message", "Admin logged in successfully");
        return ResponseEntity.ok(response);
    }


    @GetMapping("/pending-hosts")
    public ResponseEntity<Map<String, Object>> getPendingHosts() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<User> pendingHosts = hostService.getAllPendingHosts();
            response.put("status", "success");
            response.put("message", "Pending hosts fetched successfully");
            response.put("data", pendingHosts);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to fetch pending hosts: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/get-hosts")
    public ResponseEntity<Map<String, Object>> getAllHosts() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<User> hosts = hostService.getAllHosts();
            response.put("status", "success");
            response.put("message", "Verified hosts fetched successfully");
            response.put("data", hosts);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    @PutMapping("/approve-host/{id}")
    public ResponseEntity<Map<String, Object>> approveHost(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            User approvedHost = hostService.approveHost(id);
            response.put("status", "success");
            response.put("message", "Host approved successfully");
            response.put("data", approvedHost);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Host approval failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @GetMapping("/verified-hosts")
    public ResponseEntity<Map<String, Object>> getVerifiedHosts() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<User> verifiedHosts = hostService.getAllVerifiedHosts();
            response.put("status", "success");
            response.put("message", "Verified hosts fetched successfully");
            response.put("data", verifiedHosts);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to fetch hosts: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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


    @DeleteMapping("/delete-host/{id}")
    public ResponseEntity<Map<String, Object>> deleteHost(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            String result = hostService.deleteHost(id);
            response.put("status", "success");
            response.put("message", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to delete host: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    /// Manage Players

    @GetMapping("/get-players")
    public ResponseEntity<Map<String, Object>> getAllPlayers() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<PlayerDTO> players = playerService.getAllPlayers();
            response.put("status", "success");
            response.put("message", "Players fetched successfully");
            response.put("data", players);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/pending-players")
    public ResponseEntity<Map<String, Object>> getPendingPlayers() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<User> pendingPlayers = playerService.getAllPendingPlayers();
            response.put("status", "success");
            response.put("message", "Pending players fetched successfully");
            response.put("data", pendingPlayers);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/verified-players")
    public ResponseEntity<Map<String, Object>> getApprovedPlayers() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<User> approvedPlayers = playerService.getAllApprovedPlayers();
            response.put("status", "success");
            response.put("message", "Approved players fetched successfully");
            response.put("data", approvedPlayers);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    @PutMapping("/approve-player/{id}")
    public ResponseEntity<Map<String, Object>> approvePlayer(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            User approvedPlayer = playerService.approvePlayer(id);
            response.put("status", "success");
            response.put("message", "Player approved successfully");
            response.put("data", approvedPlayer);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @GetMapping("/get-player/{id}")
    public ResponseEntity<Map<String, Object>> getPlayerById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            PlayerDTO player = playerService.getPlayerById(id);
            response.put("status", "success");
            response.put("message", "Player fetched successfully");
            response.put("data", player);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/delete-player/{id}")
    public ResponseEntity<Map<String, Object>> deletePlayer(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            String msg = playerService.deletePlayer(id);
            response.put("status", "success");
            response.put("message", msg);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    /// Manage Tournament

    @GetMapping("/get-tournaments")
    public ResponseEntity<Map<String, Object>> getAllTournaments() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<TournamentDTO> tournaments = tournamentService.getAllTournaments();
            response.put("status", "success");
            response.put("message", "All tournaments fetched successfully");
            response.put("data", tournaments);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PutMapping("/toggle-tournament-status/{id}")
    public ResponseEntity<Map<String, Object>> toggleTournamentStatus(
            @PathVariable Long id,
            @RequestParam boolean isActive
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            TournamentDTO updatedTournament = tournamentService.toggleTournamentStatus(id, isActive);
            response.put("status", "success");
            response.put("message", "Tournament status updated successfully");
            response.put("data", updatedTournament);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("get-tournament/{id}")
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

    @GetMapping("/host/{hostId}")
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

    @GetMapping("/inactive-tournaments")
    public ResponseEntity<Map<String, Object>> getNotActivatedTournaments() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<TournamentDTO> notActivatedTournaments = tournamentService.getNotActivatedTournaments();
            response.put("status", "success");
            response.put("message", "Not activated tournaments fetched successfully");
            response.put("data", notActivatedTournaments);
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

    @DeleteMapping("/delete-tournament/{id}")
    public ResponseEntity<Map<String, Object>> deleteTournament(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            String result = tournamentService.deleteTournament(id);
            response.put("status", "success");
            response.put("message", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/view-transactions")
    public ResponseEntity<List<PaymentSummaryDTO>> getAllPayments() {
        List<PaymentSummaryDTO> payments = paymentService.getAllPayments();
        if (payments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(payments);
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

}
