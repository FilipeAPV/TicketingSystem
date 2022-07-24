package com.mycompany.ticketingsystem.controller;

import com.mycompany.ticketingsystem.constants.Constants;
import com.mycompany.ticketingsystem.dto.DepartmentDTO;
import com.mycompany.ticketingsystem.dto.SuperUserDTO;
import com.mycompany.ticketingsystem.dto.TicketDTO;
import com.mycompany.ticketingsystem.dto.UserDTO;
import com.mycompany.ticketingsystem.model.Ticket;
import com.mycompany.ticketingsystem.model.User;
import com.mycompany.ticketingsystem.repository.DepartmentRepository;
import com.mycompany.ticketingsystem.repository.UserRepository;
import com.mycompany.ticketingsystem.service.TicketService;
import com.mycompany.ticketingsystem.service.UserService;
import com.mycompany.ticketingsystem.utility.ConvertListDTO;
import com.mycompany.ticketingsystem.utility.StatisticsDTO;
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
import java.util.List;
import java.util.Set;

@Controller
public class DashboardController {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final TicketService ticketService;
    private final StatisticsDTO statisticsDTO;
    private final UserService userService;
    private final DepartmentRepository departmentRepository;
    private final ConvertListDTO convertListDTO;
    private UserDTO userLoggedInDTO;

    @Autowired
    public DashboardController(UserRepository userRepository,
                               ModelMapper modelMapper,
                               TicketService ticketService,
                               StatisticsDTO statisticsDTO,
                               UserService userService,
                               DepartmentRepository departmentRepository,
                               ConvertListDTO convertListDTO) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.ticketService = ticketService;
        this.statisticsDTO = statisticsDTO;
        this.userService = userService;
        this.departmentRepository = departmentRepository;
        this.convertListDTO = convertListDTO;
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


        if (authentication.getAuthorities().stream().anyMatch(ga -> ga.getAuthority().equals("ROLE_ADMINISTRATOR"))) {
            statisticsDTO.setOpenTickets(ticketService.numberOfOpenTickets());
            statisticsDTO.setAssignedTickets(ticketService.numberOfAssignedTickets());
            statisticsDTO.setAllTickets(ticketService.numberOfAllTickets().size());
            statisticsDTO.setTotalUsers((userRepository.findAll()).size());

            model.addAttribute("statistics", statisticsDTO);
        }

        return Constants.DASHBOARD;
    }

    @PostMapping("/saveTicket")
    public String saveTicket(@Valid @ModelAttribute("ticket") TicketDTO ticketDTO,
                             Errors errors, Model model, HttpSession httpSession,
                             @RequestParam(value = "relationshipWithUser", required = false) String relationshipWithUser,
                             @RequestParam(value = "currentPage", required = false) Integer currentPage) {



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

        Ticket ticketToSave = modelMapper.map(ticketDTO, Ticket.class);
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
            } else if (relationshipWithUser.equals("department")){
                path = "department";
            } else {
                path = "admin";
            }
            return ("redirect:/listTickets/" + currentPage +"?list=" + path);
        }
    }

    @GetMapping("/listSuperUsers")
    public String listSuperUsers(Model model,
                                 @RequestParam(name = "saved", required = false) String isSaved) {
        List<UserDTO> userDTOList = convertListDTO.convertListToListDTO(new UserDTO(), userService.getAllUsers(), UserDTO.class);
        List<DepartmentDTO> departmentDTOList = convertListDTO.convertListToListDTO(new DepartmentDTO(), departmentRepository.findAll(), DepartmentDTO.class);

        model.addAttribute("users", userDTOList);
        model.addAttribute("departments", departmentDTOList);
        model.addAttribute("superUserDTO", new SuperUserDTO());

        if (isSaved != null) {
            model.addAttribute("message", "Super User successfully assigned");
        }

        return "list-superusers";
    }

    @PostMapping("/saveSuperUser")
    public String saveSuperUser(@ModelAttribute(name = "superUserDTO") SuperUserDTO superUserDTO,
                                @RequestParam(name = "departmentId") int departmentId,
                                Model model) {

        boolean isSaved = userService.changeRoleToSuperUser(superUserDTO, departmentId);

        if (isSaved) {
            return "redirect:/listSuperUsers?saved=true";
        } else {
            return "redirect:/listSuperUsers";
        }
    }
}
