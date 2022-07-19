package com.mycompany.ticketingsystem.repository;

import com.mycompany.ticketingsystem.model.Department;
import com.mycompany.ticketingsystem.model.Ticket;
import com.mycompany.ticketingsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByCreatorAndStatusOrderByCreatedAt(User user, String status);
    List<Ticket> findByAssigneeAndStatusOrderByCreatedAt(User user, String status);
    List<Ticket> findByAssigneeDepartmentAndStatusOrderByCreatedAt(Department department, String status);
    List<Ticket> findByStatus(String status);
    List<Ticket> findByAssigneeIsNotNullAndStatus(String status);
}
