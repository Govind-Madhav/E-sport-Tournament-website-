package com.esports.tournament.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WinnerDTO {
    private Long playerId;
    private Integer rank;
    private Double prizeAmount;
    private String remarks;


}
