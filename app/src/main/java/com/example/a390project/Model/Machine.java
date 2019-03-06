package com.example.a390project.Model;

import java.util.Random;

public class Machine {

    // -------------------- Data Members (common to both Oven & Paint Booth) --------------------
    private String machineTitle; //<-------- will be used to identify each machine in our database (ideally should of had machineID)
    private String machineLastEmployee;
    private boolean machineStatus;
    private float temperature;
    private String machineType;

    // -------------------- Constructor --------------------
    //used to create a new machine into db
    public Machine(String machineTitle, String machineLastEmployee, boolean machineStatus, float temperature, String machineType) {
        this.machineTitle = machineTitle;
        this.machineLastEmployee = machineLastEmployee;
        this.machineStatus = machineStatus;
        this.temperature = temperature;
        this.machineType = machineType;
    }
    //used in MachineActivity
    public Machine(String machineTitle, boolean machineStatus) {
        this.machineTitle = machineTitle;
        this.machineStatus = machineStatus;
    }

    public Machine() {};

    // ------------------- Methods ----------------------

    public static String generateRandomChars(int length) {
        String candidateChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(candidateChars.charAt(random.nextInt(candidateChars
                    .length())));
        }

        return sb.toString();
    }

    // -------------------- Getters & Setters --------------------

    public String getMachineTitle() {
        return machineTitle;
    }

    public String getMachineLastEmployee() {
        return machineLastEmployee;
    }

    public boolean isMachineStatus() {
        return machineStatus;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setMachineTitle(String machineTitle) {
        this.machineTitle = machineTitle;
    }

    public void setMachineLastEmployee(String machineLastEmployee) {
        this.machineLastEmployee = machineLastEmployee;
    }

    public void setMachineStatus(boolean machineStatus) {
        this.machineStatus = machineStatus;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public String getMachineType() {
        return machineType;
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }
}
