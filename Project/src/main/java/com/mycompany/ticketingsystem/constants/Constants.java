package com.mycompany.ticketingsystem.constants;

import java.util.List;

public interface Constants {
    public static final String REGISTRATIONFORM = "register";
    public static final String DEPARTMENT_LIST = "departmentList";
    public static final String ROLE_USER = "ROLE_USER";

    public static final String TICKET_STATUS_OPEN = "OPEN";
    public static final String TICKET_STATUS_DELIVERED = "DELIVERED";
    public static final String TICKET_STATUS_CLOSED = "CLOSED";
    public static final String TICKET_STATUS_INVALID = "INVALID";

    List<String> ticketStatus = List.of(TICKET_STATUS_OPEN, TICKET_STATUS_DELIVERED, TICKET_STATUS_CLOSED, TICKET_STATUS_INVALID);

    public static final String TICKET_PRIORITY_STANDARD = "STANDARD";
    public static final String TICKET_PRIORITY_URGENT = "URGENT";

    List<String> ticketPriority = List.of(TICKET_PRIORITY_STANDARD, TICKET_PRIORITY_URGENT);

    public static final String DASHBOARD = "dashboard";
}
