package com.example.a390project.Model;

import java.util.ArrayList;

public class EmployeeTasks{

    private static final String TAG = "EmployeeTasksFragment";

    private ArrayList<Integer> employeeIDs = new ArrayList<>(); // <----- List of employees working on the task
    private int projectID; // <---------------- ID of project that the task belongs to
    private int taskID;
    private String taskTitle;

    // -------------------- Constructor --------------------
    EmployeeTasks(int taskID, String taskTitle, ArrayList<Integer> employeeIDs){
        this.taskID = taskID;
        this.taskTitle = taskTitle;
        this.employeeIDs = employeeIDs;
    }

    public EmployeeTasks(int taskID, String taskTitle){
        this.taskID = taskID;
        this.taskTitle = taskTitle;
    }

    // -------------------- Setters --------------------

    public void addEmployeeToTask(int employeeID) {
        employeeIDs.add(employeeID);
    }

    public void setTaskTitle(String taskTitle){
        this.taskTitle = taskTitle;
    }

    public void setProjectID(int projectID) { this.projectID = projectID; }

    // -------------------- Getters --------------------

    public int getTaskID(){return taskID;}

    public String getTaskTitle(){return taskTitle;}

    public ArrayList<Integer> getEmployeeIDs(){return employeeIDs;}

    public int getProjectID() { return projectID; }


}
