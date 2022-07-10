package com.mycompany.ticketingsystem.controller;

import com.mycompany.ticketingsystem.model.Department;
import com.mycompany.ticketingsystem.model.User;
import com.mycompany.ticketingsystem.repository.DepartmentRepository;
import com.mycompany.ticketingsystem.repository.UserRepository;
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
import java.util.List;

@Controller
@RequestMapping("/public")
public class RegistrationController {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    @Autowired
    public RegistrationController(DepartmentRepository departmentRepository, UserRepository userRepository) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/registrationForm")
    public String showRegistrationForm(Model model, HttpSession httpSession) {

        User modelUser = new User();
        List<Department> departmentList = departmentRepository.findAll();

        model.addAttribute("newUser", modelUser);
        model.addAttribute("departmentList", departmentList);

        httpSession.setAttribute("departmentList", departmentList);

        return "register";
    }

    @PostMapping("/saveRegistrationForm")
    public String saveRegistrationForm(@Valid @ModelAttribute("newUser") User user, Errors errors,
                                       Model model, HttpSession httpSession) {
        //TODO error verification
        //TODO persistence strategy, inclufing login?val=true

        if (errors.hasErrors()) {
            List<User> userList = (List<User>) httpSession.getAttribute("departmentList");
            model.addAttribute("departmentList", userList);
            return "register";
        }

        User savedUser = userRepository.save(user);
            return "redirect:/public/login";
    }

}
