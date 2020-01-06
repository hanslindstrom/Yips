package com.example.Yips;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable=false)
    private String username;
    private String email;

    @Column(nullable=false)
    private String password;

    //If the user has entered wrong password 3 times she should be blocked (not active)
    private int active;

    //Setting roles to empty string to avoid nullpointerexceptions
    private String roles = "";

    //If we choose to use permissions (roles might be enough).
    //private String permissions;

    protected User(String username, String email, String password, String roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        //Setting user to active (not blocked).
        this.active = 1;
    }

    protected User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public List<String> getRoleList() {
        if (this.roles.length()>0) {
            return Arrays.asList(this.roles.split(","));
        } else {
            return new ArrayList<>();
        }

    }
}
