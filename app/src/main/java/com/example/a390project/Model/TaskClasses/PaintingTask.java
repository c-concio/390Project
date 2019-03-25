package com.example.a390project.Model.TaskClasses;

public class PaintingTask {

    // paint information
    private String paintCode;
    private String paintType;
    private String description;

    // liquid
    private long viscosity;
    private long tipSize;

    // powder
    private long amount;
    private long spread;
    private Boolean recoat;

    // liquid and powder
    private long pressure;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
