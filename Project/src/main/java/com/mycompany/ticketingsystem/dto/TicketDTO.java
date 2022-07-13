package com.mycompany.ticketingsystem.dto;

import com.mycompany.ticketingsystem.constants.Constants;
import com.mycompany.ticketingsystem.model.User;

import javax.validation.constraints.NotBlank;

public class TicketDTO {
    private String status = Constants.TICKET_STATUS_OPEN;

    @NotBlank(message = "Subject field cannot be blank")
    private String subject;
    private String priority;
    @NotBlank(message = "Message field cannot be blank")
    private String message;
    private User creator;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}