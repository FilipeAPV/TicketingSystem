package com.mycompany.ticketingsystem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests().antMatchers("/h2-console/**").permitAll()
            .and()
                .csrf().ignoringAntMatchers("/h2-console/**")
            .and()
                .headers().frameOptions().sameOrigin();

        httpSecurity.authorizeRequests()
                .mvcMatchers("/","/public/**").permitAll()
                .mvcMatchers("/css/**","/fontawesome/**","/img/**","/js/**").permitAll()
                .mvcMatchers("/**").authenticated()
            .and()
                .formLogin().loginPage("/public/login").defaultSuccessUrl("/dashboard")
                .failureUrl("/public/login?error=true")
            .and()
                .logout().logoutSuccessUrl("/public/login?logout=true").invalidateHttpSession(true)
            .and()
                .httpBasic();

        return httpSecurity.build();
    }
}
