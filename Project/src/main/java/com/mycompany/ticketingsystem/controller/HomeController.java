package com.mycompany.ticketingsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String sendToLogin() {
        return "redirect:/public/login";
    }
}
