package com.example.a390project.Model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class User {

    private String name;
    private String email;
    private Map<String, String> timeCreated;
    private String id;
    private boolean isManager;

    public User(String name, String email, Map<String, String> timeCreated, boolean isManager) {
        this.name = name;
        this.email = email;
        this.timeCreated = timeCreated;
        this.isManager = isManager;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, String> getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Map<String, String> timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }




}