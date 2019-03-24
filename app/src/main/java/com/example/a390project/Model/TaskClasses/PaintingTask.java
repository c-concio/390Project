package com.example.a390project.Model.TaskClasses;

public class PaintingTask {

    private String paintCode;
    private int bakingTime;
    private String paintType;
    private String description;

    // liquid
    private long viscosity;
    private long tipSize;
    private long pressure;

    // powder
    private long amount;
    private long spread;
    private Boolean recoat;

    public String getPaintCode() {
        return paintCode;
    }

    public void setPaintCode(String paintCode) {
        this.paintCode = paintCode;
    }

    public int getBakingTime() {
        return bakingTime;
    }

    public void setBakingTime(int bakingTime) {
        this.bakingTime = bakingTime;
    }

    public String getPaintType() {
        return paintType;
    }

    public void setPaintType(String paintType) {
        this.paintType = paintType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getViscosity() {
        return viscosity;
    }

    public void setViscosity(long viscosity) {
        this.viscosity = viscosity;
    }

    public long getTipSize() {
        return tipSize;
    }

    public void setTipSize(long tipSize) {
        this.tipSize = tipSize;
    }

    public long getPressure() {
        return pressure;
    }

    public void setPressure(long pressure) {
        this.pressure = pressure;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getSpread() {
        return spread;
    }

    public void setSpread(long spread) {
        this.spread = spread;
    }

    public Boolean getRecoat() {
        return recoat;
    }

    public void setRecoat(Boolean recoat) {
        this.recoat = recoat;
    }
}
