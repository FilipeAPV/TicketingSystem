package com.mycompany.ticketingsystem.controller;

import com.mycompany.ticketingsystem.constants.Constants;
import com.mycompany.ticketingsystem.dto.UserDTO;
import com.mycompany.ticketingsystem.model.Department;
import com.mycompany.ticketingsystem.model.User;
import com.mycompany.ticketingsystem.repository.DepartmentRepository;
import com.mycompany.ticketingsystem.repository.UserRepository;
import com.mycompany.ticketingsystem.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/public")
public class RegistrationController {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    private List<Department> departmentList;

    @Autowired
    public RegistrationController(DepartmentRepository departmentRepository, UserRepository userRepository,
                                  UserService userService, ModelMapper modelMapper) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/registrationForm")
    public String showRegistrationForm(Model model, HttpSession httpSession) {

        UserDTO modelUser = new UserDTO();
        departmentList = departmentRepository.findAll();

        model.addAttribute("newUser", modelUser);
        model.addAttribute(Constants.DEPARTMENT_LIST, departmentList);

        return Constants.REGISTRATIONFORM;
    }

    @PostMapping("/saveRegistrationForm")
    public String saveRegistrationForm(@Valid @ModelAttribute("newUser") UserDTO userDTO, Errors errors,
                                       Model model, HttpSession httpSession) {

        if (errors.hasErrors()) {
            model.addAttribute(Constants.DEPARTMENT_LIST, departmentList);
            return Constants.REGISTRATIONFORM;
        }

        User user = modelMapper.map(userDTO, User.class);

        boolean isSaved = userService.saveUser(user);
        if (isSaved){
            return "redirect:/public/login?successRegistered=true";
        } else {
            return Constants.REGISTRATIONFORM;
        }

    }

}
