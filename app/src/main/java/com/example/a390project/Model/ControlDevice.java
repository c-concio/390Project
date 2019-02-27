package com.example.a390project.Model;

public class ControlDevice {

    private int cDeviceID;
    private String cDeviceTitle;
    private boolean cDeviceStatus;

    public ControlDevice(int cDeviceID, String cDeviceTitle, boolean cDeviceStatus){
        this.cDeviceID = cDeviceID;
        this.cDeviceTitle = cDeviceTitle;
        this.cDeviceStatus = cDeviceStatus;
    }

    // -------------------- Setters --------------------

    public void setcDeviceID(int cDeviceID) {
        this.cDeviceID = cDeviceID;
    }

    public void setcDeviceTitle(String cDeviceTitle) {
        this.cDeviceTitle = cDeviceTitle;
    }

    public void setcDeviceStatus(boolean cDeviceStatus) {
        this.cDeviceStatus = cDeviceStatus;
    }


    // -------------------- Getters --------------------

    public int getcDeviceID() {
        return cDeviceID;
    }

    public String getcDeviceTitle() {
        return cDeviceTitle;
    }

    public boolean iscDeviceStatus() {
        return cDeviceStatus;
    }
}
