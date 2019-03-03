package com.example.a390project.Model;

public class Project {
    String ID;
    String Title;
    String PO;
    String ManagerID;
    String Client;
    String ManagerComment;
    String DueDate;
    String TimeTaken;
    String EmployeeComment;

    public Project(String id, String title, String client, String po, String due){
        ID = id;
        Title = title;
        Client = client;
        PO = po;
        DueDate = due;
        ManagerID = "";
        ManagerComment = "";
        TimeTaken = "";
        EmployeeComment = "";
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getManagerID() {
        return ManagerID;
    }

    public void setManagerID(String managerID) {
        ManagerID = managerID;
    }

    public String getManagerComment() {
        return ManagerComment;
    }

    public void setManagerComment(String managerComment) {
        ManagerComment = managerComment;
    }

    public String getPO() {
        return PO;
    }

    public void setPO(String PO) {
        this.PO = PO;
    }

    public String getClient() {
        return Client;
    }

    public void setClient(String client) {
        Client = client;
    }

    public String getDueDate() {
        return DueDate;
    }

    public void setDueDate(String dueDate) {
        DueDate = dueDate;
    }

    public String getTimeTaken() {
        return TimeTaken;
    }

    public void setTimeTaken(String timeTaken) {
        TimeTaken = timeTaken;
    }

    public String getEmployeeComment() {
        return EmployeeComment;
    }

    public void setEmployeeComment(String employeeComment) {
        EmployeeComment = employeeComment;
    }
}
