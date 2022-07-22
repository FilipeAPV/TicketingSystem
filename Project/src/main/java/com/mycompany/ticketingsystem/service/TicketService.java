package com.mycompany.ticketingsystem.service;

import com.mycompany.ticketingsystem.constants.Constants;
import com.mycompany.ticketingsystem.controller.ListTicketsController;
import com.mycompany.ticketingsystem.controller.LoginController;
import com.mycompany.ticketingsystem.dto.UserDTO;
import com.mycompany.ticketingsystem.model.Department;
import com.mycompany.ticketingsystem.model.Ticket;
import com.mycompany.ticketingsystem.model.User;
import com.mycompany.ticketingsystem.repository.DepartmentRepository;
import com.mycompany.ticketingsystem.repository.TicketRepository;
import com.mycompany.ticketingsystem.repository.UserRepository;
import com.mycompany.ticketingsystem.utility.DbFilterSpecification;
import com.mycompany.ticketingsystem.utility.FilterDTO;
import org.apache.catalina.session.StandardSession;
import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    int pageSize = Constants.PAGE_SIZE;

    @Autowired
    public TicketService(TicketRepository ticketRepository,
                         DepartmentRepository departmentRepository,
                         UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }

    public Ticket getTicketById(int id) {
        return ticketRepository.findById(id).get();
    }

    public Page<Ticket> getListOfTicketsByCreator(int pageNum, User userLoggedIn) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return ticketRepository.findByCreatorAndStatusOrderByCreatedAt(userLoggedIn, Constants.TICKET_STATUS_OPEN, pageable);
    }

    public Page<Ticket> getListOfTicketsByAssignee(int pageNum, User userLoggedIn) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return ticketRepository.findByAssigneeAndStatusOrderByCreatedAt(userLoggedIn, Constants.TICKET_STATUS_OPEN, pageable);
    }

    public Page<Ticket> getListOfTicketsByDepartment(int pageNum, User userLoggedIn) {
        Pageable pageable = PageRequest.of(pageNum-1, pageSize);
        return ticketRepository.findByAssigneeDepartmentAndStatusOrderByCreatedAt(userLoggedIn.getDepartment(), Constants.TICKET_STATUS_OPEN, pageable);
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

    public Page<Ticket> findAllWithSpecification(int pageNum, String status, String priority) {

        Specification<Ticket> specification = Specification
                .where(status == null ? null : DbFilterSpecification.statusContains(status))
                .and(priority == null ? null : DbFilterSpecification.priorityContains(priority));

        // Spring Data JPA starts counting at 0 which means that we need to remove 1 from the number we receive from UI.
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        Page<Ticket> tickets = ticketRepository.findAll(specification, pageable);

    return tickets;
    }
}
