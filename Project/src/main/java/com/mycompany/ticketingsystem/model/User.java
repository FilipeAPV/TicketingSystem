package com.mycompany.ticketingsystem.model;

import com.mycompany.ticketingsystem.annotation.EmailMustBeUnique;
import com.mycompany.ticketingsystem.annotation.FieldsValueMatch;
import com.mycompany.ticketingsystem.constants.Constants;
import com.mycompany.ticketingsystem.controller.ListTicketsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "job_title")
    private String jobTitle;

    private String email;

    @Transient
    private String confirmEmail;

    private String manager;

    private String password;

    @Transient
    private String confirmPassword;

    private String role = Constants.ROLE_USER;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_department_id", referencedColumnName = "department_id")
    private Department department;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "creator")
    private Set<Ticket> createdTicketsList = new HashSet();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "assignee")
    private Set<Ticket> assignedTicketsList = new HashSet();

    private static Logger log = LoggerFactory.getLogger(User.class);

    public User() {}

    public User(int id, String firstName, String lastName, String jobTitle, String email, String confirmEmail, String manager, String password, String confirmPassword, String role, Department department, Set<Ticket> createdTicketsList, Set<Ticket> assignedTicketsList) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobTitle = jobTitle;
        this.email = email;
        this.confirmEmail = confirmEmail;
        this.manager = manager;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.role = role;
        this.department = department;
        this.createdTicketsList = createdTicketsList;
        this.assignedTicketsList = assignedTicketsList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmEmail() {
        return confirmEmail;
    }

    public void setConfirmEmail(String confirmEmail) {
        this.confirmEmail = confirmEmail;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", email='" + email + '\'' +
                ", manager='" + manager + '\'' +
                ", password='" + password + '\'' +
                ", department=" + department.getName() +
                '}';
    }

    public Set<Ticket> getCreatedTicketsList() {
        return createdTicketsList;
    }

    public void setCreatedTicketsList(Ticket ticket) {
        log.info(ticket + "has been added to " + this.getEmail() + " created tickets list");
        this.createdTicketsList.add(ticket);
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
