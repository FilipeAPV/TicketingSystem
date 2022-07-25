package com.mycompany.ticketingsystem.service;

import com.mycompany.ticketingsystem.constants.Constants;
import com.mycompany.ticketingsystem.model.Department;
import com.mycompany.ticketingsystem.model.Ticket;
import com.mycompany.ticketingsystem.model.User;
import com.mycompany.ticketingsystem.repository.DepartmentRepository;
import com.mycompany.ticketingsystem.repository.TicketRepository;
import com.mycompany.ticketingsystem.repository.UserRepository;
import com.mycompany.ticketingsystem.utility.DbFilterSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

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

    public Ticket getTicketById(int id) throws Exception {
        Optional<Ticket> optinalTicket = ticketRepository.findById(id);

        if (optinalTicket.isPresent()) {
            return optinalTicket.get();
        }
        throw new Exception("Result for getTicketById " + id + " is Null");
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

    public Department getDepartmentName(User userLoggedIn) throws Exception{
        Optional<Department> tempDepartment = departmentRepository.findById(userLoggedIn.getDepartment().getId());

        if (tempDepartment.isPresent()) {
            return tempDepartment.get();
        }

        throw new Exception("Result for getDepartmentName " + userLoggedIn.getDepartment().getId() + " is Null");
    }

    public Boolean saveTicket(Ticket ticket, User userLoggedIn) {
        boolean isSaved = false;

        if (ticket.getCreator() == null) {
            ticket.setCreator(userLoggedIn);
        }
        ticket = ticketRepository.save(ticket);

        if (ticket != null && ticket.getId() > 0) {
            isSaved = true;
        }

        return isSaved;
    }

    public void editAssignee(int assigneeId, Integer ticketId) throws Exception {
        Optional<Ticket> ticketToChangeAssigneeOpt = ticketRepository.findById(ticketId);
        Optional<User> userToAddAsAssigneeOpt = userRepository.findById(assigneeId);

            if (ticketToChangeAssigneeOpt.isEmpty() || userToAddAsAssigneeOpt.isEmpty()) {
                throw new Exception("Ticket id: " + ticketId + "OR user id: " + assigneeId + " do not exist");
            }

        Ticket ticketToChangeAssignee = ticketToChangeAssigneeOpt.get();
        User userToAddAsAssignee = userToAddAsAssigneeOpt.get();

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

    return ticketRepository.findAll(specification, pageable);
    }
}
