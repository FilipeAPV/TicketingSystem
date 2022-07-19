package com.mycompany.ticketingsystem.utility;

import org.springframework.stereotype.Component;

@Component
public class StatisticsDTO {

    private int openTickets;
    private int assignedTickets;
    private int totalUsers;
    private int allTickets;

    public int getOpenTickets() {
        return openTickets;
    }

    public void setOpenTickets(int openTickets) {
        this.openTickets = openTickets;
    }

    public int getAssignedTickets() {
        return assignedTickets;
    }

    public void setAssignedTickets(int assignedTickets) {
        this.assignedTickets = assignedTickets;
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }

    public int getAllTickets() {
        return allTickets;
    }

    public void setAllTickets(int allTickets) {
        this.allTickets = allTickets;
    }
}
