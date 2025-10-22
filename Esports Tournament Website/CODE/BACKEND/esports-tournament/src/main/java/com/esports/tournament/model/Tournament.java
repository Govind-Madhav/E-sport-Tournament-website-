package com.esports.tournament.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tournaments")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long hostId;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String gameType;
    private Integer joiningFee;
    private boolean isActive;
    private String imageUrl;


}
