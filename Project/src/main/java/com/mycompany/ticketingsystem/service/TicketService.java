package com.mycompany.ticketingsystem.service;

import com.mycompany.ticketingsystem.dto.UserDTO;
import com.mycompany.ticketingsystem.model.Ticket;
import com.mycompany.ticketingsystem.model.User;
import com.mycompany.ticketingsystem.repository.TicketRepository;
import org.apache.catalina.session.StandardSession;
import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class TicketService {
    private TicketRepository ticketRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> getListOfTicketsByCreator(User userLoggedIn) {
        return ticketRepository.findByCreatorOrderByCreatedAt(userLoggedIn);
    }

    public Boolean saveTicket(Ticket ticket, User userLoggedIn) {
        boolean isSaved = false;

        ticket.setCreator(userLoggedIn);
        ticket = ticketRepository.save(ticket);

        if (ticket != null && ticket.getId() > 0) {
            isSaved = true;
        }

        return isSaved;
    }
}
