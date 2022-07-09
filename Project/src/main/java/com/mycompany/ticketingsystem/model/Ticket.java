package com.mycompany.ticketingsystem.model;

import javax.persistence.*;

@Entity
@Table(name = "tickets")
public class Ticket extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private int id;
    private String status;
    private String subject;
    private String priority;
    private String message;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "creator_id", referencedColumnName = "user_id")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "assignee_id", referencedColumnName = "user_id", nullable = true)
    private User assignee;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", subject='" + subject + '\'' +
                ", priority='" + priority + '\'' +
                ", message='" + message + '\'' +
                ", creator=" + creator.getFirstName() +
                ", assignee=" + assignee +
                '}';
    }
}
