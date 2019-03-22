package com.example.a390project.Model;

import java.util.Random;

public class WorkBlock {

    private String workBlockID;
    private long startTime;
    private long endTime;
    private long workingTime;
    private String taskID;
    private String employeeID;
    private String title;
    private String projectPO;

    public WorkBlock(String workBlockID, long startTime, long endTime, long workingTime, String taskID, String employeeID, String title, String projectPO) {
        this.workBlockID = workBlockID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.workingTime = workingTime;
        this.taskID = taskID;
        this.employeeID = employeeID;
        this.title = title;
        this.projectPO = projectPO;
    }

    public String getProjectPO() {
        return projectPO;
    }

    public void setProjectPO(String projectPO) {
        this.projectPO = projectPO;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWorkBlockID() {
        return workBlockID;
    }

    public void setWorkBlockID(String workBlockID) {
        this.workBlockID = workBlockID;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(long workingTime) {
        this.workingTime = workingTime;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public static String generateRandomChars() {
        int length = 16;
        String candidateChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(candidateChars.charAt(random.nextInt(candidateChars
                    .length())));
        }

        return sb.toString();
    }
}
