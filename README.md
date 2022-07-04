# TicketingSystem

## Objective

With this project I want to apply the knowledge that I've acquired during my first University semester and after have studied some Udemy courses about Core Java, Spring, Spring Boot and Spring Data JPA.

I'll try, as much as possible, to document the thought process that motivated my decisions.

## Technologies / Projects Used

- Thymeleaf
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL

## Project Description

### Program Flow

1. User raises tickets via form
2. All tickets are redirected to a _dispatcher_ that sends them to the correct department.
3. Each department has a _SuperUser_ that assigns the ticket

### Requirements

- Ticket

  - 4 status: Open, Delivered, Closed and Invalid
  - The user needs to be able to select the department that he thinks is concerned by the ticket.
  - All participants should be able to comment on the ticket and all the comments need to be recorded.

- Authentication & Authorization

  - Roles: user, superuser, admin
    - user: open tickets, list tickets opened by himself, update and delete these.
    - superuser: assign tickets, comment on all tickets assigned to his department, change assignee.
    - admin: view all tickets from all departments, classify tickets as invalid, determine department superusers

- Auditing

  - All changes need to be audited by having:
    - created by / at
    - updated by / at

- Validation

  - Form fields need to be validated

- List of items (Users, Messages, etc)
  - Need to be easily sorted and offer pagination
