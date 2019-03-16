package com.example.a390project.Model;

public class PaintBucket {
    private String paintType;
    private String paintCode;
    private String paintDescription;
    private int bakeTemperature;
    private int bakeTime;
    private float paintWeight;

    public PaintBucket() {}

    public PaintBucket(String paintType, String paintCode, String paintDescription, int bakeTemp, int bakeTime, float paintWeight) {
        this.paintType = paintType;
        this.paintCode = paintCode;
        this.paintDescription = paintDescription;
        this.bakeTemperature = bakeTemp;
        this.bakeTime = bakeTime;
        this.paintWeight = paintWeight;
    }

    public String getPaintType() {
        return paintType;
    }

    public void setPaintType(String paintType) {
        this.paintType = paintType;
    }

    public String getPaintCode() {
        return paintCode;
    }

    public void setPaintCode(String paintCode) {
        this.paintCode = paintCode;
    }

    public String getPaintDescription() {
        return paintDescription;
    }

    public void setPaintDescription(String paintDescription) {
        this.paintDescription = paintDescription;
    }

    public int getBakeTemperature() {
        return bakeTemperature;
    }

    public void setBakeTemperature(int bakeTemperature) {
        this.bakeTemperature = bakeTemperature;
    }

    public int getBakeTime() {
        return bakeTime;
    }

    public void setBakeTime(int bakeTime) {
        this.bakeTime = bakeTime;
    }

    public float getPaintWeight() {
        return paintWeight;
    }

    public void setPaintWeight(float paintWeight) {
        this.paintWeight = paintWeight;
    }
}
