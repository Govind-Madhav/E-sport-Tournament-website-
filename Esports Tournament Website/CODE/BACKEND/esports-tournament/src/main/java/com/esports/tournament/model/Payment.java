package com.esports.tournament.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private User player;

    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    private double amount;

    private String paymentMethod;

    private String transactionId; // Generated transaction ID

    private boolean status; // Payment status

    private String cardNumber;
    private String cardExpiryDate;
    private String cardCVC;

    // UPI details for UPI payment method
    private String upiId;

    private LocalDate createdDate;
    private LocalDate updatedDate;

    // Getter for status (if not using Lombok's @Data, you'd need to add a getter)
    public boolean getStatus() {
        return status;
    }
}
