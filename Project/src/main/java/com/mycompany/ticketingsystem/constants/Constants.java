package com.mycompany.ticketingsystem.constants;

import java.util.List;

public final class Constants {
    public static final String REGISTRATIONFORM = "register";
    public static final String DEPARTMENT_LIST = "departmentList";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_SUPERUSER = "ROLE_SUPERUSER";

    public static final String TICKET_STATUS_OPEN = "OPEN";
    public static final String TICKET_STATUS_DELIVERED = "DELIVERED";
    public static final String TICKET_STATUS_CLOSED = "CLOSED";
    public static final String TICKET_STATUS_INVALID = "INVALID";

    public static final List<String> ticketStatus = List.of(TICKET_STATUS_OPEN, TICKET_STATUS_DELIVERED, TICKET_STATUS_CLOSED, TICKET_STATUS_INVALID);

    public static final String TICKET_PRIORITY_STANDARD = "STANDARD";
    public static final String TICKET_PRIORITY_URGENT = "URGENT";

    public static final List<String> ticketPriority = List.of(TICKET_PRIORITY_STANDARD, TICKET_PRIORITY_URGENT);

    public static final String USER_CURRENTLY_LOGGED_IN = "userLoggedIn";
    public static final String REDIRECT_TO_LISTICKETS = "redirect:/listTickets/";
    public static final String DASHBOARD = "dashboard";

    public static final int PAGE_SIZE = 3;
}
