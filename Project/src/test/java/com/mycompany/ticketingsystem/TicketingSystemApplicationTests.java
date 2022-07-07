package com.mycompany.ticketingsystem;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootTest
@EntityScan("com.mycompany.ticketingsystem.model")
@EnableJpaRepositories("com.mycompany.ticketingsystem.repository")
class TicketingSystemApplicationTests {

    @Test
    void contextLoads() {
    }

}
