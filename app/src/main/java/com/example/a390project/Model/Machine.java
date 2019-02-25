package com.example.a390project.Model;

public class Machine {

    // -------------------- Data Members --------------------
    private String machineTitle;
    private String machineLastEmployee;
    private boolean machineStatus;

    // -------------------- Constructor --------------------
    public Machine(String machineName, String machineLastEmployee, boolean machineStatus) {
        this.machineTitle = machineName;
        this.machineLastEmployee = machineLastEmployee;
        this.machineStatus = machineStatus;
    }

    // -------------------- Setters --------------------

    public void setMachineTitle(String machineTitle) {
        this.machineTitle = machineTitle;
    }

    public void setMachineLastEmployee(String machineLastEmployee) {
        this.machineLastEmployee = machineLastEmployee;
    }

    public void setMachineStatus(boolean machineStatus) {
        this.machineStatus = machineStatus;
    }

    // -------------------- Getters --------------------

    public String getMachineTitle() {
        return machineTitle;
    }

    public String getMachineLastEmployee() {
        return machineLastEmployee;
    }

    public boolean isMachineStatus() {
        return machineStatus;
    }



}
