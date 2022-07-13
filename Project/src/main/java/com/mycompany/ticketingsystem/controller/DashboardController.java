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

import javax.validation.Valid;

@Controller
public class DashboardController {

    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private TicketService ticketService;
    private User userLoggedIn;
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
                               @RequestParam(value = "saved", required = false) String isSaved) {

        //TODO if the only need of the model attribute is to pass the first name, then we need to send only
        //the string
        userLoggedIn  = userRepository.findByEmail(authentication.getName());
        userLoggedInDTO = modelMapper.map(userLoggedIn, UserDTO.class);

        String message = null;
        if (isSaved != null) {
            message = "Ticket created successfully";
        }

        model.addAttribute("userLoggedIn", userLoggedInDTO);
        model.addAttribute("ticket", new TicketDTO());
        model.addAttribute("priorityList", Constants.ticketPriority);
        model.addAttribute("message", message);

        return Constants.DASHBOARD;
    }

    @PostMapping("/saveTicket")
    public String saveTicket(@Valid @ModelAttribute("ticket") TicketDTO ticketDTO,
                             Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("priorityList", Constants.ticketPriority);
            model.addAttribute("userLoggedIn", userLoggedInDTO);
            return Constants.DASHBOARD;
        }

        Ticket ticketToSave = modelMapper.map(ticketDTO, Ticket.class);
        Boolean isSaved = ticketService.saveTicket(ticketToSave, userLoggedIn);

        if (Boolean.TRUE.equals(isSaved)) { // Does not throw NPE if isSaved is null. It will take it as false.
            return "redirect:/dashboard?saved=true";
        }

        return Constants.DASHBOARD;
    }
}
