package com.esports.tournament.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaderboardDTO {
    private Long playerId;
    private String fullName;
    private int rank;
    private Double prizeAmount;
    private String remarks;
    private String profileImage;
}

