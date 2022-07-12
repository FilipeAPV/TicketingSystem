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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DashboardController {

    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private TicketService ticketService;
    private User userLoggedIn;

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
        UserDTO userLoggedInDTO = modelMapper.map(userLoggedIn, UserDTO.class);

        String message = null;
        if (isSaved != null) {
            message = "Ticket created successfully";
        }

        model.addAttribute("userLoggedIn", userLoggedInDTO);
        model.addAttribute("ticket", new TicketDTO());
        model.addAttribute("priorityList", Constants.ticketPriority);
        model.addAttribute("message", message);



        return "dashboard";
    }

    @PostMapping("/saveTicket")
    public String saveTicket(@ModelAttribute("ticket") TicketDTO ticketDTO) {

        Ticket ticketToSave = modelMapper.map(ticketDTO, Ticket.class);
        Boolean isSaved = ticketService.saveTicket(ticketToSave, userLoggedIn);

        if (isSaved) {
            return "redirect:/dashboard?saved=true";
        }

        return "dashboard";
    }
}
