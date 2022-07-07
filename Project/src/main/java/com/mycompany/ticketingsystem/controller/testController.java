package com.mycompany.ticketingsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class testController {
    @GetMapping("/a")
    public String test() {
        return "test";
    }
}
