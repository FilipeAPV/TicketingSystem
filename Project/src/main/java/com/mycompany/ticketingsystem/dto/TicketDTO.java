package com.mycompany.ticketingsystem.dto;

import com.mycompany.ticketingsystem.constants.Constants;
import com.mycompany.ticketingsystem.model.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class TicketDTO {
    private int id;
    private String status = Constants.TICKET_STATUS_OPEN;
    @NotBlank(message = "Subject field cannot be blank")
    private String subject;
    private String priority;
    @NotBlank(message = "Message field cannot be blank")
    private String message;
    private String addMessage;
    private User creator;
    private LocalDateTime createdAt;
    private User assignee;

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public String getAddMessage() {
        return addMessage;
    }

    public void setAddMessage(String addMessage) {
        this.addMessage = addMessage;
    }
}
