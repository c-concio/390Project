package com.example.a390project;

import java.util.ArrayList;

public class Employee {

    // variables
    private int employeeID;
    private String employeeName;
    private ArrayList<Integer> taskIDs = new ArrayList<>(); // < --------- tasks associated to the employee

    // -------------------- Constructor --------------------
    Employee(int employeeID, String employeeName, ArrayList<Integer> taskIDs){
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.taskIDs = taskIDs;
    }

    Employee(int employeeID, String employeeName){
        this.employeeID = employeeID;
        this.employeeName = employeeName;
    }

    // -------------------- Setters --------------------

    public void setEmployeeName(String employeeName){
        this.employeeName = employeeName;
    }

    public void addTask(int taskID){
        taskIDs.add(taskID);
    }

    // -------------------- Getters --------------------

    public int getEmployeeID() { return employeeID; }

    public String getEmployeeName(){ return employeeName; }

    public ArrayList<Integer> getTaskIDs() { return taskIDs; }



}
