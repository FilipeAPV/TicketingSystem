package com.mycompany.ticketingsystem.controller;

import com.mycompany.ticketingsystem.constants.Constants;
import com.mycompany.ticketingsystem.dto.TicketDTO;
import com.mycompany.ticketingsystem.dto.UserDTO;
import com.mycompany.ticketingsystem.model.Ticket;
import com.mycompany.ticketingsystem.model.User;
import com.mycompany.ticketingsystem.repository.TicketRepository;
import com.mycompany.ticketingsystem.service.TicketService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class ListTicketsController {

    private TicketService ticketService;
    private TicketRepository ticketRepository;
    private HttpSession httpSession;
    private ModelMapper modelMapper;

    @Autowired
    public ListTicketsController(TicketService ticketService,
                                 TicketRepository ticketRepository,
                                 HttpSession httpSession,
                                 ModelMapper modelMapper) {
        this.ticketService = ticketService;
        this.ticketRepository = ticketRepository;
        this.httpSession = httpSession;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/listTickets")
    public String listTickets(@RequestParam(value = "list") String listType,
                              Model model, HttpSession httpSession) {

        String message = null;
        List<Ticket> listTickets;
        User userLoggedIn = (User) httpSession.getAttribute("userLoggedIn");

        if (listType.equals("created")) {
            message = "CREATED TICKETS";
            listTickets = ticketService.getListOfTicketsByCreator(userLoggedIn);
        } else {
            message = "ASSIGNED TICKETS";
            listTickets = ticketService.getListOfTicketsByAssignee(userLoggedIn);
            model.addAttribute("assigned", "assigned");
        }

        model.addAttribute("listTicketsCreated",listTickets);
        model.addAttribute("message", message);

        return "list-tickets";
    }

    @GetMapping("/editTicket")
    public String editTicket(@RequestParam("ticketId") int ticketId,
                             @RequestParam("relationship") String relationshipWithUser,
                             Model model, HttpSession httpSession) {
        Ticket ticketToEdit = ticketService.getTicketById(ticketId);
        TicketDTO ticketToEditDTO = modelMapper.map(ticketToEdit, TicketDTO.class);
        model.addAttribute("ticket", ticketToEditDTO);
        model.addAttribute("priorityList", Constants.ticketPriority);
        model.addAttribute("relationshipWithUser", relationshipWithUser);

        httpSession.setAttribute("ticketToEdit", ticketToEdit);

        return "update-tickets";
    }


    @GetMapping("/closeTicket")
    public String deleteTicket(@RequestParam("ticketId") int id) {
        Ticket ticketToClose = ticketRepository.findById(id).get();
        ticketToClose.setStatus(Constants.TICKET_STATUS_CLOSED);
        ticketRepository.save(ticketToClose);
        return "redirect:/listTickets?list=created";
    }

}
