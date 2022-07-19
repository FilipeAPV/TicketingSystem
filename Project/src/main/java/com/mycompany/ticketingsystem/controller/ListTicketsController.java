package com.mycompany.ticketingsystem.controller;

import com.mycompany.ticketingsystem.constants.Constants;
import com.mycompany.ticketingsystem.dto.AssigneeDTO;
import com.mycompany.ticketingsystem.dto.TicketDTO;
import com.mycompany.ticketingsystem.dto.UserDTO;
import com.mycompany.ticketingsystem.model.Department;
import com.mycompany.ticketingsystem.model.Ticket;
import com.mycompany.ticketingsystem.model.User;
import com.mycompany.ticketingsystem.repository.DepartmentRepository;
import com.mycompany.ticketingsystem.repository.TicketRepository;
import com.mycompany.ticketingsystem.repository.UserRepository;
import com.mycompany.ticketingsystem.service.TicketService;
import com.mycompany.ticketingsystem.utility.ConvertListDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class ListTicketsController {

    private final TicketService ticketService;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final HttpSession httpSession;
    private final ModelMapper modelMapper;
    private final ConvertListDTO convertListDTO;

    @Autowired
    public ListTicketsController(TicketService ticketService,
                                 TicketRepository ticketRepository,
                                 UserRepository userRepository,
                                 DepartmentRepository departmentRepository,
                                 HttpSession httpSession,
                                 ModelMapper modelMapper,
                                 ConvertListDTO convertListDTO) {
        this.ticketService = ticketService;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.httpSession = httpSession;
        this.modelMapper = modelMapper;
        this.convertListDTO = convertListDTO;
    }

    @GetMapping("/listTickets")
    public String listTickets(@RequestParam(value = "list") String listType,
                              Model model, HttpSession httpSession) {

        String message = null;
        String relationship = null;
        List<TicketDTO> listTicketsDTO;
        User userLoggedIn = (User) httpSession.getAttribute("userLoggedIn");
        AssigneeDTO assigneeDTO = new AssigneeDTO();
        Set<UserDTO> listOfUserDTOInsideDepartment = new HashSet<>();

        if (listType.equals("created")) {
            message = "CREATED TICKETS";
            relationship = "created";
            listTicketsDTO = convertListDTO.convertTicketToDTO(ticketService.getListOfTicketsByCreator(userLoggedIn));
        } else if (listType.equals("assigned")){
            message = "ASSIGNED TICKETS";
            listTicketsDTO = convertListDTO.convertTicketToDTO(ticketService.getListOfTicketsByAssignee(userLoggedIn));
            relationship = "assigned";
        } else if (listType.equals("department")){
            Department userLoggedInDepartment = ticketService.getDepartmentName(userLoggedIn);
            String departmentName = userLoggedInDepartment.getName();
            listOfUserDTOInsideDepartment = convertListDTO.convertUserToDTO(userLoggedInDepartment.getUserList());

            relationship = "department";
            message = "TICKETS FOR THE " + " " + departmentName + " " + " DEPARTMENT";
            listTicketsDTO = convertListDTO.convertTicketToDTO(ticketService.getListOfTicketsByDepartment(userLoggedIn));

        } else if (listType.equals("admin")) {
            message = "ADMIN CONSOLE";
            relationship = "admin";
            listTicketsDTO = convertListDTO.convertTicketToDTO(ticketService.numberOfAllTickets());
            // List of SuperUsers below
            listOfUserDTOInsideDepartment = convertListDTO.convertUserToDTO(userRepository.findByRole(Constants.ROLE_SUPERUSER));

        } else {
            listTicketsDTO = List.of(new TicketDTO());
        }


        if (listOfUserDTOInsideDepartment != null) {
            model.addAttribute("listOfUserDTOInsideDepartment", listOfUserDTOInsideDepartment);
        }

        model.addAttribute("listTicketsCreated",listTicketsDTO);
        model.addAttribute("message", message);
        model.addAttribute("assigneeDTO", assigneeDTO);
        model.addAttribute("relationship", relationship);

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

    @PostMapping("/editAssignee")
    public String editAssignee(@ModelAttribute("assigneeDTO") AssigneeDTO assigneeDTO,
                               @RequestParam("ticketId") int ticketId,
                               @RequestParam("relationship") String relationship) {

        String path = relationship;

        if (assigneeDTO != null) {
            ticketService.editAssignee(assigneeDTO.getAssigneeId(), ticketId);
        }

        return ("redirect:/listTickets?list=" + path);
    }


    @GetMapping("/closeTicket")
    public String deleteTicket(@RequestParam("ticketId") int id,
                               @RequestParam("relationship") String relationshipWithUser) {
        com.mycompany.ticketingsystem.model.Ticket ticketToClose = ticketRepository.findById(id).get();
        ticketToClose.setStatus(Constants.TICKET_STATUS_CLOSED);
        ticketRepository.save(ticketToClose);
        return "redirect:/listTickets?list=" + relationshipWithUser;
    }

}
