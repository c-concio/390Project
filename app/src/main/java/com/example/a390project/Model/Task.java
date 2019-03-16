package com.example.a390project.Model;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class Task {
    private String projectPO;
    private String taskType;
    private String description;
    private String employeeComment;
    private String taskID;
    private long createdTime;
    private long startTime;
    private long endTime;
    private long date;
    private int partCounted = 0;
    private int partAccepted = 0;
    private int partRejected = 0;
    private List<SubTask> subTasks;


    // prepaint variables
    private String prepaintName;

    // baking variables <---------------------USE PAINTBUCKET MODEL INSTEAD
    private String paintCode;
    private String paintType;

    public Task(){}

    public Task(String taskID, String projectPO, String taskType, String description, long createdTime) {
        this.taskID = taskID;
        this.projectPO = projectPO;
        this.taskType = taskType;
        this.description = description;
        this.createdTime = createdTime;
    }

    //constructor to use when creating a prepaint task
    public Task(String taskID, String projectPO, String taskType, String description, long createdTime, List<SubTask> subTasks) {
        this.taskID = taskID;
        this.projectPO = projectPO;
        this.taskType = taskType;
        this.description = description;
        this.createdTime = createdTime;
        this.subTasks = subTasks;
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

    public String getProjectPO() {
        return projectPO;
    }

    public void setProjectPO(String projectPO) {
        this.projectPO = projectPO;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getEmployeeComment() {
        return employeeComment;
    }

    public void setEmployeeComment(String employeeComment) {
        this.employeeComment = employeeComment;
    }

    public String getPrepaintName() {
        return prepaintName;
    }

    public void setPrepaintName(String prepaintName) {
        this.prepaintName = prepaintName;
    }

    public int getPartCounted() {
        return partCounted;
    }

    public void setPartCounted(int partCounted) {
        this.partCounted = partCounted;
    }

    public int getPartAccepted() {
        return partAccepted;
    }

    public void setPartAccepted(int partAccepted) {
        this.partAccepted = partAccepted;
    }

    public int getPartRejected() {
        return partRejected;
    }

    public void setPartRejected(int partRejected) {
        this.partRejected = partRejected;
    }

    public String getPaintCode() {
        return paintCode;
    }

    public void setPaintCode(String paintCode) {
        this.paintCode = paintCode;
    }

    public String getPaintType() {
        return paintType;
    }

    public void setPaintType(String paintType) {
        this.paintType = paintType;
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<SubTask> subTasks) {
        this.subTasks = subTasks;
    }
}
