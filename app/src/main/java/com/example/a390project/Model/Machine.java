package com.example.a390project.Model;

import java.util.Random;

public class Machine {

    // -------------------- Data Members --------------------
    private String machineID; // <-------- will be used to identify each machine in our database
    private String machineTitle;
    private String machineLastEmployee;
    private boolean machineStatus;

    // -------------------- Constructor --------------------
    public Machine(String machineID, String machineTitle, String machineLastEmployee, boolean machineStatus) {
        this.machineID = machineID;
        this.machineTitle = machineTitle;
        this.machineLastEmployee = machineLastEmployee;
        this.machineStatus = machineStatus;
    }

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

    public void setMachineID(String machineID) {
        this.machineID = machineID;
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

    public String getMachineID() {
        return machineID;
    }
}
