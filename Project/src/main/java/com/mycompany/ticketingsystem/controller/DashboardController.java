package com.mycompany.ticketingsystem.controller;

import com.mycompany.ticketingsystem.constants.Constants;
import com.mycompany.ticketingsystem.dto.TicketDTO;
import com.mycompany.ticketingsystem.dto.UserDTO;
import com.mycompany.ticketingsystem.model.Ticket;
import com.mycompany.ticketingsystem.model.User;
import com.mycompany.ticketingsystem.repository.UserRepository;
import com.mycompany.ticketingsystem.service.TicketService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class DashboardController {

    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private TicketService ticketService;
    private UserDTO userLoggedInDTO;

    @Autowired
    public DashboardController(UserRepository userRepository, ModelMapper modelMapper,
                               TicketService ticketService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.ticketService = ticketService;
    }

    @GetMapping("/dashboard")
    public String getDashboard(Authentication authentication, Model model,
                               @RequestParam(value = "saved", required = false) String isSaved,
                               HttpSession httpSession) {

        //TODO if the only need of the model attribute is to pass the first name, then we need to send only
        //the string
        User userLoggedIn  = userRepository.findByEmail(authentication.getName());
        httpSession.setAttribute("userLoggedIn", userLoggedIn);
        userLoggedInDTO = modelMapper.map(userLoggedIn, UserDTO.class);

        String message = null;
        if (isSaved != null) {
            message = "Ticket created successfully";
        }

        model.addAttribute("ticket", new TicketDTO());
        model.addAttribute("priorityList", Constants.ticketPriority);
        model.addAttribute("message", message);
        model.addAttribute("userLoggedIn", userLoggedInDTO);


        return Constants.DASHBOARD;
    }

    @PostMapping("/saveTicket")
    public String saveTicket(@Valid @ModelAttribute("ticket") TicketDTO ticketDTO,
                             Errors errors, Model model, HttpSession httpSession,
                             @RequestParam(value = "relationshipWithUser", required = false) String relationshipWithUser) {



        if (errors.hasErrors()) {
            model.addAttribute("priorityList", Constants.ticketPriority);
            model.addAttribute("userLoggedIn", userLoggedInDTO);
            return Constants.DASHBOARD;
        }

        Ticket ticketToEdit = (Ticket) httpSession.getAttribute("ticketToEdit");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        if (ticketToEdit != null && ticketToEdit.getId() > 0) {
            ticketDTO.setId(ticketToEdit.getId());
            ticketDTO.setCreator(ticketToEdit.getCreator());
            ticketDTO.setCreatedAt(ticketToEdit.getCreatedAt());
            ticketDTO.setAssignee(ticketToEdit.getAssignee());

            ticketDTO.setMessage(ticketToEdit.getMessage()
            + "\n\n"
            + "New message from " + userLoggedInDTO.getFirstName()
            + " at: "
            + formatter.format(date)
            + "\n"
            + ticketDTO.getAddMessage()
            + " - ");
        }

        com.mycompany.ticketingsystem.model.Ticket ticketToSave = modelMapper.map(ticketDTO, com.mycompany.ticketingsystem.model.Ticket.class);
        User userLoggedIn = (User) httpSession.getAttribute("userLoggedIn");
        Boolean isSaved = ticketService.saveTicket(ticketToSave, userLoggedIn);

        if (Boolean.TRUE.equals(isSaved) && !(ticketToEdit!=null)) { // Does not throw NPE if isSaved is null. It will take it as false.
            return "redirect:/dashboard?saved=true";
        } else {
            httpSession.removeAttribute("ticketToEdit");
            String path = null;
            if (relationshipWithUser.equals("created")) {
                path = "created";
            } else if (relationshipWithUser.equals("assigned")){
                path = "assigned";
            } else {
                path = "department";
            }
            return ("redirect:/listTickets?list=" + path);
        }
    }

}
