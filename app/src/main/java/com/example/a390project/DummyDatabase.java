package com.example.a390project;

import com.example.a390project.Model.Employee;
import com.example.a390project.Model.Machine;

import java.util.ArrayList;
import java.util.List;

public class DummyDatabase {

    public List<Employee> generateDummyEmployees(int size){
        List<Employee> employee = new ArrayList<>();

        for (int i = 1; i <= size; i++){
            employee.add(new Employee(i, "Employee Name " + i));
        }

        return employee;
    }

    public List<Machine> generateDummyMachines(){
        List<Machine> machines = new ArrayList();

        String [] names = {"Abdulrahim", "Antoine ", "Andrew", "Chris", "Kris"};

        //alternate status to view background effect
        boolean status = false;
        for (int i = 0; i < 5; i++) {
            machines.add(new Machine(Machine.generateRandomChars(28),"Machine " + i, names[i],status));
            status = !status;
        }

        return machines;
    }



}
