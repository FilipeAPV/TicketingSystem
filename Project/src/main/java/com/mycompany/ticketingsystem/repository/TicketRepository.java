package com.mycompany.ticketingsystem.repository;

import com.mycompany.ticketingsystem.model.Department;
import com.mycompany.ticketingsystem.model.Ticket;
import com.mycompany.ticketingsystem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer>, JpaSpecificationExecutor<Ticket> {
    Page<Ticket> findByCreatorAndStatusOrderByCreatedAt(User user, String status, Pageable pageable);
    Page<Ticket> findByAssigneeAndStatusOrderByCreatedAt(User user, String status, Pageable pageable);
    Page<Ticket> findByAssigneeDepartmentAndStatusOrderByCreatedAt(Department department, String status, Pageable pageable);
    List<Ticket> findByStatus(String status);
    List<Ticket> findByAssigneeIsNotNullAndStatus(String status);
    List<Ticket> findAllByOrderByCreatedAt();
}
