package com.mycompany.ticketingsystem;

import com.mycompany.ticketingsystem.model.Ticket;
import com.mycompany.ticketingsystem.repository.DepartmentRepository;
import com.mycompany.ticketingsystem.repository.TicketRepository;
import com.mycompany.ticketingsystem.repository.UserRepository;
import com.mycompany.ticketingsystem.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("/application_test.properties")
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(classes = TicketingSystemApplication.class)
@Transactional
@WithMockUser(username="admin",roles={"USER","ADMIN"})
public class IntegrationTestListTicketsController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

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
    void testCloseTicket() throws Exception {

        Optional<Ticket> testTicket = ticketRepository.findById(1);

        Assertions.assertNotNull(testTicket);
        Assertions.assertEquals("OPEN", testTicket.get().getStatus());

        MvcResult mvcResult = this.mockMvc.perform(get("/closeTicket")
                .contentType(MediaType.APPLICATION_JSON)
                .param("ticketId", "1")
                .param("relationship", "admin")
                .param("currentPage", "1"))
                .andExpect(status().is3xxRedirection()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "redirect:/listTickets/1?list=admin");

        testTicket = ticketRepository.findById(1);

        Assertions.assertNotNull(testTicket);
        Assertions.assertEquals("CLOSED", testTicket.get().getStatus());

    }

}
