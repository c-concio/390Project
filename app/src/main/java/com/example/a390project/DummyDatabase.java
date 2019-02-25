package com.example.a390project;

import com.example.a390project.Model.Employee;
import com.example.a390project.Model.EmployeeTasks;
import com.example.a390project.Model.Machine;

import java.util.ArrayList;
import java.util.List;

// this class is used to generate dummy variables to input
// to the UI instead of hardcoding them in their activity/fragment


public class DummyDatabase {

    public List<Employee> generateDummyEmployees(int size){
        List<Employee> employee = new ArrayList<>();

        for (int i = 1; i <= size; i++){
            employee.add(new Employee(i, "Employee Name " + i));
        }

        return employee;
    }

    public List<Machine> generateDummyMachines(){
        List<Machine> machines = new ArrayList<>();

        String [] names = {"Abdulrahim", "Antoine ", "Andrew", "Chris", "Kris"};

        //alternate status to view background effect
        boolean status = false;
        for (int i = 0; i < 5; i++) {
            machines.add(new Machine(Machine.generateRandomChars(28),"Machine " + i, names[i],status));
            status = !status;
        }

        return machines;
    }

    protected List<EmployeeTasks> generateTasks(int size){

        List<EmployeeTasks> tasks = new ArrayList<>();
        for(int i = 0; i < size;i++){
            tasks.add(new EmployeeTasks(i, "Task " + i));
        }

        return tasks;
    }


}
