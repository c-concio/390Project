package com.example.a390project.Model;

public class Project {
    String po;
    String title;
    String client;
    long startDate;
    long dueDate;
    Boolean hasCompleted = false;

    //constructor used by FirebaseHelper method 'createProject(String PO, String title, String client, long startDate, long dueDate)'
    public Project(String po, String title, String client, long startDate, long dueDate){
        this.po = po;
        this.title = title;
        this.client = client;
        this.startDate = startDate;
        this.dueDate = dueDate;
    }

    public Project(String po, String title, String client, long startDate, long dueDate, Boolean hasCompleted){
        this.po = po;
        this.title = title;
        this.client = client;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.hasCompleted = hasCompleted;
    }

    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public Boolean getHasCompleted() {
        return hasCompleted;
    }

    public void setHasCompleted(Boolean hasCompleted) {
        this.hasCompleted = hasCompleted;
    }
}
