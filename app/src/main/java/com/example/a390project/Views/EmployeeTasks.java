package com.example.a390project.Views;

import java.util.ArrayList;

public class EmployeeTasks{

    private static final String TAG = "EmployeeTasks";

    private ArrayList<Integer> employeeIDs = new ArrayList<>(); // <----- List of employees working on the task
    private int taskID;
    private String taskTitle;
    private String taskDescription;

    // -------------------- Constructor --------------------
    EmployeeTasks(int taskID, String taskTitle, String taskDescription, ArrayList<Integer> employeeIDs){
        this.taskID = taskID;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.employeeIDs = employeeIDs;
    }

    EmployeeTasks(int taskID, String taskTitle, String taskDescription){
        this.taskID = taskID;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
    }

    // -------------------- Setters --------------------

    public void addEmployeeToTask(int employeeID) {
        employeeIDs.add(employeeID);
    }

    public void setTaskTitle(String taskTitle){
        this.taskTitle = taskTitle;
    }

    public void setTaskDescription(String taskDescription){
        this.taskDescription = taskDescription;
    }

    // -------------------- Getters --------------------

    public int getTaskID(){return taskID;}

    public String getTaskTitle(){return taskTitle;}

    public String getTaskDescription(){return taskDescription;}

    public ArrayList<Integer> getEmployeeIDs(){return employeeIDs;}



}
