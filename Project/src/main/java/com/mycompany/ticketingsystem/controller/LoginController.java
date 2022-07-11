package com.mycompany.ticketingsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/public/")
public class LoginController {
    @GetMapping("/login")
    public String showLogin(Model model,
                            @RequestParam(value = "successRegistered", required = false) String successRegistered) {
        String message = null;
        if (successRegistered != null) {
            message = "User successfully registered. Please Login";
        }
        model.addAttribute("message", message);
        return "login";
    }
}
