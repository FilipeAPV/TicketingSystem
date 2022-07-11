package com.mycompany.ticketingsystem.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecBcryptConfig {
    /*
    Class created to avoid circular dependency between the classes:
    AuthenticationProviderPswEmail and SecurityConfiguration
    Neither class could be fully created because SecurityConfiguration creates the @Bean below and also injects AuthenticationProviderPswEmail @Bean
    Whereas AuthenticationProviderPswEmail injects the @Bean below
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
