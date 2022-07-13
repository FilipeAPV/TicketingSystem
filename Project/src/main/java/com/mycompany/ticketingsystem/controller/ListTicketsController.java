package com.mycompany.ticketingsystem.controller;

import com.mycompany.ticketingsystem.dto.UserDTO;
import com.mycompany.ticketingsystem.model.Ticket;
import com.mycompany.ticketingsystem.model.User;
import com.mycompany.ticketingsystem.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ListTicketsController {

    private TicketService ticketService;

    @Autowired
    public ListTicketsController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/listTickets")
    public String listTickets(@RequestParam(value = "list") String listType,
                              Model model, HttpSession httpSession) {

        String message = null;
        if (listType.equals("created")) {
            message = "CREATED TICKETS";
            List<Ticket> listTicketsCreated = ticketService.getListOfTicketsByCreator((User) httpSession.getAttribute("userLoggedIn"));
            model.addAttribute("listTicketsCreated",listTicketsCreated);
        } else {
            message = "ASSIGNED TICKETS";
        }

        model.addAttribute("message", message);

        return "list-tickets";
    }

}
