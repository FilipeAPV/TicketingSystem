package com.mycompany.ticketingsystem.service;

import com.mycompany.ticketingsystem.constants.Constants;
import com.mycompany.ticketingsystem.controller.LoginController;
import com.mycompany.ticketingsystem.dto.UserDTO;
import com.mycompany.ticketingsystem.model.Department;
import com.mycompany.ticketingsystem.model.Ticket;
import com.mycompany.ticketingsystem.model.User;
import com.mycompany.ticketingsystem.repository.DepartmentRepository;
import com.mycompany.ticketingsystem.repository.TicketRepository;
import com.mycompany.ticketingsystem.repository.UserRepository;
import com.mycompany.ticketingsystem.utility.FilterDTO;
import org.apache.catalina.session.StandardSession;
import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final LoginController loginController;
    private final FilterDTO filterDTO;

    @Autowired
    public TicketService(TicketRepository ticketRepository,
                         DepartmentRepository departmentRepository,
                         UserRepository userRepository,
                         LoginController loginController,
                         FilterDTO filterDTO) {
        this.ticketRepository = ticketRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.loginController = loginController;
        this.filterDTO = filterDTO;
    }

    public Ticket getTicketById(int id) {
        return ticketRepository.findById(id).get();
    }

    public List<Ticket> getListOfTicketsByCreator(User userLoggedIn) {
        return ticketRepository.findByCreatorAndStatusOrderByCreatedAt(userLoggedIn, Constants.TICKET_STATUS_OPEN);
    }

    public List<Ticket> getListOfTicketsByAssignee(User userLoggedIn) {
        return ticketRepository.findByAssigneeAndStatusOrderByCreatedAt(userLoggedIn, Constants.TICKET_STATUS_OPEN);
    }

    public List<Ticket> getListOfTicketsByDepartment(User userLoggedIn) {
        return ticketRepository.findByAssigneeDepartmentAndStatusOrderByCreatedAt(userLoggedIn.getDepartment(), Constants.TICKET_STATUS_OPEN);
    }

    public Department getDepartmentName(User userLoggedIn) {
        return departmentRepository.findById(userLoggedIn.getDepartment().getId()).get();
    }

    public Boolean saveTicket(Ticket ticket, User userLoggedIn) {
        boolean isSaved = false;

        if (!(ticket.getCreator() != null)) {
            ticket.setCreator(userLoggedIn);
        }
        ticket = ticketRepository.save(ticket);

        if (ticket != null && ticket.getId() > 0) {
            isSaved = true;
        }

        return isSaved;
    }

    public void editAssignee(int assigneeId, Integer ticketId) {
        Ticket ticketToChangeAssignee = ticketRepository.findById(ticketId).get();
        User userToAddAsAssignee = userRepository.findById(assigneeId).get();

        ticketToChangeAssignee.setAssignee(userToAddAsAssignee);
        ticketToChangeAssignee.setAssigneeDepartment(userToAddAsAssignee.getDepartment());

        ticketRepository.save(ticketToChangeAssignee);
    }

    public int numberOfOpenTickets() {
        List<Ticket> ticketList = ticketRepository.findByStatus(Constants.TICKET_STATUS_OPEN);
        return ticketList.size();
    }

    public List<Ticket> numberOfAllTickets() {
        return ticketRepository.findAllByOrderByCreatedAt();
    }

    public int numberOfAssignedTickets() {
        List<Ticket> ticketList = ticketRepository.findByAssigneeIsNotNullAndStatus(Constants.TICKET_STATUS_OPEN);
        return ticketList.size();
    }
}
