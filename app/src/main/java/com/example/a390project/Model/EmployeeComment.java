package com.example.a390project.Model;

public class EmployeeComment {

    private String comment;
    private long date;
    private String username;

    EmployeeComment(){}

    EmployeeComment(String comment, long date, String username){
        this.comment = comment;
        this.date = date;
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
