# TicketingSystem

## Objective

With this project I want to apply the knowledge that I've acquired during my first University semester and after have studied some Udemy courses about Core Java, Spring, Spring Boot and Spring Data JPA.

I'll try, as much as possible, to document the thought process that motivated my decisions.

**Feel free to check a video presentation of the project: [Ticketing System](https://youtu.be/E8m34U5u2V4)**

## Technologies / Projects Used

- Thymeleaf
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL

## Project Description

### Program Flow

1. User raises tickets via form (after registration)
2. All tickets are redirected to a _dispatcher_ that sends them to the correct department.
3. Each department has a _SuperUser_ that assigns the ticket

### Requirements

- Ticket

  - 2 status: Open and Closed.
  - All participants should be able to comment on the ticket and all the comments need to be recorded.

- Authentication & Authorization

  - Roles: anonymous, user, superuser, admin
    - anonymous: access register form
    - user: open tickets, list tickets opened by himself, update and delete these.
    - superuser: assign tickets to users inside his department, comment on those tickets and change assignee.
    - admin: view all tickets from all departments, assign tickets to superusers, determine department superusers.

- Auditing

  - All changes need to be audited by having:
    - created by / at
    - updated by / at

- Validation

  - Form fields need to be validated

- List of items (Users, Messages, etc)
  - List tickets per assignee, department.
  - Need to be easily sorted and offer pagination
