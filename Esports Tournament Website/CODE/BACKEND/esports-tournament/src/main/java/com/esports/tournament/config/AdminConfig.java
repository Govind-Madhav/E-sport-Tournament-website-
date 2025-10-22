package com.esports.tournament.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@lombok.Getter
public class AdminConfig {

    @Value("${admin.email}")
    private String email;

    @Value("${admin.password}")
    private String password;


}
