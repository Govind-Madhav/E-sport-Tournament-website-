package com.esports.tournament.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSummaryDTO {
    private Long playerId;
    private Long tournamentId;
    private Double amount;
    private String paymentMethod;
    private String transactionId;
    private boolean status;


}

