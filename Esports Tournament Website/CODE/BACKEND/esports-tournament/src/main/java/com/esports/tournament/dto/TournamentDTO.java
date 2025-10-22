package com.esports.tournament.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TournamentDTO {

    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String gameType;
    private boolean isActive;
    private Long hostId;
    private String imageUrl;
    private Integer joiningFee;
    private String remainingTime;


    public TournamentDTO(Long id, String name, String description, LocalDate startDate, LocalDate endDate,
                         String gameType, boolean isActive, Long hostId, Integer joiningFee, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.gameType = gameType;
        this.isActive = isActive;
        this.hostId = hostId;
        this.joiningFee = joiningFee;
        this.imageUrl = imageUrl;
        calculateRemainingTime();
    }

    public void calculateRemainingTime() {
        if (startDate != null) {
            Duration duration = Duration.between(LocalDate.now().atStartOfDay(), startDate.atStartOfDay());
            long days = duration.toDays();
            long hours = duration.toHours() % 24;
            long minutes = duration.toMinutes() % 60;

            this.remainingTime = String.format("%d days, %d hours, %d minutes", days, hours, minutes);
        }
    }
}
