package com.mycompany.ticketingsystem.controller;

import com.mycompany.ticketingsystem.model.User;
import com.mycompany.ticketingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private UserRepository userRepository;

    @Autowired
    public DashboardController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/dashboard")
    public String getDashboard(Authentication authentication, Model model) {

        User userLoggedIn = userRepository.findByEmail(authentication.getName());

        model.addAttribute("userLoggedIn", userLoggedIn);

        return "dashboard";
    }
}
