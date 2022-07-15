package com.mycompany.ticketingsystem.service;

import com.mycompany.ticketingsystem.constants.Constants;
import com.mycompany.ticketingsystem.dto.UserDTO;
import com.mycompany.ticketingsystem.model.Ticket;
import com.mycompany.ticketingsystem.model.User;
import com.mycompany.ticketingsystem.repository.TicketRepository;
import org.apache.catalina.session.StandardSession;
import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class TicketService {
    private TicketRepository ticketRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
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
}
