package com.example.a390project;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.a390project.ListViewAdapters.ControlDeviceListViewAdapter;
import com.example.a390project.ListViewAdapters.EmployeeListViewAdapter;
import com.example.a390project.ListViewAdapters.EmployeeTasksListViewAdapter;
import com.example.a390project.ListViewAdapters.InventoryListViewAdapter;
import com.example.a390project.ListViewAdapters.MachineListViewAdapter;
import com.example.a390project.ListViewAdapters.PrepaintTaskListViewAdapter;
import com.example.a390project.ListViewAdapters.ProjectListViewAdapter;
import com.example.a390project.ListViewAdapters.TaskListViewAdapter;
import com.example.a390project.Model.ControlDevice;
import com.example.a390project.Model.Employee;
import com.example.a390project.Model.Machine;
import com.example.a390project.Model.Oven;
import com.example.a390project.Model.PaintBucket;
import com.example.a390project.Model.Paintbooth;
import com.example.a390project.Model.Project;
import com.example.a390project.Model.SubTask;
import com.example.a390project.Model.Task;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.lang.Integer.parseInt;

public class FirebaseHelper {

    // ------------------------------------------- FirebaseHelper variables -------------------------------------------
    private static final String TAG = "FirebaseHelper";
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private String uId;
    private boolean check;
    private boolean setStatusOfSwitchControlDevice = true;

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

    // ------------------------------------------- Task variables -------------------------------------------
    private List<String> taskIDs;
    private List<Task> tasks;
    private ValueEventListener packagingValueEventListener;
    private ValueEventListener inspectionValueEventListener;


    // ------------------------------------------- Control Device variables -------------------------------------------

    private List<ControlDevice> cDevices;

    // ------------------------------------------- PrepaintTask -------------------------------------------

    // -------------------------------------------------------------------------------------------------------------


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
                    float temperature = ds.child("Temperature").getValue(float.class);

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

        Employee newEmployee = dataSnapshot.getValue(Employee.class);

        if (newEmployee != null) {
            newEmployee.setAccountID(dataSnapshot.getKey());
            Log.d(TAG, "updateUsers: account id: " + newEmployee.getAccountID());
        }

        // if the user being examined is the current user, Employee is not added to the listView
        //if (!newEmployee.getAccountID().equals(currentUserId)){
            employees.add(newEmployee);
        //}

        // update listView once a user has changed or added
        callEmployeeListViewAdapter();
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
            callEmployeeListViewAdapter();
        }
    }

    private void callEmployeeListViewAdapter(){

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
                intent.putExtra("employeeName", employees.get(position).getName());
                employeeFragmentView.getContext().startActivity(intent);
            }
        });
    }

    public void detatchEmployeeChildEventListener(){
        dbRefEmployees.removeEventListener(childEventListener);
    }

    void setWorkingTasksValueListener(String userID, final Activity activity){


        rootRef.child("users").child(userID).child("workingTasks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final List<String> assignedTasksIDs = new ArrayList<>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    assignedTasksIDs.add(postSnapshot.getKey());
                }

                rootRef.child("tasks").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Task> assignedTasks = new ArrayList<>();
                        for (String taskID : assignedTasksIDs) {
                            if (dataSnapshot.hasChild(taskID)) {
                                Task newTask = new Task();
                                newTask.setTaskID(taskID);
                                newTask.setProjectPO(dataSnapshot.child(taskID).child("projectPO").getValue(String.class));
                                newTask.setTaskType(dataSnapshot.child(taskID).child("taskType").getValue(String.class));
                                assignedTasks.add(newTask);
                                Log.d(TAG, "onDataChange: Project PO" + newTask.getProjectPO());
                            }
                        }
                        if (assignedTasks.size() < 1){
                            TextView noWorkingTasksTextView = activity.findViewById(R.id.noWorkingTasksTextView);
                            noWorkingTasksTextView.setVisibility(View.VISIBLE);
                        }
                        else {
                            TextView noWorkingTasksTextView = activity.findViewById(R.id.noWorkingTasksTextView);
                            noWorkingTasksTextView.setVisibility(View.GONE);
                            callWorkingTasksListViewAdapter(activity, assignedTasks);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void callWorkingTasksListViewAdapter(Activity activity, List<Task> assignedTasks){

        EmployeeTasksListViewAdapter adapter = new EmployeeTasksListViewAdapter(activity, assignedTasks);
        ListView assignedTasksListView = activity.findViewById(R.id.workingTasksListView);
        assignedTasksListView.setAdapter(adapter);
        EmployeeActivity.ListUtils.setDynamicHeight(assignedTasksListView);

    }

    void setCompletedTasksValueEventListener(String userID, final Activity activity){


        rootRef.child("users").child(userID).child("completedTasks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final List<String> completedTasksIDs = new ArrayList<>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    completedTasksIDs.add(postSnapshot.getKey());
                }

                rootRef.child("tasks").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Task> completedTasks = new ArrayList<>();
                        for (String taskID : completedTasksIDs) {
                            if (dataSnapshot.hasChild(taskID)) {
                                Task newTask = new Task();
                                newTask.setTaskID(taskID);
                                newTask.setProjectPO(dataSnapshot.child(taskID).child("projectPO").getValue(String.class));
                                newTask.setTaskType(dataSnapshot.child(taskID).child("taskType").getValue(String.class));
                                completedTasks.add(newTask);
                                Log.d(TAG, "onDataChange: completed task: " + newTask.getProjectPO());
                                Log.d(TAG, "onDataChange: " + newTask.getTaskID());
                            }
                        }
                        if (completedTasks.size() < 1){
                            TextView noCompletedTasksTextView = activity.findViewById(R.id.noCompletedTasksTextView);
                            noCompletedTasksTextView.setVisibility(View.VISIBLE);
                        }
                        else {
                            TextView noCompletedTasksTextView = activity.findViewById(R.id.noCompletedTasksTextView);
                            noCompletedTasksTextView.setVisibility(View.GONE);
                            callCompletedTasksListViewAdapter(activity, completedTasks);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void callCompletedTasksListViewAdapter(Activity activity, List<Task> completedTasks){

        EmployeeTasksListViewAdapter adapter = new EmployeeTasksListViewAdapter(activity, completedTasks);
        ListView completedTasksListView = activity.findViewById(R.id.completedTasksListView);
        completedTasksListView.setAdapter(adapter);
        EmployeeActivity.ListUtils.setDynamicHeight(completedTasksListView);

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

    /*
    ------------------------------ Firebase Project Methods --------------------------------------------------------
     */

    public void createTask(String taskType, String taskDescription, String projectPO) {
        Calendar cal = Calendar.getInstance();
        long timeCreated = cal.getTimeInMillis();
        String taskID = Task.generateRandomChars();


        //add task in 'tasks'
        rootRef.child("tasks").child(taskID).setValue(new Task(taskID, projectPO,taskType,taskDescription,timeCreated));

        //add task in 'projects'>'tasks'
        rootRef.child("projects").child(projectPO).child("tasks").child(taskID).setValue(true);

    }

    public void populateTasks(String projectPO, final Activity activity) {
        rootRef.child("projects").child(projectPO).child("tasks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                taskIDs = new ArrayList<String>();
                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                    taskIDs.add(ds.getKey());
                }
                rootRef.child("tasks").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //populate all the tasks from 'rootRef'>'tasks'
                        tasks = new ArrayList<Task>();
                        for (String taskID:taskIDs) {
                            if (dataSnapshot.hasChild(taskID)) {
                                String projectPO = dataSnapshot.child(taskID).child("projectPO").getValue(String.class);
                                String taskType = dataSnapshot.child(taskID).child("taskType").getValue(String.class);
                                String description = dataSnapshot.child(taskID).child("description").getValue(String.class);
                                long createdTime = dataSnapshot.child(taskID).child("createdTime").getValue(long.class);
                                tasks.add(new Task(taskID, projectPO, taskType, description, createdTime));
                            }
                        }
                        callTaskListViewAdapter(activity, tasks);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void callTaskListViewAdapter(Activity activity, List<Task> tasks) {
        TaskListViewAdapter adapter = new TaskListViewAdapter(activity,tasks);
        ListView itemsListView  = activity.findViewById(R.id.task_list_view);
        itemsListView.setAdapter(adapter);
    }

    // --------------------------------------- Packaging Task Methods ---------------------------------------

    void setStartTime(String taskId){
        rootRef.child("tasks").child(taskId).child("startTime").setValue(ServerValue.TIMESTAMP);
    }

    void setEndTime(String taskId){
        rootRef.child("tasks").child(taskId).child("endTime").setValue(ServerValue.TIMESTAMP);
    }

    public void goToTaskPackagingActivity(String packagingTaskID, final Context context){

        rootRef.child("tasks").child(packagingTaskID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Task newTask = new Task();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void setTaskPackagingActivityListener(String taskID, final Activity activity){
        rootRef.child("tasks").child(taskID).addValueEventListener(packagingValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Task currentTask = dataSnapshot.getValue(Task.class);

                EditText descriptionEditText = activity.findViewById(R.id.descriptionEditText);
                EditText dateEditText = activity.findViewById(R.id.dateEditText);
                EditText employeeCommentEditText = activity.findViewById(R.id.employeeCommentEditText);

                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                String date = formatter.format(currentTask.getDate());

                descriptionEditText.setText(currentTask.getDescription());
                dateEditText.setText(String.valueOf(date));

                employeeCommentEditText.setText(currentTask.getEmployeeComment());

                // get the material used
                LinearLayout materialUsedLinearLayout = activity.findViewById(R.id.materialUsedLinearLayout);
                materialUsedLinearLayout.removeAllViews();
                if (!dataSnapshot.child("materialUsed").hasChildren()) {
                    TextView text = new TextView(activity);
                    text.setText("No material chosen yet");
                    materialUsedLinearLayout.addView(text);
                }
                else {
                    for (DataSnapshot postSnapshot : dataSnapshot.child("materialUsed").getChildren()) {
                        TextView text = new TextView(activity);
                        text.setText(postSnapshot.getValue(String.class));
                        materialUsedLinearLayout.addView(text);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void detachTaskPackagingActivityListener(String taskID){
        rootRef.child("tasks").child(taskID).removeEventListener(packagingValueEventListener);
    }

    //------------------------------ Inspection Task Methods --------------------------------------------------------

    public void setTaskInspectionActivityListener(String taskID, final Activity activity){
        rootRef.child("tasks").child(taskID).addValueEventListener(inspectionValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Task currentTask = dataSnapshot.getValue(Task.class);

                EditText partCountedEditText = activity.findViewById(R.id.partCountedEditText);
                EditText partAcceptedEditText = activity.findViewById(R.id.partAcceptedEditText);
                EditText partRejectedEditText = activity.findViewById(R.id.partRejectedEditText);


                partCountedEditText.setText(Integer.toString(currentTask.getPartCounted()));
                partAcceptedEditText.setText(Integer.toString(currentTask.getPartAccepted()));
                partRejectedEditText.setText(Integer.toString(currentTask.getPartRejected()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void detachTaskInspectionActivityListener(String taskID){
        rootRef.child("tasks").child(taskID).removeEventListener(inspectionValueEventListener);
    }

    //Inspection Controllers
    public void changeInspectionCounted(String taskID, int partCounted){
        rootRef.child("tasks").child(taskID).child("partCounted").setValue(partCounted);
    }

    public void changeInspectionAccepted(String taskID, int partCounted){
        rootRef.child("tasks").child(taskID).child("partAccepted").setValue(partCounted);
    }

    public void changeInspectionRejected(String taskID, int partCounted){
        rootRef.child("tasks").child(taskID).child("partRejected").setValue(partCounted);
    }
    //end of Inspection Controllers

    //------------------------------ Firebase Control Device Methods --------------------------------------------------------

    public void createControlDevice(ControlDevice cDevice){
        rootRef.child("cDevices").child(cDevice.getcDeviceTitle()).setValue(cDevice);
    }

    public void populateControlDevices(final View view, final Activity activity){
        rootRef.child("cDevices").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!check) {
                cDevices = new ArrayList<ControlDevice>();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String cDeviceTitle = ds.child("cDeviceTitle").getValue(String.class);
                    boolean cDeviceStatus = ds.child("cDeviceStatus").getValue(boolean.class);
                    cDevices.add(new ControlDevice(cDeviceTitle, cDeviceStatus));
                }
                callControlDeviceListViewAdapter(view, activity);
                check = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void callControlDeviceListViewAdapter(View view, Activity activity){
        ControlDeviceListViewAdapter adapter = new ControlDeviceListViewAdapter(activity, cDevices);
        ListView itemsListView = (ListView) view.findViewById(R.id.control_device_list_view);
        itemsListView.setAdapter(adapter);
    }

    public void setStatusOfSwitch(String cDeviceTitle, final Switch switchControlDevice) {
        rootRef.child("cDevices").child(cDeviceTitle).child("cDeviceStatus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(setStatusOfSwitchControlDevice) {
                    switchControlDevice.setChecked(dataSnapshot.getValue(boolean.class));
                    setStatusOfSwitchControlDevice = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void changeDeviceStatus(String cDeviceTitle, boolean state) {
        rootRef.child("cDevices").child(cDeviceTitle).child("cDeviceStatus").setValue(state);
        Log.d(TAG, cDeviceTitle + " IS NOW " + state);
    }


    // -------------------------------------------- Firebase Prepaint Task Methods --------------------------------------------
    public void populateSubTasks(String taskId, final Activity activity){
        final List<String> subTasksID = new ArrayList<>();
        final List<SubTask> subTasks = new ArrayList<>();
        rootRef.child("tasks").child(taskId).child("subTasks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    subTasksID.add(ds.getKey());
                }
                rootRef.child("subTasks").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (String subTaskID:subTasksID) {
                            boolean isCompleted = dataSnapshot.child(subTaskID).child("completed").getValue(boolean.class);
                            if (!isCompleted) {
                                String subTaskType = dataSnapshot.child(subTaskID).child("subTaskType").getValue(String.class);
                                String ID = dataSnapshot.child(subTaskID).child("subTaskID").getValue(String.class);
                                String projectID = dataSnapshot.child(subTaskID).child("projectID").getValue(String.class);
                                String taskID = dataSnapshot.child(subTaskID).child("taskID").getValue(String.class);
                                long createdTime = dataSnapshot.child(subTaskID).child("createdTime").getValue(long.class);
                                subTasks.add(new SubTask(subTaskType,subTaskID,projectID,taskID,isCompleted,createdTime));
                            }
                        }
                        callPrepaintTaskListViewAdapter(activity, subTasks);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void callPrepaintTaskListViewAdapter(Activity activity, List<SubTask> subTasks){
        PrepaintTaskListViewAdapter adapter = new PrepaintTaskListViewAdapter(activity, subTasks);
        ListView prepaintTasksListView = activity.findViewById(R.id.prepaintTaskListView);
        prepaintTasksListView.setAdapter(adapter);
    }

    // ------------------------------------------------ Firebase Baking Methods ------------------------------------------------

    public void setBakingValueEventListener(String taskID, final Activity activity){
        rootRef.child("tasks").child(taskID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Task views
                EditText descriptionEditText;
                EditText employeeCommentEditText;
                TextView paintCodeTextView;
                Button startTimeButton;
                Button endTimeButton;
                Button bakingCompletedButton;

                descriptionEditText = activity.findViewById(R.id.descriptionEditText);
                employeeCommentEditText = activity.findViewById(R.id.employeeCommentEditText);
                paintCodeTextView = activity.findViewById(R.id.paintCodeTextView);
                startTimeButton = activity.findViewById(R.id.startTimeButton);
                endTimeButton = activity.findViewById(R.id.endTimeButton);
                bakingCompletedButton = activity.findViewById(R.id.bakingCompletedButton);

                Task newTask = dataSnapshot.getValue(Task.class);

                // Task set text
                if (newTask.getDescription() != null)
                    descriptionEditText.setText(newTask.getDescription());
                if (newTask.getEmployeeComment() != null)
                    employeeCommentEditText.setText(newTask.getEmployeeComment());

                final String paintCode = newTask.getPaintCode();
                final String paintType = newTask.getPaintType();

                paintCodeTextView.setText(paintCode);

                //Should modify path to get paintCode(achieved through painting task: tasks > taskID > paintCode & paintType,
                // THEN: inventory > paintingType > paintingCode > all data needed)

                // create a listener for the paint inventory to get the paintBucket info

                rootRef.child("inventory").child(paintType).child(paintCode).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Inventory views
                        TextView paintDescriptionTextVIew;
                        TextView bakingTimeTextView;
                        TextView bakingTempTextView;

                        paintDescriptionTextVIew = activity.findViewById(R.id.paintDescriptionTextView);
                        bakingTimeTextView = activity.findViewById(R.id.bakingTimeTextView);
                        bakingTempTextView = activity.findViewById(R.id.bakingTempTextView);

                        PaintBucket newPaintBucket = dataSnapshot.getValue(PaintBucket.class);

                        paintDescriptionTextVIew.setText(newPaintBucket.getPaintDescription());
                        bakingTimeTextView.setText(String.valueOf(newPaintBucket.getBakeTime()));
                        bakingTempTextView.setText(String.valueOf(newPaintBucket.getBakeTemperature()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setPaintingValues(final TextView mPaintCode, final TextView mBakeTemp, final TextView mBakeTime, final TextView mDescription, final TextView mPaintDescription, String paintingTaskID) {
        rootRef.child("tasks").child(paintingTaskID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final String paintType = dataSnapshot.child("paintType").getValue(String.class);
                final String paintCode = dataSnapshot.child("paintCode").getValue(String.class);
                final String description = dataSnapshot.child("description").getValue(String.class);

                rootRef.child("inventory").child(paintType).child(paintCode).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int bakeTemperature = dataSnapshot.child("bakeTemperature").getValue(int.class);
                        int bakingTime = dataSnapshot.child("bakeTime").getValue(int.class);
                        String paintDescription = dataSnapshot.child("paintDescription").getValue(String.class);

                        mPaintCode.setText(paintCode);
                        mPaintDescription.setText(paintDescription);
                        mBakeTemp.setText(Integer.toString(bakeTemperature));
                        mBakeTime.setText(Integer.toString(bakingTime));
                        mDescription.setText(description);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*
       ------------------------------------------------ Firebase PrePaintingTask Methods ------------------------------------------------
     */

    public void createPrepaintingTask(String taskID, String projectPO, String prePainting, String taskDescription, long createdTime, List<SubTask> subTasks) {
        rootRef.child("tasks").child(taskID).setValue(new Task(taskID, projectPO, prePainting, taskDescription, createdTime));
        for (SubTask subTask:subTasks) {
            rootRef.child("subTasks").child(subTask.getSubTaskID()).setValue(subTask);
            rootRef.child("tasks").child(taskID).child("subTasks").child(subTask.getSubTaskID()).setValue(true);
            rootRef.child("projects").child(projectPO).child("tasks").child(taskID).child("subTasks").child(subTask.getSubTaskID()).setValue(true);
        }
    }

    /*
      ------------------------------------------------ Firebase PaintingTaskDialogFragment Methods ------------------------------------------------
     */

    public void createPaintingTask(String projectPO, String taskDescription, String paintCode, String paintType, Dialog dialog) {
        String taskID = Task.generateRandomChars();
        long createdTime = System.currentTimeMillis();

        //add task in 'tasks'
        rootRef.child("tasks").child(taskID).setValue(new Task(taskID, projectPO,"Painting",taskDescription,createdTime));
        rootRef.child("tasks").child(taskID).child("paintCode").setValue(paintCode);
        rootRef.child("tasks").child(taskID).child("paintType").setValue(paintType);

        //add task in 'projects'>'tasks'
        rootRef.child("projects").child(projectPO).child("tasks").child(taskID).setValue(true);
        dialog.dismiss();
    }

    public void populatePaintCodesANDCreatePaintingTask(final String paintType, final Spinner mPaintCode, final Context context,
                                                        final TextView mPaintDescription, final TextView mPaintBakeTemperature,
                                                        final TextView mPaintBakeTime, final TextView mPaintWeight, final FloatingActionButton mFab,
                                                        final String projectPO, final String taskDescription, final Dialog dialog) {
        rootRef.child("inventory").child(paintType).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> paintCodes = new ArrayList<>();
                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    paintCodes.add(ds.getKey());
                    Log.d(TAG, "Paint code: "+ds.getKey());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_dropdown_item,paintCodes);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mPaintCode.setAdapter(adapter);


                mPaintCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        final String paintCode = adapterView.getItemAtPosition(i).toString().trim();
                        //populate fields of painting characteristics
                        rootRef.child("inventory").child(paintType).child(paintCode).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String paintDescription = dataSnapshot.child("paintDescription").getValue(String.class);
                                String bakeTemperature = Integer.toString(dataSnapshot.child("bakeTemperature").getValue(int.class));
                                String bakeTime = Integer.toString(dataSnapshot.child("bakeTime").getValue(int.class));
                                String paintWeight = Float.toString(dataSnapshot.child("paintWeight").getValue(float.class));

                                mPaintDescription.setText(paintDescription);
                                mPaintBakeTemperature.setText(bakeTemperature);
                                mPaintBakeTime.setText(bakeTime);
                                mPaintWeight.setText(paintWeight);

                                mFab.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (!paintCode.isEmpty() && !paintType.isEmpty()) {
                                            FirebaseHelper firebaseHelper = new FirebaseHelper();
                                            firebaseHelper.createPaintingTask(projectPO, taskDescription, paintCode, paintType, dialog);
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*
      ------------------------------------------------ Firebase Inventory Fragment Methods ------------------------------------------------
     */

    public void populateInventory(final View view, final Activity activity) {
        rootRef.child("inventory").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<PaintBucket> paintBuckets = new ArrayList<>();

                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    String paintType = ds.getKey();

                    for(DataSnapshot ds2:ds.getChildren()) {
                        String paintCode = ds2.getKey();
                        String paintDescription = ds2.child("paintDescription").getValue(String.class);
                        int bakeTemperature = ds2.child("bakeTemperature").getValue(int.class);
                        int bakeTime = ds2.child("bakeTime").getValue(int.class);
                        float paintWeight = ds2.child("paintWeight").getValue(float.class);

                        paintBuckets.add(new PaintBucket(paintType, paintCode, paintDescription, bakeTemperature, bakeTime, paintWeight));
                    }
                }
                callInventoryListViewAdapter(view,activity,paintBuckets);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void callInventoryListViewAdapter(View view, Activity activity, List<PaintBucket> paintBuckets) {
        // instantiate the custom list adapter
        InventoryListViewAdapter adapter = new InventoryListViewAdapter(activity, paintBuckets);

        // get the ListView and attach the adapter
        ListView itemsListView  = (ListView) view.findViewById(R.id.inventory_list_view);
        itemsListView.setAdapter(adapter);
    }

    public void createInventoryItem(String paintType, String paintCode, String paintDescription, int paintBakeTemperature, int paintBakeTime, float paintWeight) {
        rootRef.child("inventory").child(paintType).child(paintCode).setValue(new PaintBucket(paintType,paintCode,paintDescription,paintBakeTemperature,paintBakeTime,paintWeight));
    }

}