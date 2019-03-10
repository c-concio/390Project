package com.example.a390project.Model;

public class ControlDevice {

    private String cDeviceTitle;
    private boolean cDeviceStatus;

    public ControlDevice(String cDeviceTitle, boolean cDeviceStatus){
        this.cDeviceTitle = cDeviceTitle;
        this.cDeviceStatus = cDeviceStatus;
    }

    // -------------------- Setters --------------------

    public void setcDeviceTitle(String cDeviceTitle) {
        this.cDeviceTitle = cDeviceTitle;
    }

    public void setcDeviceStatus(boolean cDeviceStatus) {
        this.cDeviceStatus = cDeviceStatus;
    }


    // -------------------- Getters --------------------

    public String getcDeviceTitle() {
        return cDeviceTitle;
    }

    public boolean iscDeviceStatus() {
        return cDeviceStatus;
    }
}
