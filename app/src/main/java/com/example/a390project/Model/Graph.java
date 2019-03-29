package com.example.a390project.Model;

public class Graph {

    private String graphTitle;
    private String machineTitle;
    private String graphID;
    private long startTime;

    public Graph(String graphTitle, String machineTitle, String graphID, long startTime) {
        this.graphTitle = graphTitle;
        this.machineTitle = machineTitle;
        this.graphID = graphID;
        this.startTime = startTime;
    }

    public String getGraphTitle() {
        return graphTitle;
    }

    public void setGraphTitle(String graphTitle) {
        this.graphTitle = graphTitle;
    }

    public String getMachineTitle() {
        return machineTitle;
    }

    public void setMachineTitle(String machineTitle) {
        this.machineTitle = machineTitle;
    }

    public String getGraphID() {
        return graphID;
    }

    public void setGraphID(String graphID) {
        this.graphID = graphID;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
