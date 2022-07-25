package com.mycompany.ticketingsystem.controller;

import com.mycompany.ticketingsystem.constants.Constants;
import com.mycompany.ticketingsystem.dto.AssigneeDTO;
import com.mycompany.ticketingsystem.dto.TicketDTO;
import com.mycompany.ticketingsystem.dto.UserDTO;
import com.mycompany.ticketingsystem.model.Department;
import com.mycompany.ticketingsystem.model.Ticket;
import com.mycompany.ticketingsystem.model.User;
import com.mycompany.ticketingsystem.repository.TicketRepository;
import com.mycompany.ticketingsystem.repository.UserRepository;
import com.mycompany.ticketingsystem.service.TicketService;
import com.mycompany.ticketingsystem.service.UserService;
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
import java.util.List;

import static com.mycompany.ticketingsystem.constants.Constants.REDIRECT_TO_LISTICKETS;
import static com.mycompany.ticketingsystem.constants.Constants.USER_CURRENTLY_LOGGED_IN;

@Controller
public class ListTicketsController {

    private final TicketService ticketService;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ConvertListDTO convertListDTO;
    private final UserService userService;
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
                                 ConvertListDTO convertListDTO,
                                 UserService userService) {
        this.ticketService = ticketService;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.convertListDTO = convertListDTO;
        this.userService = userService;
    }

    @GetMapping("/listTickets/{pageNum}")
    public String listTickets(@RequestParam(value = "list") String listType,
                              @PathVariable(name = "pageNum") int pageNum,
                              Model model, HttpSession httpSession) throws Exception {

        String message = null;
        String relationship = null;
        List<TicketDTO> listTicketsDTO;
        User userLoggedIn = (User) httpSession.getAttribute(USER_CURRENTLY_LOGGED_IN);
        AssigneeDTO assigneeDTO = new AssigneeDTO();
        List<UserDTO> listOfUserDTOInsideDepartment = new ArrayList<>();
        Page<Ticket> ticketPage;

        log.info("\n" + this.getClass() + " @GetMapping(\"/listTickets/{pageNum}\")" + " list= " + listType + " pageNum= " + pageNum);

        if (listType.equals("created")) {
            message = "CREATED TICKETS";
            relationship = "created";
            ticketPage = ticketService.getListOfTicketsByCreator(pageNum, userLoggedIn);
            setTotalPagesAndElements(ticketPage.getTotalPages(), ticketPage.getTotalElements());
            listTicketsDTO = convertListDTO.convertListToListDTO(ticketPage.getContent(), TicketDTO.class);

        } else if (listType.equals("assigned")){
            message = "ASSIGNED TICKETS";
            relationship = "assigned";
            ticketPage = ticketService.getListOfTicketsByAssignee(pageNum, userLoggedIn);
            setTotalPagesAndElements(ticketPage.getTotalPages(), ticketPage.getTotalElements());
            listTicketsDTO = convertListDTO.convertListToListDTO(ticketPage.getContent(), TicketDTO.class);

        } else if (listType.equals("department")){

            if (userLoggedIn == null) {
                throw new Exception("Could not identify the logged in User!");
            }

            Department userLoggedInDepartment = ticketService.getDepartmentName(userLoggedIn);
            String departmentName = userLoggedInDepartment.getName();
            listOfUserDTOInsideDepartment = convertListDTO.convertListToListDTO(userService.findAllUsersInsideOneDepartment(userLoggedInDepartment), UserDTO.class);

            message = "TICKETS FOR THE " + " " + departmentName + " " + " DEPARTMENT";
            relationship = "department";

            ticketPage = ticketService.getListOfTicketsByDepartment(pageNum, userLoggedIn);
            setTotalPagesAndElements(ticketPage.getTotalPages(), ticketPage.getTotalElements());
            listTicketsDTO = convertListDTO.convertListToListDTO(ticketPage.getContent(), TicketDTO.class);

        } else if (listType.equals("admin")) {
            message = "ADMIN CONSOLE";
            relationship = "admin";

            // List of SuperUsers below
            listOfUserDTOInsideDepartment = convertListDTO.convertListToListDTO(userRepository.findByRole(Constants.ROLE_SUPERUSER), UserDTO.class);

            ticketPage = ticketService.findAllWithSpecification(pageNum, status, priority);
            setTotalPagesAndElements(ticketPage.getTotalPages(), ticketPage.getTotalElements());
            listTicketsDTO = convertListDTO.convertListToListDTO(ticketPage.getContent(), TicketDTO.class);

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
                             Model model, HttpSession httpSession) throws Exception {

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
                               @RequestParam("relationship") String relationship,
                               @RequestParam("currentPage") int currentPage) throws Exception {

        String path = relationship;

        if (assigneeDTO != null) {
            ticketService.editAssignee(assigneeDTO.getAssigneeId(), ticketId);
        }

        return (REDIRECT_TO_LISTICKETS + currentPage + "?list=" + path);
    }

    @PostMapping("/filterlist")
    public String filterList(@ModelAttribute("filter") FilterDTO filterDTOfromHtml,
                             @RequestParam("relationship") String relationship,
                             @RequestParam("currentPage") int currentPage) {

        this.filterDTOfromHtml = filterDTOfromHtml;
        status = (filterDTOfromHtml.isOpen()) ? Constants.TICKET_STATUS_OPEN : null;
        priority = (filterDTOfromHtml.isHigh()) ? Constants.TICKET_PRIORITY_URGENT : null;

        return REDIRECT_TO_LISTICKETS + currentPage + "?list=" + relationship;
    }

    @GetMapping("/closeTicket")
    public String deleteTicket(@RequestParam("ticketId") int id,
                               @RequestParam("relationship") String relationshipWithUser,
                               @RequestParam("currentPage") int currentPage) throws Exception {

        Ticket ticketToClose = ticketService.getTicketById(id);
        ticketToClose.setStatus(Constants.TICKET_STATUS_CLOSED);
        ticketRepository.save(ticketToClose);
        return REDIRECT_TO_LISTICKETS + currentPage + "?list=" + relationshipWithUser;
    }

    public void setTotalPagesAndElements(int totalPages, long totalElements) {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }
}
