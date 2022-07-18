package com.mycompany.ticketingsystem.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "departments")
public class Department extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private int id;

    private String name;

    @OneToOne
    @JoinColumn(name = "super_user_id", referencedColumnName = "user_id", nullable = true)
    private User superUserId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "department")
    private Set<User> userList = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "assigneeDepartment")
    private Set<Ticket> ticketListPerDepartment = new HashSet<>();

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

    public Set<User> getUserList() {
        return userList;
    }

    public void setUserList(User user) {
        this.userList.add(user);
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", superUser_id=" + superUserId +
                //", userList=" + userList + - commented on 11/07/2022 because LazyInitializationException with Spring Security
                '}';
    }

    public User getSuperUserId() {
        return superUserId;
    }

    public void setSuperUserId(User superUserId) {
        this.superUserId = superUserId;
    }

    public Set<Ticket> getTicketListPerDepartment() {
        return ticketListPerDepartment;
    }

    public void setTicketListPerDepartment(Ticket ticket) {
        this.ticketListPerDepartment.add(ticket);
    }
}
