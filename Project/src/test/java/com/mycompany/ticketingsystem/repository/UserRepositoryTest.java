package com.mycompany.ticketingsystem.repository;

import com.mycompany.ticketingsystem.model.Department;
import com.mycompany.ticketingsystem.model.Ticket;
import com.mycompany.ticketingsystem.model.User;
import com.mycompany.ticketingsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    User userTest = new User();
    Department departmentTest = new Department();
    Ticket ticketTest = new Ticket();

    @BeforeEach
    void setUp() {
        userTest.setFirstName("Matias");
        userTest.setLastName("Tomas");
        userTest.setJobTitle("HR Associate");
        userTest.setEmail("mt@gmail.com");
        userTest.setManager("Rashid");
        userTest.setPassword("1234");
        userTest.setCreatedBy("JUnit Test");
        userTest.setCreatedAt(LocalDateTime.now());

        departmentTest.setName("HR");
        departmentTest.setSuperUser_id(1);
        departmentTest.setCreatedAt(LocalDateTime.now());
        departmentTest.setCreatedBy("JUnit Test");

        userTest.setDepartment(departmentTest);
        departmentTest.setUserList(userTest);

        ticketTest.setStatus("Open");
        ticketTest.setSubject("This is a subject test");
        ticketTest.setPriority("High");
        ticketTest.setMessage("Message Test");
        ticketTest.setCreatedAt(LocalDateTime.now());
        ticketTest.setCreatedBy("JUnit Test");

        ticketTest.setCreator(userTest);
        userTest.setTicketsList(ticketTest);


        // add ticket to list with setter
    }

    @Test
    public void savingUser() {
        User savedUser = userRepository.save(userTest);

        System.out.println(savedUser);
        System.out.println(savedUser.getDepartment());
        System.out.println(savedUser.getDepartment().getUserList());

        assertThat(savedUser).isNotNull();
    }

    @Test
    public void savingTicket() {
        Ticket savedTicket = ticketRepository.save(ticketTest);

        System.out.println(savedTicket);
        System.out.println(savedTicket.getCreator());
        System.out.println(savedTicket.getCreator().getTicketsList());

        assertThat(savedTicket).isNotNull();
    }
}