package com.mycompany.ticketingsystem.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "departments")
public class Department extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private int id;

    private String name;

    @OneToOne
    @JoinColumn(name = "super_user_id", referencedColumnName = "user_id", nullable = true)
    private User superUser_id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "department")
    private Set<User> userList = new HashSet<>();

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
                ", superUser_id=" + superUser_id +
                ", userList=" + userList +
                '}';
    }

    public User getSuperUser_id() {
        return superUser_id;
    }

    public void setSuperUser_id(User superUser_id) {
        this.superUser_id = superUser_id;
    }
}
