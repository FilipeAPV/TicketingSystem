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
import com.mycompany.ticketingsystem.utility.FilterDTO;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    private final ModelMapper modelMapper;
    private final ConvertListDTO convertListDTO;
    private String status = null;
    private String priority = null;
    private int totalPages = 0;
    private long totalElements = 0;
    private FilterDTO filterDTOfromHtml;

    private static Logger log = LoggerFactory.getLogger(ListTicketsController.class);


    @Autowired
    public ListTicketsController(TicketService ticketService,
                                 TicketRepository ticketRepository,
                                 UserRepository userRepository,
                                 ModelMapper modelMapper,
                                 ConvertListDTO convertListDTO) {
        this.ticketService = ticketService;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.convertListDTO = convertListDTO;
    }

    @GetMapping("/listTickets/{pageNum}")
    public String listTickets(@RequestParam(value = "list") String listType,
                              @PathVariable(name = "pageNum") int pageNum,
                              Model model, HttpSession httpSession) {

        String message = null;
        String relationship = null;
        List<TicketDTO> listTicketsDTO;
        User userLoggedIn = (User) httpSession.getAttribute("userLoggedIn");
        AssigneeDTO assigneeDTO = new AssigneeDTO();
        Set<UserDTO> listOfUserDTOInsideDepartment = new HashSet<>();
        Page<Ticket> ticketPage;

        log.info("\n" + this.getClass() + " @GetMapping(\"/listTickets/{pageNum}\")" + " list= " + listType + " pageNum= " + pageNum);

        if (listType.equals("created")) {
            message = "CREATED TICKETS";
            relationship = "created";
            ticketPage = ticketService.getListOfTicketsByCreator(pageNum, userLoggedIn);
            setTotalPagesAndElements(ticketPage.getTotalPages(), ticketPage.getTotalElements());
            listTicketsDTO = convertListDTO.convertTicketToDTO(ticketPage.getContent());

        } else if (listType.equals("assigned")){
            message = "ASSIGNED TICKETS";
            relationship = "assigned";
            ticketPage = ticketService.getListOfTicketsByAssignee(pageNum, userLoggedIn);
            setTotalPagesAndElements(ticketPage.getTotalPages(), ticketPage.getTotalElements());
            listTicketsDTO = convertListDTO.convertTicketToDTO(ticketPage.getContent());

        } else if (listType.equals("department")){
            Department userLoggedInDepartment = ticketService.getDepartmentName(userLoggedIn);
            String departmentName = userLoggedInDepartment.getName();
            listOfUserDTOInsideDepartment = convertListDTO.convertUserToDTO(userLoggedInDepartment.getUserList());

            message = "TICKETS FOR THE " + " " + departmentName + " " + " DEPARTMENT";
            relationship = "department";

            ticketPage = ticketService.getListOfTicketsByDepartment(pageNum, userLoggedIn);
            setTotalPagesAndElements(ticketPage.getTotalPages(), ticketPage.getTotalElements());
            listTicketsDTO = convertListDTO.convertTicketToDTO(ticketPage.getContent());

        } else if (listType.equals("admin")) {
            message = "ADMIN CONSOLE";
            relationship = "admin";

            // List of SuperUsers below
            listOfUserDTOInsideDepartment = convertListDTO.convertUserToDTO(userRepository.findByRole(Constants.ROLE_SUPERUSER));

            ticketPage = ticketService.findAllWithSpecification(pageNum, status, priority);
            setTotalPagesAndElements(ticketPage.getTotalPages(), ticketPage.getTotalElements());
            listTicketsDTO = convertListDTO.convertTicketToDTO(ticketPage.getContent());

            model.addAttribute("filter", (filterDTOfromHtml != null) ? filterDTOfromHtml : new FilterDTO());
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
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalTickets", totalElements);

        return "list-tickets";
    }

    @GetMapping("/editTicket")
    public String editTicket(@RequestParam("ticketId") int ticketId,
                             @RequestParam("relationship") String relationshipWithUser,
                             @RequestParam("currentPage") int currentPage,
                             Model model, HttpSession httpSession) {

        Ticket ticketToEdit = ticketService.getTicketById(ticketId);
        TicketDTO ticketToEditDTO = modelMapper.map(ticketToEdit, TicketDTO.class);

        model.addAttribute("ticket", ticketToEditDTO);
        model.addAttribute("priorityList", Constants.ticketPriority);
        model.addAttribute("relationshipWithUser", relationshipWithUser);
        model.addAttribute("currentPage", currentPage);

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

    @PostMapping("/filterlist")
    public String filterList(@ModelAttribute("filter") FilterDTO filterDTOfromHtml,
                             @RequestParam("relationship") String relationship,
                             @RequestParam("currentPage") int currentPage) {

        this.filterDTOfromHtml = filterDTOfromHtml;
        status = (filterDTOfromHtml.isOpen()) ? Constants.TICKET_STATUS_OPEN : null;
        priority = (filterDTOfromHtml.isHigh()) ? Constants.TICKET_PRIORITY_URGENT : null;

        return "redirect:/listTickets/" + currentPage + "?list=" + relationship;
    }

    @GetMapping("/closeTicket")
    public String deleteTicket(@RequestParam("ticketId") int id,
                               @RequestParam("relationship") String relationshipWithUser,
                               @RequestParam("currentPage") int currentPage) {
        com.mycompany.ticketingsystem.model.Ticket ticketToClose = ticketRepository.findById(id).get();
        ticketToClose.setStatus(Constants.TICKET_STATUS_CLOSED);
        ticketRepository.save(ticketToClose);
        return "redirect:/listTickets/" + currentPage + "?list=" + relationshipWithUser;
    }

    public void setTotalPagesAndElements(int totalPages, long totalElements) {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }
}
