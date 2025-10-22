package com.esports.tournament.service.impl;

import com.esports.tournament.dto.PaymentDTO;
import com.esports.tournament.dto.PaymentSummaryDTO;
import com.esports.tournament.dto.TournamentDTO;
import com.esports.tournament.model.Payment;
import com.esports.tournament.model.User;
import com.esports.tournament.model.Tournament;
import com.esports.tournament.repo.IPaymentRepository;
import com.esports.tournament.repo.IUserRepository;
import com.esports.tournament.repo.ITournamentRepository;
import com.esports.tournament.service.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService implements IPaymentService {

    @Autowired
    private IPaymentRepository paymentRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ITournamentRepository tournamentRepository;

    @Override
    public PaymentSummaryDTO makePayment(PaymentDTO paymentDTO) {

        User player = userRepository.findById(paymentDTO.getPlayerId())
                .orElseThrow(() -> new RuntimeException("Player not found"));

        Tournament tournament = tournamentRepository.findById(paymentDTO.getTournamentId())
                .orElseThrow(() -> new RuntimeException("Tournament not found"));

        String transactionId = UUID.randomUUID().toString();

        Payment payment = new Payment();
        payment.setPlayer(player);
        payment.setTournament(tournament);
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setTransactionId(transactionId);
        payment.setStatus(true);

        if ("Card".equalsIgnoreCase(paymentDTO.getPaymentMethod())) {
            payment.setCardNumber(paymentDTO.getCardNumber());
            payment.setCardExpiryDate(paymentDTO.getCardExpiryDate());
            payment.setCardCVC(paymentDTO.getCardCVC());
        }

        if ("UPI".equalsIgnoreCase(paymentDTO.getPaymentMethod())) {
            payment.setUpiId(paymentDTO.getUpiId());
        }

        payment.setCreatedDate(LocalDate.now());
        payment.setUpdatedDate(LocalDate.now());

        paymentRepository.save(payment);


        return new PaymentSummaryDTO(
                payment.getPlayer().getId(),
                payment.getTournament().getId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getTransactionId(),
                payment.getStatus()
        );
    }


    @Override
    public List<TournamentDTO> getAllPaidTournaments(Long playerId) {
        List<Payment> payments = paymentRepository.findByPlayerId(playerId);

        if (payments.isEmpty()) {
            throw new RuntimeException("No paid tournaments found for player: " + playerId);
        }


        return payments.stream()
                .map(payment -> {
                    Tournament tournament = payment.getTournament();


                    Duration duration = Duration.between(LocalDate.now().atStartOfDay(), tournament.getStartDate().atStartOfDay());
                    long days = duration.toDays();
                    long hours = duration.toHours() % 24;
                    long minutes = duration.toMinutes() % 60;
                    String remainingTime = String.format("%d days, %d hours, %d minutes", days, hours, minutes);

                    return new TournamentDTO(
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
                            remainingTime
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentSummaryDTO> getPaymentsByHost(Long hostId) {

        List<Tournament> tournaments = tournamentRepository.findByHostId(hostId);
        List<PaymentSummaryDTO> paymentSummaries = new ArrayList<>();

        for (Tournament tournament : tournaments) {
            List<Payment> payments = paymentRepository.findByTournamentId(tournament.getId());
            for (Payment payment : payments) {
                PaymentSummaryDTO paymentSummaryDTO = new PaymentSummaryDTO(
                        payment.getPlayer().getId(),
                        payment.getTournament().getId(),
                        payment.getAmount(),
                        payment.getPaymentMethod(),
                        payment.getTransactionId(),
                        payment.getStatus()
                );
                paymentSummaries.add(paymentSummaryDTO);
            }
        }

        return paymentSummaries;
    }


    @Override
    public List<PaymentSummaryDTO> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        if (payments.isEmpty()) {
            throw new RuntimeException("No payments found");
        }

        return payments.stream()
                .map(payment -> new PaymentSummaryDTO(
                        payment.getPlayer().getId(),
                        payment.getTournament().getId(),
                        payment.getAmount(),
                        payment.getPaymentMethod(),
                        payment.getTransactionId(),
                        payment.getStatus()
                ))
                .collect(Collectors.toList());
    }
}
