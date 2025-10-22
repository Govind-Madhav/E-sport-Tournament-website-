package com.esports.tournament.repo;

import com.esports.tournament.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IPaymentRepository extends JpaRepository<Payment, Long> {


    List<Payment> findByPlayerId(Long playerId);


    Optional<Payment> findByTransactionId(String transactionId);


    List<Payment> findByTournamentId(Long tournamentId);
}
