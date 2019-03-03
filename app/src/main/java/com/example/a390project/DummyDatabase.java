package com.example.a390project;

import com.example.a390project.Model.ControlDevice;
import com.example.a390project.Model.Employee;
import com.example.a390project.Model.EmployeeTasks;
import com.example.a390project.Model.Machine;
import com.example.a390project.Model.Project;

import java.util.ArrayList;
import java.util.List;

// this class is used to generate dummy variables to input
// to the UI instead of hardcoding them in their activity/fragment


public class DummyDatabase {

    private List<Employee> employee = new ArrayList<>();
    private int employeeID = 0;

    private List<ControlDevice> cDevice = new ArrayList<>();
    private int cDeviceID = 0;

    private int Projectid;

    public List<Employee> generateDummyEmployees(int size){
        //employee.add(new Employee(0, "ant", "Test123"));
        for (int i = 1; i <= size; i++){
            employeeID++;
            employee.add(new Employee(employeeID, "Employee Name " + employeeID, "Test123"));
        }

        return employee;
    }

    public void addEmployee(String username, String password){
        employeeID++;
        employee.add(new Employee(employeeID, username, password));
    }
    public List<Project> generateDummyProjects(int size){
        List<Project> projects = new ArrayList<>();
        for (int i = 1; i <= size; i++){
            Projectid++;
            projects.add(new Project(Integer.toString(Projectid), "Project ", "Client ", "PO ", "due date"));
        }

        return projects;
    }

    public List<Employee> getEmployees(){
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

    public List<ControlDevice> generateDummyControlDevice(){


        for(int i=0; i<2; i++){
            cDeviceID++;
            cDevice.add(new ControlDevice(cDeviceID, "Switch " + i, false));

        }
        return cDevice;
    }

    public void addControlDevice(String name){
        cDeviceID++;
        cDevice.add(new ControlDevice(cDeviceID, name, false));
    }




}
