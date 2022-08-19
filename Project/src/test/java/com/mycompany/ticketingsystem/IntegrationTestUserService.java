package com.mycompany.ticketingsystem;

import com.mycompany.ticketingsystem.model.User;
import com.mycompany.ticketingsystem.repository.DepartmentRepository;
import com.mycompany.ticketingsystem.repository.TicketRepository;
import com.mycompany.ticketingsystem.repository.UserRepository;
import com.mycompany.ticketingsystem.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;

import java.util.Optional;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;

@TestPropertySource("/application_test.properties")
@SpringBootTest(classes = {TicketingSystemApplication.class})
@WithMockUser(username="admin",roles={"USER","ADMIN"})
@Transactional
@DirtiesContext(classMode = AFTER_CLASS)
public class IntegrationTestUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void init() {
    }

    @AfterEach
    void clean() {
        userRepository.findAll().forEach(a -> a.setDepartment(null));
        ticketRepository.findAll().forEach(a ->
             {

                 a.setAssignee(null);
                 a.setCreator(null);
                 ticketRepository.save(a);
             });
        departmentRepository.findAll().forEach(a -> a.setSuperUserId(null));

        userRepository.deleteAll();
    }

    @Test
    @Sql("/data_test.sql")
    void testSaveUser() {
        User testUser = new User(0, "Tony", "Martinez", "Title",
                "tm@gmail.com","tm@gmail.com", "Manuel", "1", "1",
                "Tester", null, null, null);

        userService.saveUser(testUser);

        Optional<User> testUserOptinal = userRepository.findById(3);

        Assertions.assertNotNull(testUserOptinal, "User retrieved from DB is Null");
        Assertions.assertEquals(3, testUserOptinal.get().getId());
    }

}
