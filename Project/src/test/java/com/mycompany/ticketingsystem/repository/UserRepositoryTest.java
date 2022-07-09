package com.mycompany.ticketingsystem.repository;

import com.mycompany.ticketingsystem.model.Department;
import com.mycompany.ticketingsystem.model.Ticket;
import com.mycompany.ticketingsystem.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.stereotype.Repository;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Disable in memory db support.
class UserRepositoryTest{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    User userTest = new User();
    Department departmentTest = new Department();
    Ticket ticketTest = new Ticket();
    Ticket ticketTest1 = new Ticket();

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

        //IT
        Department dep = departmentRepository.findById(1).get();

        userTest.setDepartment(dep);
        dep.setUserList(userTest);

        departmentTest.setName("HR");
        departmentTest.setSuperUser_id(userTest);
        departmentTest.setCreatedAt(LocalDateTime.now());
        departmentTest.setCreatedBy("JUnit Test");



        ticketTest.setStatus("Open");
        ticketTest.setSubject("This is a subject test");
        ticketTest.setPriority("High");
        ticketTest.setMessage("Message Test");
        ticketTest.setCreatedAt(LocalDateTime.now());
        ticketTest.setCreatedBy("JUnit Test");

        ticketTest.setCreator(userTest);
        userTest.setCreatedTicketsList(ticketTest);

        ticketTest1.setStatus("Open");
        ticketTest1.setSubject("Test2");
        ticketTest1.setPriority("High");
        ticketTest1.setMessage("Message Test");
        ticketTest1.setCreatedAt(LocalDateTime.now());
        ticketTest1.setCreatedBy("JUnit Test");

        ticketTest1.setCreator(userTest);
        userTest.setCreatedTicketsList(ticketTest1);

    }

    @Test
    @Rollback(false)
    public void savingUser() {
        User savedUser = userRepository.save(userTest);

        System.out.println(savedUser);
        System.out.println(savedUser.getDepartment());
        System.out.println(savedUser.getDepartment().getUserList());

        assertThat(savedUser.getId()).isEqualTo(6);
        assertThat(savedUser.getDepartment().getId()).isEqualTo(1);

    }

    @Test
    @Rollback(false)
    public void savingTicket() {
        Ticket savedTicket = ticketRepository.save(ticketTest);

        System.out.println(savedTicket);
        System.out.println(savedTicket.getCreator());
        System.out.println(savedTicket.getCreator().getCreatedTicketsList());

        assertThat(savedTicket).isNotNull();
        assertThat(savedTicket.getId()).isGreaterThan(0);
    }

    @Test
    @Rollback(false)
    public void sameUserCreatesAnotherTicket() {

        Ticket savedTicket = ticketRepository.save(ticketTest1);

        System.out.println(savedTicket);
        System.out.println(savedTicket.getCreator());
        System.out.println(savedTicket.getCreator().getCreatedTicketsList());

        assertThat(savedTicket).isNotNull();
        assertThat(savedTicket.getId()).isGreaterThan(0);
    }

    @Test
    @Rollback(false)
    public void addAssigneeToTicket() {

        Ticket savedTicket = ticketRepository.save(ticketTest1);

        System.out.println(savedTicket);
        System.out.println(savedTicket.getCreator());
        System.out.println(savedTicket.getCreator().getCreatedTicketsList());

        assertThat(savedTicket).isNotNull();
        assertThat(savedTicket.getId()).isGreaterThan(0);
    }
}