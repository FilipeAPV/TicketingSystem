package com.mycompany.ticketingsystem.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionController {
    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception exception,
                                   Model model){
        model.addAttribute("errormsg", exception.getMessage());
        return "error";
    }

}
