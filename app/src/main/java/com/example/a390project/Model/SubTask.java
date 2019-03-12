package com.example.a390project.Model;

public class SubTask {

    private String subTaskType;
    private String subTaskID;
    private String projectID;
    private String taskID;
    private boolean isCompleted;
    private long createdTime;
    private long startTime;
    private long endTime;

    public SubTask(String subTaskType,String subTaskID, String projectID, String taskID, boolean isCompleted, long createdTime) {
        this.subTaskType = subTaskType;
        this.subTaskID = subTaskID;
        this.projectID = projectID;
        this.taskID = taskID;
        this.isCompleted = isCompleted;
        this.createdTime = createdTime;
    }

    public String getSubTaskType() {
        return subTaskType;
    }

    public void setSubTaskType(String subTaskType) {
        this.subTaskType = subTaskType;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getSubTaskID() {
        return subTaskID;
    }

    public void setSubTaskID(String subTaskID) {
        this.subTaskID = subTaskID;
    }
}
