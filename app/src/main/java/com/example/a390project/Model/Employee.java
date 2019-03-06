package com.example.a390project.Model;

import java.util.ArrayList;

public class Employee {

    // variables
    private String accountID;
    private String name;
    private Boolean manager;
    private String email;
    private String timeCreated;

    Employee(){}

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getManager() {
        return manager;
    }

    public void setManager(Boolean manager) {
        this.manager = manager;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
