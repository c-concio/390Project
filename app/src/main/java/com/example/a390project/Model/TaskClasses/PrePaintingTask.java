package com.example.a390project.Model.TaskClasses;

public class PrePaintingTask {

    private long sandblastingHours;
    private long sandingHours;
    private long cleaningHours;
    private long iriditeHours;
    private long maskingHours;

    public PrePaintingTask(long sandblastingHours, long sandingHours, long cleaningHours, long iriditeHours, long maskingHours){
        this.sandblastingHours = sandblastingHours;
        this.sandingHours = sandingHours;
        this.cleaningHours = cleaningHours;
        this.iriditeHours = iriditeHours;
        this.maskingHours = maskingHours;
    }

    public long getSandblastingHours() {
        return sandblastingHours;
    }

    public void setSandblastingHours(long sandblastingHours) {
        this.sandblastingHours = sandblastingHours;
    }

    public long getSandingHours() {
        return sandingHours;
    }

    public void setSandingHours(long sandingHours) {
        this.sandingHours = sandingHours;
    }

    public long getCleaningHours() {
        return cleaningHours;
    }

    public void setCleaningHours(long cleaningHours) {
        this.cleaningHours = cleaningHours;
    }

    public long getIriditeHours() {
        return iriditeHours;
    }

    public void setIriditeHours(long iriditeHours) {
        this.iriditeHours = iriditeHours;
    }

    public long getMaskingHours() {
        return maskingHours;
    }

    public void setMaskingHours(long maskingHours) {
        this.maskingHours = maskingHours;
    }
}
