package com.esports.tournament.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private Long playerId;
    private Long tournamentId;
    private Double amount;
    private String paymentMethod;
    private String transactionId;

    private boolean status;


    private String cardNumber;
    private String cardExpiryDate;
    private String cardCVC;


    private String upiId;


    public PaymentDTO(Long playerId, Long tournamentId, Double amount, String paymentMethod, String transactionId,
                      String cardNumber, String cardExpiryDate, String cardCVC, String upiId, boolean status) {
        this.playerId = playerId;
        this.tournamentId = tournamentId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.cardNumber = cardNumber;
        this.cardExpiryDate = cardExpiryDate;
        this.cardCVC = cardCVC;
        this.upiId = upiId;
        this.status = status;
    }

}
