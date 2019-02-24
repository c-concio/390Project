package com.example.a390project.Views;

public class Machine {

    //data members
    private String machineName;
    private String machineLastEmployee;
    private boolean machineStatus;

    //constructors
    public Machine(String machineName, String machineLastEmployee, boolean machineStatus) {
        this.machineName = machineName;
        this.machineLastEmployee = machineLastEmployee;
        this.machineStatus = machineStatus;
    }

    //class methods

    //getters & setters
    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getMachineLastEmployee() {
        return machineLastEmployee;
    }

    public void setMachineLastEmployee(String machineLastEmployee) {
        this.machineLastEmployee = machineLastEmployee;
    }

    public boolean isMachineStatus() {
        return machineStatus;
    }

    public void setMachineStatus(boolean machineStatus) {
        this.machineStatus = machineStatus;
    }

}
