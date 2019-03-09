package com.example.a390project.Model;

import java.util.Random;

public class Task {
    private String projectPO;
    private String taskType;
    private String description;
    private String employeeComment;
    private long createdTime;
    private long startTime;
    private long endTime;

    public Task(String projectPO, String taskType, String description, long createdTime) {
        this.projectPO = projectPO;
        this.taskType = taskType;
        this.description = description;
        this.createdTime = createdTime;
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

}
