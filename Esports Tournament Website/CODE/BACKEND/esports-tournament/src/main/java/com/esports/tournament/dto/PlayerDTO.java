package com.esports.tournament.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDTO {
    private String fullName;
    private String email;
    private String password;
    private String mobile;
    private String imageUrl;
}

