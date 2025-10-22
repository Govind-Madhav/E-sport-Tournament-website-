package com.esports.tournament.service;

import com.esports.tournament.dto.PaymentDTO;
import com.esports.tournament.dto.PaymentSummaryDTO;
import com.esports.tournament.dto.TournamentDTO;

import java.util.List;

public interface IPaymentService {

    public PaymentSummaryDTO makePayment(PaymentDTO paymentDTO);

    public List<TournamentDTO> getAllPaidTournaments(Long playerId);

    public List<PaymentSummaryDTO> getPaymentsByHost(Long hostId);

    public List<PaymentSummaryDTO> getAllPayments();

}
