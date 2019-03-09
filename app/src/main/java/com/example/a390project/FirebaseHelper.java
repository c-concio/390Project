package com.example.a390project;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.a390project.ListViewAdapters.EmployeeListViewAdapter;
import com.example.a390project.ListViewAdapters.MachineListViewAdapter;
import com.example.a390project.ListViewAdapters.ProjectListViewAdapter;
import com.example.a390project.Model.Employee;
import com.example.a390project.Model.EmployeeTasks;
import com.example.a390project.Model.Machine;
import com.example.a390project.Model.Oven;
import com.example.a390project.Model.Paintbooth;
import com.example.a390project.Model.Project;
import com.example.a390project.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class FirebaseHelper {

    // ------------------------------------------- FirebaseHelper variables -------------------------------------------
    private static final String TAG = "FirebaseHelper";
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private String uId;

    public FirebaseHelper() {
        rootRef = FirebaseDatabase.getInstance().getReference();
        uId = FirebaseAuth.getInstance().getUid();
    }

    // ------------------------------------------- Machine variables -------------------------------------------
    private List<Machine> machines;

    // ------------------------------------------- Employee variables -------------------------------------------
    private List<Employee> employees;
    private String currentUserId;
    private View employeeFragmentView;
    private DatabaseReference dbRefEmployees;
    // childEventListener to get Employees
    private ChildEventListener childEventListener;

    // ------------------------------------------- Project variables -------------------------------------------

    private List<Project> projects;

    /*
   ------------------------------------------FirebaseHelper User creation functions-----------------------------------
    */
    public void addUser(String name, String email, boolean isManager, FirebaseUser currentUser) {
        Map<String, String> t = ServerValue.TIMESTAMP;
        uId = currentUser.getUid();
        rootRef.child("users").child(uId).setValue(new User(name, email, t, isManager));
    }

    /*
   ------------------------------------------FirebaseHelper Machine functions-----------------------------------
    */

    public void createMachine(Machine machine){
        rootRef.child("machines").child(machine.getMachineTitle()).setValue(machine);
    }


    public void populateMachine(final View view, final Activity activity) {
        rootRef.child("machines").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                machines = new ArrayList<Machine>();
                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                    String machineTitle = ds.child("machineTitle").getValue(String.class);
                    String machineLastEmployee =  ds.child("machineLastEmployee").getValue(String.class);
                    String machineType = ds.child("machineType").getValue(String.class);
                    boolean machineStatus = ds.child("machineStatus").getValue(boolean.class);
                    float temperature = ds.child("temperature").getValue(float.class);

                    if (machineType.equals("Oven")) {
                        long machineStatusTimeOff = ds.child("machineStatusTimeOff").getValue(long.class);
                        long machineStatusTimeOn = ds.child("machineStatusTimeOn").getValue(long.class);
                        machines.add(new Oven(machineTitle, machineLastEmployee, machineStatus, temperature, machineType,
                                machineStatusTimeOff, machineStatusTimeOn));
                    }
                    else if (machineType.equals("PaintBooth")) {
                        float humidity = ds.child("humidity").getValue(float.class);
                        machines.add(new Paintbooth(machineTitle, machineLastEmployee, machineStatus, temperature, machineType,
                                humidity));
                    }
                }
                callMachineListViewAdapter(view,activity);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void callMachineListViewAdapter(View view, Activity activity) {
        // instantiate the custom list adapter
        MachineListViewAdapter adapter = new MachineListViewAdapter(activity, machines);

        // get the ListView and attach the adapter
        ListView itemsListView  = (ListView) view.findViewById(R.id.machine_list_view);
        itemsListView.setAdapter(adapter);
    }

    // --------------------------------------- Firebase employees functions ------------------------------------------

    public void getEmployees(View view){
        employees = new ArrayList<>();
        currentUserId = FirebaseAuth.getInstance().getUid();
        employeeFragmentView = view;

        dbRefEmployees = FirebaseDatabase.getInstance().getReference("users");

        childEventListener = new ChildEventListener(){

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                getEmployees(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildChanged: ");
                updateEmployees(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved: ");
                updateEmployees(dataSnapshot);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildMoved: ");
                updateEmployees(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };
        dbRefEmployees.addChildEventListener(childEventListener);
    }

    private void getEmployees(DataSnapshot dataSnapshot){

        Log.d(TAG, "updateUsers: dataSnapshot return: " + dataSnapshot.getValue());
        Employee newEmployee = dataSnapshot.getValue(Employee.class);
        if (newEmployee != null) {
            newEmployee.setAccountID(dataSnapshot.getKey());
            Log.d(TAG, "updateUsers: account id: " + newEmployee.getAccountID());
        }

        // if the user being examined is the current user, Employee is not added to the listView
        if (!newEmployee.getAccountID().equals(currentUserId)){
            employees.add(newEmployee);
        }

        // update listView once a user has changed or added
        listViewAdapter();
    }

    private void updateEmployees(DataSnapshot dataSnapshot){
        String employeeId = dataSnapshot.getKey();
        if(!employeeId.equals(currentUserId)) {
            Log.d(TAG, "updateUsers: employeeId = " + employeeId);
            for (int i = 0; i < employees.size(); i++) {
                Log.d(TAG, "updateUsers: employee name =" + employees.get(i).getAccountID());
                if (employeeId.equals(employees.get(i).getAccountID())) {
                    employees.set(i, dataSnapshot.getValue(Employee.class));
                    employees.get(i).setAccountID(employeeId);
                    Log.d(TAG, "updateUsers: updated name: " + employees.get(i).getName());
                }
            }
            listViewAdapter();
        }
    }

    private void listViewAdapter(){
        //Log.d(TAG, "updateUsers: updated name: " + employees.get(3));

        Log.d(TAG, "listViewAdapter: employees size = " + employees.size());
        Log.d(TAG, "listViewAdapter: --------------------------------------");
        for(Employee currentEmployee : employees){
            Log.d(TAG, "listViewAdapter: employee name = " + currentEmployee.getName());
        }
        Log.d(TAG, "listViewAdapter: --------------------------------------");

        ListView employeeListView = employeeFragmentView.findViewById(R.id.employeesListView);
        EmployeeListViewAdapter employeeAdapter = new EmployeeListViewAdapter(employeeFragmentView.getContext(), employees);
        employeeListView.setAdapter(employeeAdapter);

        // setup what happens when a list view item is clicked
        employeeListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // go to the employee's activity
                Intent intent = new Intent(view.getContext(), EmployeeActivity.class);
                intent.putExtra("employeeID", employees.get(position).getAccountID());
                employeeFragmentView.getContext().startActivity(intent);
            }
        });
    }

    public void detatchEmployeeChildEventListener(){
        dbRefEmployees.removeEventListener(childEventListener);
    }

    /*
    ------------------------------ Firebase Project Methods --------------------------------------------------------
     */

    public void createProject(String po, String title, String client, long startDate, long dueDate) {
        rootRef.child("projects").child(po).setValue(new Project(po, title, client, startDate, dueDate));
    }

    public void populateProjects(final View view, final Activity activity, final ProgressBar mProgressbar) {
        rootRef.child("projects").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                projects = new ArrayList<>();
                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                    String po = ds.getKey();
                    String title = ds.child("title").getValue(String.class);
                    String client = ds.child("client").getValue(String.class);
                    long startDate = ds.child("startDate").getValue(long.class);
                    long dueDate = ds.child("dueDate").getValue(long.class);
                    projects.add(new Project(po, title, client, startDate, dueDate));
                }
                callProjectListViewAdapter(view, activity);
                mProgressbar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void callProjectListViewAdapter(View view, Activity activity){
        ProjectListViewAdapter adapter = new ProjectListViewAdapter(activity, projects);
        ListView itemsListView  = view.findViewById(R.id.project_list_view);
        itemsListView.setAdapter(adapter);
    }
}