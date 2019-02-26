package com.example.a390project.Model;

import java.util.ArrayList;

public class Employee {

    // variables
    private int accountID;
    private String employeeName;
    private String employeePassword;
    private ArrayList<Integer> taskIDs = new ArrayList<>(); // < --------- tasks associated to the employee

    // -------------------- Constructor --------------------
    Employee(int employeeID, String employeeName,String employeepassword, ArrayList<Integer> taskIDs){
        this.accountID = employeeID;
        this.employeeName = employeeName;
        this.employeePassword = employeepassword;
        this.taskIDs = taskIDs;
    }



    public Employee(int employeeID, String employeeName, String employeepassword){
        this.accountID = employeeID;
        this.employeeName = employeeName;
        this.employeePassword = employeepassword;
    }

    // -------------------- Setters --------------------
    public String getEmployeePassword() {
        return employeePassword;
    }

    public void setEmployeePassword(String employeePassword) {
        this.employeePassword = employeePassword;
    }
    public void setEmployeeName(String employeeName){
        this.employeeName = employeeName;
    }

    public void addTask(int taskID){
        taskIDs.add(taskID);
    }

    // -------------------- Getters --------------------

    public int getEmployeeID() { return accountID; }

    public String getEmployeeName(){ return employeeName; }

    public ArrayList<Integer> getTaskIDs() { return taskIDs; }



}
