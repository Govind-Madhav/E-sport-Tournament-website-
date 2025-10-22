package com.esports.tournament.controller;

import com.esports.tournament.dto.*;
import com.esports.tournament.model.Role;
import com.esports.tournament.model.User;
import com.esports.tournament.repo.IUserRepository;
import com.esports.tournament.service.IPaymentService;
import com.esports.tournament.service.ITournamentService;
import com.esports.tournament.service.ITournamentWinnerService;
import com.esports.tournament.service.impl.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/player")
public class PlayerController {

    @Autowired
    private IUserRepository repo;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private ITournamentService tournamentService;

    @Autowired
    private IPaymentService paymentService;

    @Autowired
    private ITournamentWinnerService tournamentWinnerService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerPlayer(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String mobile,
            @RequestParam MultipartFile file
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            PlayerDTO dto = new PlayerDTO(fullName, email, password, mobile, null);
            PlayerDTO saved = playerService.registerPlayer(dto, file);
            response.put("status", "success");
            response.put("message", "Player registered successfully. Awaiting admin approval.");
            response.put("data", saved);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginPlayer(@RequestBody LoginDTO loginDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            PlayerDTO player = playerService.loginPlayer(loginDTO.getEmail(), loginDTO.getPassword());
            response.put("status", "success");
            response.put("message", "Player login successful");
            response.put("data", repo.findByEmail(loginDTO.getEmail()));
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
                response.put("message", "New password and confirm password do not match.");
                return ResponseEntity.badRequest().body(response);
            }

            String message = playerService.forgotPassword(forgotPasswordDTO.getEmail(), forgotPasswordDTO.getNewPassword());
            response.put("status", "success");
            response.put("message", message);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
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

    @PutMapping("/update-profile/{id}")
    public ResponseEntity<Map<String, Object>> updatePlayerProfile(
            @PathVariable Long id,
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String mobile,
            @RequestParam(required = false) MultipartFile file
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            PlayerDTO dto = new PlayerDTO(fullName, email, password, mobile, null);
            PlayerDTO updatedPlayer = playerService.updatePlayer(id, dto, file);
            response.put("status", "success");
            response.put("message", "Player profile updated successfully");
            response.put("data", updatedPlayer);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (IOException e) {
            response.put("status", "error");
            response.put("message", "Failed to upload image: " + e.getMessage());
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

    @PostMapping("/join/{playerId}/{tournamentId}")
    public ResponseEntity<Map<String, Object>> joinTournament(@PathVariable Long playerId, @PathVariable Long tournamentId) {
        Map<String, Object> response = new HashMap<>();
        try {
            tournamentService.joinTournament(playerId, tournamentId);
            response.put("status", "success");
            response.put("message", "Player successfully joined the tournament. Request forwarded for approval");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    /// Payment
    @PostMapping("/make-payment/{playerId}/{tournamentId}")
    public ResponseEntity<Map<String, Object>> makePayment(
            @PathVariable Long playerId,
            @PathVariable Long tournamentId,
            @RequestBody PaymentDTO paymentDTO) {

        Map<String, Object> response = new HashMap<>();

        try {
            paymentDTO.setPlayerId(playerId);
            paymentDTO.setTournamentId(tournamentId);
            PaymentSummaryDTO savedPayment = paymentService.makePayment(paymentDTO);

            response.put("status", "success");
            response.put("message", "Payment successful");
            response.put("data", savedPayment);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error processing payment: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }


    @GetMapping("/paid-tournaments/{playerId}")
    public ResponseEntity<Map<String, Object>> getAllPaidTournaments(@PathVariable Long playerId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<TournamentDTO> paidTournaments = paymentService.getAllPaidTournaments(playerId);
            response.put("status", "success");
            response.put("message", "Paid tournaments fetched successfully");
            response.put("data", paidTournaments);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
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

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Get tournaments for homepage - returns ongoing and upcoming tournaments
     * This endpoint is accessible without authentication for public homepage display
     */
    @GetMapping("/homepage-tournaments")
    public ResponseEntity<Map<String, Object>> getHomepageTournaments() {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, List<TournamentDTO>> tournaments = tournamentService.getHomepageTournaments();
            response.put("status", "success");
            response.put("message", "Homepage tournaments fetched successfully");
            response.put("data", tournaments);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to fetch homepage tournaments: " + e.getMessage());
            response.put("data", Map.of("ongoing", List.of(), "upcoming", List.of()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Unified login endpoint that automatically detects user role
     * This replaces the need for manual role selection in frontend
     */
    @PostMapping("/unified-login")
    public ResponseEntity<Map<String, Object>> unifiedLogin(@RequestBody LoginDTO loginDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Find user by email
            User user = repo.findByEmail(loginDTO.getEmail());
            
            if (user == null) {
                response.put("status", "error");
                response.put("message", "User not found with this email address");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // Check password (in a real app, you'd hash and compare passwords)
            if (!user.getPassword().equals(loginDTO.getPassword())) {
                response.put("status", "error");
                response.put("message", "Invalid password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // Check if user is verified
            if (!user.isVerified()) {
                response.put("status", "error");
                response.put("message", "Account not verified. Please wait for admin approval.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            // Return user data with role information
            response.put("status", "success");
            response.put("message", "Login successful");
            response.put("data", user);
            response.put("role", user.getRole().toString());
            response.put("redirectPath", getRedirectPath(user.getRole()));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Helper method to determine redirect path based on user role
     */
    private String getRedirectPath(Role role) {
        switch (role) {
            case PLAYER:
                return "/userHomePage";
            case HOST:
                return "/hostPage";
            default:
                return "/";
        }
    }

    /**
     * Admin login endpoint - handles admin authentication separately
     */
    @PostMapping("/admin-login")
    public ResponseEntity<Map<String, Object>> adminLogin(@RequestBody LoginDTO loginDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Check admin credentials (you might want to move this to a service)
            if (!loginDTO.getEmail().equals("admin@gmail.com") || !loginDTO.getPassword().equals("admin")) {
                response.put("status", "error");
                response.put("message", "Invalid admin credentials");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // Create admin user object for response
            User adminUser = new User();
            adminUser.setId(0L); // Special ID for admin
            adminUser.setFullName("Admin");
            adminUser.setEmail("admin@gmail.com");
            adminUser.setRole(Role.ADMIN);
            adminUser.setVerified(true);

            response.put("status", "success");
            response.put("message", "Admin login successful");
            response.put("data", adminUser);
            response.put("role", "ADMIN");
            response.put("redirectPath", "/adminPage");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Admin login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}







