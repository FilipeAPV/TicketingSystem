package com.mycompany.ticketingsystem.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/public/")
public class LoginController {
    @GetMapping("/login")
    public String showLogin(Model model,
                            @RequestParam(value = "successRegistered", required = false) String successRegistered,
                            @RequestParam(value = "error", required = false) String errorLogin,
                            @RequestParam(value = "logout", required = false) String logout) {
        String message = null;
        if (successRegistered != null) {
            message = "User successfully registered. Please Login";
        } else if (errorLogin != null) {
            message = "Invalid Credentials";
        } else if (logout != null) {
            message = "Logout successful!";
        }
        model.addAttribute("message", message);
        return "login";
    }

    @GetMapping("/logout")
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/public/login?logout=true";
    }

}
