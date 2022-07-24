package com.mycompany.ticketingsystem.dto;

import com.mycompany.ticketingsystem.model.User;

public class DepartmentDTO {
    private int id;
    private String name;
    private User superUserId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getSuperUserId() {
        return superUserId;
    }

    public void setSuperUserId(User superUserId) {
        this.superUserId = superUserId;
    }
}
