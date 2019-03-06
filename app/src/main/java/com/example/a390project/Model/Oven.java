package com.example.a390project.Model;

import java.util.List;

public class Oven extends Machine {

    private long machineStatusTimeOff;
    private long machineStatusTimeOn;

    public Oven(String machineTitle, String machineLastEmployee, boolean machineStatus, float temperature, String machineType) {
        super(machineTitle, machineLastEmployee, machineStatus, temperature, machineType);
    }

    public Oven(String machineTitle, String machineLastEmployee, boolean machineStatus, float temperature, String machineType,
                long machineStatusTimeOff, long machineStatusTimeOn) {
        setMachineTitle(machineTitle);
        setMachineLastEmployee(machineLastEmployee);
        setMachineStatus(machineStatus);
        setTemperature(temperature);
        setMachineType(machineType);
        this.machineStatusTimeOff = machineStatusTimeOff;
        this.machineStatusTimeOn = machineStatusTimeOn;
    }


    //       -------------------------- Getter / Setter -------------------------------------

    public long getMachineStatusTimeOff() {
        return machineStatusTimeOff;
    }

    public void setMachineStatusTimeOff(long machineStatusTimeOff) {
        this.machineStatusTimeOff = machineStatusTimeOff;
    }

    public long getMachineStatusTimeOn() {
        return machineStatusTimeOn;
    }

    public void setMachineStatusTimeOn(long machineStatusTimeOn) {
        this.machineStatusTimeOn = machineStatusTimeOn;
    }



}
