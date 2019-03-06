package com.example.a390project.Model;

public class Paintbooth extends Machine {

    private float humidity;

    public Paintbooth(String machineTitle, String machineLastEmployee, boolean machineStatus, float temperature, String machineType) {
        super(machineTitle, machineLastEmployee, machineStatus, temperature, machineType);
    }

    public Paintbooth(String machineTitle, String machineLastEmployee, boolean machineStatus, float temperature, String machineType,
                      float humidity) {
        setMachineTitle(machineTitle);
        setMachineLastEmployee(machineLastEmployee);
        setMachineStatus(machineStatus);
        setTemperature(temperature);
        setMachineType(machineType);
        this.humidity = humidity;
    }

    //       -------------------------- Getter / Setter -------------------------------------
    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }
}
