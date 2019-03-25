package com.example.a390project.Model.TaskClasses;

public class Inventory {

    private String paintDescription;
    private long bakeTime;
    private long bakeTemperature;
    private long paintWeight;

    public String getPaintDescription() {
        return paintDescription;
    }

    public void setPaintDescription(String paintDescription) {
        this.paintDescription = paintDescription;
    }

    public long getBakeTime() {
        return bakeTime;
    }

    public void setBakeTime(long bakeTime) {
        this.bakeTime = bakeTime;
    }

    public long getBakeTemperature() {
        return bakeTemperature;
    }

    public void setBakeTemperature(long bakeTemperature) {
        this.bakeTemperature = bakeTemperature;
    }

    public long getPaintWeight() {
        return paintWeight;
    }

    public void setPaintWeight(long paintWeight) {
        this.paintWeight = paintWeight;
    }
}
