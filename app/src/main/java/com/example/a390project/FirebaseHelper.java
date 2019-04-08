package com.example.a390project;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a390project.ForegroundServices.NotificationForegroundService;
import com.example.a390project.ListViewAdapters.ControlDeviceListViewAdapter;
import com.example.a390project.ListViewAdapters.EmployeeCommentListViewAdapter;
import com.example.a390project.ListViewAdapters.EmployeeListViewAdapter;
import com.example.a390project.ListViewAdapters.EmployeeTasksListViewAdapter;
import com.example.a390project.ListViewAdapters.EmployeeWorkBlocksListViewAdapter;
import com.example.a390project.ListViewAdapters.GraphableProjectsListViewAdapter;
import com.example.a390project.ListViewAdapters.GraphsListViewAdapter;
import com.example.a390project.ListViewAdapters.InventoryMaterialListViewAdapter;
import com.example.a390project.ListViewAdapters.MachineListViewAdapter;
import com.example.a390project.ListViewAdapters.MaterialRecyclerAdapter;
import com.example.a390project.ListViewAdapters.PaintRecyclerAdapter;
import com.example.a390project.ListViewAdapters.PrepaintTaskListViewAdapter;
import com.example.a390project.ListViewAdapters.ProjectListViewAdapter;
import com.example.a390project.ListViewAdapters.TaskListViewAdapter;
import com.example.a390project.Model.ControlDevice;
import com.example.a390project.Model.Employee;
import com.example.a390project.Model.EmployeeComment;
import com.example.a390project.Model.Graph;
import com.example.a390project.Model.GraphData;
import com.example.a390project.Model.Machine;
import com.example.a390project.Model.Material;
import com.example.a390project.Model.Oven;
import com.example.a390project.Model.PaintBucket;
import com.example.a390project.Model.Paintbooth;
import com.example.a390project.Model.Project;
import com.example.a390project.Model.SubTask;
import com.example.a390project.Model.Task;
import com.example.a390project.Model.User;
import com.example.a390project.Model.WorkBlock;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static java.lang.Integer.parseInt;

public class FirebaseHelper {

    // ------------------------------------------- FirebaseHelper variables -------------------------------------------
    private static final String TAG = "FirebaseHelper";
    private DatabaseReference rootRef;
    private String uId;
    public static final String CHANNEL_ID = "NotificationChannel";

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

    // ------------------------------------------- Task variables -------------------------------------------
    private List<String> taskIDs;
    private List<Task> tasks;
    private ValueEventListener packagingValueEventListener;
    private ValueEventListener inspectionValueEventListener;


    // ------------------------------------------- Employee Comments Variables -------------------------------------------
    private ValueEventListener employeeCommentsValueEventListener;

    // -------------------------------------------------------------------------------------------------------------

    // -------------------------------------------Notification timer method

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


    /*
    -------------------------------------------Employees workingTasks & completedTasks ListView Population methods ------------------------------------------------------------
     */

    public void setWorkingTasksValueListener(String userID, final TextView noWorkingTasksTextView, final Activity activity, final ListView assignedTasksListView){
        rootRef.child("users").child(userID).child("workingTasks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> workingTaskIDs = new ArrayList<>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    boolean isTaskCompleted = postSnapshot.child("completed").getValue(boolean.class);
                    if (!isTaskCompleted) {
                        workingTaskIDs.add(postSnapshot.getKey());
                    }
                }
                rootRef.child("tasks").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Task> workingTasks = new ArrayList<>();
                        for (String taskID : workingTaskIDs) {
                            if (dataSnapshot.hasChild(taskID)) {
                                Task newTask = new Task();
                                newTask.setTaskID(taskID);
                                newTask.setProjectPO(dataSnapshot.child(taskID).child("projectPO").getValue(String.class));
                                newTask.setTaskType(dataSnapshot.child(taskID).child("taskType").getValue(String.class));
                                workingTasks.add(newTask);
                                Log.d(TAG, "onDataChange: Project PO" + newTask.getProjectPO());
                            }
                        }
                        if (workingTasks.size() < 1){
                            noWorkingTasksTextView.setVisibility(View.VISIBLE);
                        }
                        else {
                            noWorkingTasksTextView.setVisibility(View.GONE);
                            callWorkingTasksListViewAdapter(activity, workingTasks, assignedTasksListView);
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

    private void callWorkingTasksListViewAdapter(Activity activity, List<Task> assignedTasks, ListView assignedTasksListView){

        EmployeeTasksListViewAdapter adapter = new EmployeeTasksListViewAdapter(activity, assignedTasks);
        assignedTasksListView.setAdapter(adapter);
        setEmployeeTasksListViewHeightBasedOnChildren(assignedTasksListView);


    }

    public void setCompletedTasksValueEventListener(String userID, final TextView noCompletedTasksTextView, final Activity activity){


        rootRef.child("users").child(userID).child("completedTasks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final List<String> completedTasksIDs = new ArrayList<>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
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
                            if (completedTasks.size() < 1) {
                                noCompletedTasksTextView.setVisibility(View.VISIBLE);
                            } else {
                                noCompletedTasksTextView.setVisibility(View.GONE);
                                callCompletedTasksListViewAdapter(activity, completedTasks);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void callCompletedTasksListViewAdapter(Activity activity, List<Task> completedTasks){
        if (!completedTasks.isEmpty()) {
            EmployeeTasksListViewAdapter adapter = new EmployeeTasksListViewAdapter(activity, completedTasks);
            ListView completedTasksListView = activity.findViewById(R.id.completedTasksListView);
            if (completedTasksListView != null) {
                completedTasksListView.setAdapter(adapter);
                setEmployeeTasksListViewHeightBasedOnChildren(completedTasksListView);
            }
        }
    }

    /*
    ------------------------------ Firebase Project Methods --------------------------------------------------------
     */

    public void createProject(final String po, final String title, final String client, final long startDate, final long dueDate, final Context context) {
        rootRef.child("projects").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rootRef.child("projects").removeEventListener(this);
                if (dataSnapshot.hasChild(po)) {
                    Toast.makeText(context, "Enter a unique Project PO...", Toast.LENGTH_SHORT).show();
                }
                else {
                    rootRef.child("projects").child(po).setValue(new Project(po, title, client, startDate, dueDate));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void populateProjects(final View view, final Activity activity, final ProgressBar mProgressbar, final boolean isDueDateSort, final boolean isStartDateSort) {
        rootRef.child("projects").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Project> projects = new ArrayList<>();
                List<Boolean> projectsCompleted = new ArrayList<>();
                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                    String po = ds.getKey();
                    String title = ds.child("title").getValue(String.class);
                    String client = ds.child("client").getValue(String.class);
                    long startDate = ds.child("startDate").getValue(long.class);
                    long dueDate = ds.child("dueDate").getValue(long.class);
                    Boolean hasCompleted;
                    if (ds.child("completed").exists()) {
                        hasCompleted = (ds.child("completed").getValue(Boolean.class));
                    }
                    else {
                        hasCompleted = false;
                    }
                    projects.add(new Project(po, title, client, startDate, dueDate, hasCompleted));

                }

                if (!isDueDateSort && !isStartDateSort) {
                    callProjectListViewAdapter(view, activity, projects, projectsCompleted);
                }
                else if(isDueDateSort && !isStartDateSort) {
                    sortProjectsByDueDate(projects);
                    callProjectListViewAdapter(view, activity, projects, projectsCompleted);
                }
                else if(!isDueDateSort && isStartDateSort) {
                    sortProjectsByStartDate(projects);
                    callProjectListViewAdapter(view, activity, projects, projectsCompleted);
                }
                mProgressbar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private List<Project> sortProjectsByStartDate(List<Project> projects) {
        Collections.sort(projects,new Comparator<Project>(){
            @Override
            public int compare(final Project lhs, Project rhs) {
                if (lhs.getStartDate()<rhs.getStartDate()) {
                    return 1;
                }
                else if (lhs.getStartDate()>rhs.getStartDate()) {
                    return -1;
                }
                else {
                    return 0;
                }
            }
        });
        return projects;
    }

    private List<Project> sortProjectsByDueDate(List<Project> projects) {
        Collections.sort(projects,new Comparator<Project>(){
            @Override
            public int compare(final Project lhs, Project rhs) {
                if (lhs.getDueDate()<rhs.getDueDate()) {
                    return -1;
                }
                else if (lhs.getDueDate()>rhs.getDueDate()) {
                    return 1;
                }
                else {
                    return 0;
                }
            }
        });
        return projects;
    }

    private void callProjectListViewAdapter(View view, Activity activity, List<Project> projects, List<Boolean> projectsCompleted){
        ProjectListViewAdapter adapter = new ProjectListViewAdapter(activity, projects, projectsCompleted);
        ListView itemsListView  = view.findViewById(R.id.project_list_view);
        itemsListView.setAdapter(adapter);
    }

    // function that checks if all the tasks for a given project is complete or not
    ValueEventListener isTasksCompleted(final String projectPO, final ProjectActivity projectActivity){
        ValueEventListener valueEventListener;
        rootRef.addValueEventListener(valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // get all the taskIDs of a project
                List<String> taskIDs = new ArrayList<>();

                DataSnapshot projectTasksDataSnapshot = dataSnapshot.child("projects").child(projectPO).child("tasks");
                for (DataSnapshot currentSnapshot : projectTasksDataSnapshot.getChildren()){
                    taskIDs.add(currentSnapshot.getKey());
                }

                // for all the taskIDs, find if it is completed

                Boolean tasksCompleted = true;
                DataSnapshot taskDataSnapshot = dataSnapshot.child("tasks");
                for (String taskId : taskIDs){
                    if (taskDataSnapshot.child(taskId).exists()){
                        if (!taskDataSnapshot.child(taskId).child("completed").getValue(Boolean.class))
                            tasksCompleted = false;
                    }
                }

                // check if project is already completed
                Boolean projectCompleted = false;
                if (dataSnapshot.child("projects").child(projectPO).child("completed").exists()){
                    projectCompleted = dataSnapshot.child("projects").child(projectPO).child("completed").getValue(Boolean.class);
                }

                Log.d(TAG, "onDataChange: projectCompleted " + projectCompleted);
                // if tasks are not completed, then set the activity's tasksCompleted status to false and call the OnCreateOptionsMenu function to handle the complete project option

                projectActivity.setProjectCompleted(projectCompleted);
                projectActivity.setTasksCompleted(tasksCompleted);
                projectActivity.invalidateOptionsMenu();

                if (projectCompleted) {
                    FloatingActionButton mFabOpenTaskDialogFragment = projectActivity.findViewById(R.id.fab_open_dialog_fragment_task);
                    mFabOpenTaskDialogFragment.hide();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return valueEventListener;
    }

    void detatchTasksCompletedValueEventListener (ValueEventListener valueEventListener){
        rootRef.removeEventListener(valueEventListener);
    }

    // for the selected project to complete: calculates the total time from the work blocks of each task and deletes them
    public void calculateTotalTimes(final String projectPO){

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // get the taskIDs
                DataSnapshot projectDS = dataSnapshot.child("projects").child(projectPO);
                List<String> taskIDs = new ArrayList<>();
                for (DataSnapshot currentDS : projectDS.child("tasks").getChildren()){
                    taskIDs.add(currentDS.getKey());
                }

                // get the workBlocks from the tasks and calculate the total time
                DataSnapshot taskDS = dataSnapshot.child("tasks");
                for (String taskID : taskIDs){
                    if (taskDS.child(taskID).exists()){
                        //delete the workBlocks
                        taskDS.child(taskID).child("workBlocks").getRef().removeValue();

                        // go through all the workBlocks and calculate the totalTime and delete the block
                        long totalTime = 0;
                        if (dataSnapshot.child("workHistory").child("workingTasks").child(taskID).exists()){

                            for (DataSnapshot currentDS : dataSnapshot.child("workHistory").child("workingTasks").child(taskID).child("workBlocks").getChildren()){
                                totalTime += currentDS.child("workingTime").getValue(long.class);
                                // get the employeeIDs and delete the workblock in the employee
                                String employeeID = currentDS.child("employeeID").getValue(String.class);
                                String workBlockID = currentDS.getKey();
                                // go to employee and delete workblock
                                if (dataSnapshot.child("users").child(employeeID).child("workingTasks").child(taskID).child("workBlocks").child(workBlockID).exists())
                                    dataSnapshot.child("users").child(employeeID).child("workingTasks").child(taskID).child("workBlocks").child(workBlockID).getRef().setValue(null);
                            }

                            //set the totalTime fo the task
                            taskDS.child(taskID).child("totalTime").getRef().setValue(totalTime);

                            // delete the workblock
                            dataSnapshot.child("workHistory").child("workingTasks").child(taskID).getRef().setValue(null);
                        }

                    }
                }

                // create a variable in project to indicate that the project has been completed
                dataSnapshot.child("projects").child(projectPO).child("completed").getRef().setValue(true);

                rootRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /*
    ------------------------------ Firebase Project Methods --------------------------------------------------------
     */

    public void createTask(String taskType, String taskDescription, String projectPO) {
        Calendar cal = Calendar.getInstance();
        long timeCreated = cal.getTimeInMillis();
        String taskID = Task.generateRandomChars();


        //add task in 'tasks'
        rootRef.child("tasks").child(taskID).setValue(new Task(taskID, projectPO,taskType,taskDescription,timeCreated,false));

        //add task in 'projects'>'tasks'
        rootRef.child("projects").child(projectPO).child("tasks").child(taskID).setValue(true);

    }

    public void populateTasks(final String projectPO, final Activity activity) {
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
                                boolean completed = dataSnapshot.child(taskID).child("completed").getValue(boolean.class);
                                tasks.add(new Task(taskID, projectPO, taskType, description, createdTime,completed));
                            }
                        }
                        sortTasksByLatestCreatedFirst(tasks);
                        callTaskListViewAdapter(activity, tasks, projectPO);
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

    //will sort 'tasks' starting with the top row being the latest created one
    private List<Task> sortTasksByLatestCreatedFirst(List<Task> tasks) {
        Collections.sort(tasks,new Comparator<Task>(){
            @Override
            public int compare(final Task lhs, Task rhs) {
                if (lhs.getCreatedTime()<rhs.getCreatedTime()) {
                    return 1;
                }
                else if (lhs.getCreatedTime()>rhs.getCreatedTime()) {
                    return -1;
                }
                else {
                    return 0;
                }
            }
        });
        return tasks;
    }

    private void callTaskListViewAdapter(Activity activity, List<Task> tasks, String projectPO) {
        TaskListViewAdapter adapter = new TaskListViewAdapter(activity, tasks, projectPO);
        ListView itemsListView  = activity.findViewById(R.id.project_tasks_list_view);
        if (itemsListView!= null)
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

    public ValueEventListener setTaskPackagingActivityListener(final String taskID, final Activity activity){
        ValueEventListener packagingValueEventListener;
        rootRef.addValueEventListener(packagingValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot taskDataSnapshot = dataSnapshot.child("tasks").child(taskID);
                Task packagingTask = new Task();

                // description
                packagingTask.setDescription(taskDataSnapshot.child("description").getValue(String.class));
                TextView packagingDescriptionTextView = activity.findViewById(R.id.descriptionTextView);
                packagingDescriptionTextView.setText(packagingTask.getDescription());

                if (taskDataSnapshot.child("completed").exists()){
                    if (taskDataSnapshot.child("completed").getValue(Boolean.class)){
                        LinearLayout addMaterialLinearLayout = activity.findViewById(R.id.addMaterialLinearLayout);
                        LinearLayout newCommentLinearLayout = activity.findViewById(R.id.newCommentLinearLayout);

                        Button startButton = activity.findViewById(R.id.startTimeButton);
                        Button endButton = activity.findViewById(R.id.endTimeButton);
                        Button completedButton = activity.findViewById(R.id.completed_packaging_task);

                        startButton.setVisibility(View.GONE);
                        endButton.setVisibility(View.GONE);
                        completedButton.setVisibility(View.GONE);

                        addMaterialLinearLayout.setVisibility(View.GONE);
                        newCommentLinearLayout.setVisibility(View.GONE);


                    }
                }

                // inventory
                if (taskDataSnapshot.child("materials").exists()){
                    List<String> materialIDs = new ArrayList<>();

                    // fill materialIDs array with all the material IDs
                    for(DataSnapshot materialSnapshot : taskDataSnapshot.child("materials").getChildren()){
                        materialIDs.add(materialSnapshot.getKey());
                    }

                    // go to the inventory section and get all the materials
                    List<Material> materials = new ArrayList<>();
                    DataSnapshot materialDataSnapshot = dataSnapshot.child("inventory").child("material");

                    for (String materialID : materialIDs){
                        if (materialDataSnapshot.child(materialID).exists()) {
                            Material newMaterial = materialDataSnapshot.child(materialID).getValue(Material.class);
                            newMaterial.setMaterialName(materialID);
                            materials.add(newMaterial);
                        }
                    }

                    // output materials into listView
                    ListView materialListView = activity.findViewById(R.id.materialListView);
                    InventoryMaterialListViewAdapter adapter = new InventoryMaterialListViewAdapter(activity, materials);
                    materialListView.setAdapter(adapter);
                    setPackagingMaterialListViewHeightBasedOnChildren(materialListView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return packagingValueEventListener;
    }

    private static void setPackagingMaterialListViewHeightBasedOnChildren(ListView listView) {
        InventoryMaterialListViewAdapter listAdapter = (InventoryMaterialListViewAdapter) listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 45;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() + 1));
        listView.setLayoutParams(params);
    }

    public ValueEventListener populateMaterialSpinner(final Activity activity){
        ValueEventListener machineValueEventListener;
        rootRef.child("inventory").child("material").addValueEventListener(machineValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> materials = new ArrayList<>();

                for (DataSnapshot currentSnapshot : dataSnapshot.getChildren()){
                    materials.add(currentSnapshot.getKey());
                    Log.d(TAG, "onDataChange: materials " + currentSnapshot.getKey());
                }

                Spinner materialSpinner = activity.findViewById(R.id.materialSpinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, materials);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                materialSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return machineValueEventListener;
    }

    public void addMaterialUsed(String taskID, String materialId){
        rootRef.child("tasks").child(taskID).child("materials").child(materialId).setValue(true);
    }


    //------------------------------ Inspection Task Methods --------------------------------------------------------

    public void setTaskInspectionActivityListener(String taskID, final Activity activity){
        rootRef.child("tasks").child(taskID).addValueEventListener(inspectionValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("completed").getValue(Boolean.class)){
                    EditText partCountedEditText = activity.findViewById(R.id.partCountedEditText);
                    EditText partAcceptedEditText = activity.findViewById(R.id.partAcceptedEditText);
                    EditText partRejectedEditText = activity.findViewById(R.id.partRejectedEditText);
                    LinearLayout newCommentLinearLayout = activity.findViewById(R.id.newCommentLinearLayout);
                    Button completeButton = activity.findViewById(R.id.completeButton);
                    LinearLayout timeLinearLayout = activity.findViewById(R.id.timeLinearLayout);

                    partCountedEditText.setEnabled(false);
                    partAcceptedEditText.setEnabled(false);
                    partRejectedEditText.setEnabled(false);

                    completeButton.setVisibility(View.GONE);
                    timeLinearLayout.setVisibility(View.GONE);

                    newCommentLinearLayout.setVisibility(View.GONE);
                }

                if (dataSnapshot.hasChild("partCounted")) {
                    int partCounted = dataSnapshot.child("partCounted").getValue(int.class);
                    EditText partCountedEditText = activity.findViewById(R.id.partCountedEditText);
                    partCountedEditText.setText(Integer.toString(partCounted));
                }
                if (dataSnapshot.hasChild("partAccepted")) {
                    int partAccepted = dataSnapshot.child("partAccepted").getValue(int.class);
                    EditText partAcceptedEditText = activity.findViewById(R.id.partAcceptedEditText);
                    partAcceptedEditText.setText(Integer.toString(partAccepted));
                }
                if (dataSnapshot.hasChild("partRejected")) {
                    int partRejected = dataSnapshot.child("partRejected").getValue(int.class);
                    EditText partRejectedEditText = activity.findViewById(R.id.partRejectedEditText);
                    partRejectedEditText.setText(Integer.toString(partRejected));
                }
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
    /*
    --------------------------------------------------------- DELETE TASKS-PROJECTS METHODS ---------------------------------------------------------------------------
     */

    public void deleteTask(String TaskID, String projectPO){
        //remove task from projects
        rootRef.child("projects").child(projectPO).child("tasks").child(TaskID).removeValue();

        //remove task from tasks
        rootRef.child("tasks").child(TaskID).removeValue();

        //remove the subtasks if any
        rootRef.child("tasks").child(TaskID).child("subTasks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String subkey = ds.getKey();
                        DatabaseReference SubTaskindb = FirebaseDatabase.getInstance().getReference("subTasks").child(subkey);
                        SubTaskindb.removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void deleteProject(final String projectPO) {
        rootRef.child("projects").child(projectPO).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rootRef.child("projects").child(projectPO).removeEventListener(this);
                if (dataSnapshot.exists()) {
                    List<String> taskIDs = new ArrayList<>();
                    List<String> subTaskIDs = new ArrayList<>();
                    List<String> graphIDs = new ArrayList<>();
                    if (dataSnapshot.hasChild("tasks")) {
                        for (DataSnapshot ds : dataSnapshot.child("tasks").getChildren()) {
                            taskIDs.add(ds.getKey());
                            if (ds.hasChild("subTasks")) {
                                for (DataSnapshot dsSub : ds.child("subTasks").getChildren()) {
                                    subTaskIDs.add(dsSub.getKey());
                                }
                            }
                        }
                    }
                    if (dataSnapshot.hasChild("graphs")) {
                        for (DataSnapshot ds : dataSnapshot.child("graphs").getChildren()) {
                            graphIDs.add(ds.getKey());
                        }
                    }
                    if (!taskIDs.isEmpty()) {
                        for (String taskID : taskIDs) {
                            rootRef.child("tasks").child(taskID).removeValue();
                        }
                    }
                    if (!subTaskIDs.isEmpty()) {
                        for (String subTaskID : subTaskIDs) {
                            rootRef.child("subTasks").child(subTaskID).removeValue();
                        }
                    }
                    if (!graphIDs.isEmpty()) {
                        for (final String graphID : graphIDs) {
                            rootRef.child("projects").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    rootRef.child("projects").removeEventListener(this);
                                    boolean deleteGraph = true;
                                    for (DataSnapshot ds:dataSnapshot.getChildren()) {
                                        if (!ds.getKey().equals(projectPO)) {
                                            if (ds.child("graphs").hasChild(graphID)) {
                                                deleteGraph = false;
                                            }
                                        }
                                    }
                                    if (deleteGraph) {
                                        rootRef.child("graphs").child(graphID).removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                    rootRef.child("projects").child(projectPO).removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /*
    --------------------------------------------------------- END OF DELETE METHODS ---------------------------------------------------------------------------
     */


    //end of Inspection Controllers

    //------------------------------ Firebase Control Device Methods --------------------------------------------------------

    public void createControlDevice(ControlDevice cDevice){
        rootRef.child("cDevices").child(cDevice.getcDeviceTitle()).setValue(cDevice);
    }

    public void populateControlDevices(final View view, final Activity activity){
        rootRef.child("cDevices").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rootRef.child("cDevices").removeEventListener(this);
                List<ControlDevice> cDevices = new ArrayList<>();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String cDeviceTitle = ds.child("cDeviceTitle").getValue(String.class);
                    boolean cDeviceStatus = ds.child("cDeviceStatus").getValue(boolean.class);
                    cDevices.add(new ControlDevice(cDeviceTitle, cDeviceStatus));
                }
                callControlDeviceListViewAdapter(view, activity, cDevices);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void callControlDeviceListViewAdapter(View view, Activity activity, List<ControlDevice> cDevices){
        ControlDeviceListViewAdapter adapter = new ControlDeviceListViewAdapter(activity, cDevices);
        ListView itemsListView = view.findViewById(R.id.control_device_list_view);
        itemsListView.setAdapter(adapter);
    }

    public void setStatusOfSwitch(final String cDeviceTitle, final Switch switchControlDevice) {
        rootRef.child("cDevices").child(cDeviceTitle).child("cDeviceStatus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rootRef.child("cDevices").child(cDeviceTitle).child("cDeviceStatus").removeEventListener(this);
                boolean checked = dataSnapshot.getValue(boolean.class);
                switchControlDevice.setChecked(checked);
                Log.d(TAG, cDeviceTitle + " IS SET TO " + checked);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void changeDeviceStatus(final String cDeviceTitle, final boolean state, final Context context) {
        rootRef.child("users").child(uId).child("canToggleCDevices").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rootRef.child("users").child(uId).child("canToggleCDevices").removeEventListener(this);
                if (dataSnapshot.getValue(boolean.class)) {
                    rootRef.child("cDevices").child(cDeviceTitle).child("cDeviceStatus").setValue(state);
                    Log.d(TAG, cDeviceTitle + " IS NOW " + state);
                }
                else {
                    Toast.makeText(context, "Not inside shop radius", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void canToggleSwitch(final Switch switchControlDevice, final Context context) {
        rootRef.child("users").child(uId).child("canToggleCDevices").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.getValue(boolean.class)) {
                        switchControlDevice.setClickable(true);
                    } else {
                        switchControlDevice.setClickable(false);
                        //Toast.makeText(context, "Not inside shop radius t", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    // -------------------------------------------- Firebase Prepaint Task Methods --------------------------------------------
    public ValueEventListener populateSubTasks(final String taskId, final Activity activity, final boolean[] backPressed){

        ValueEventListener prepaintValueEventListener;

        rootRef.addValueEventListener(prepaintValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> subTasksID = new ArrayList<>();
                List<SubTask> subTasks = new ArrayList<>();

                if (dataSnapshot.child("tasks").child(taskId).child("completed").exists()){
                    if (dataSnapshot.child("tasks").child(taskId).child("completed").getValue(Boolean.class)){
                        Button completeButton = activity.findViewById(R.id.prepaintingCompletedButton);
                        LinearLayout newCommentLinearLayout = activity.findViewById(R.id.newCommentLinearLayout);

                        completeButton.setVisibility(View.GONE);
                        newCommentLinearLayout.setVisibility(View.GONE);
                    }
                }

                // subTasks
                for(DataSnapshot ds : dataSnapshot.child("tasks").child(taskId).child("subTasks").getChildren()) {
                    subTasksID.add(ds.getKey());
                }


                for (String subTaskID : subTasksID) {
                    boolean isCompleted = dataSnapshot.child("subTasks").child(subTaskID).child("completed").getValue(boolean.class);
                    if (!isCompleted) {
                        String subTaskType = dataSnapshot.child("subTasks").child(subTaskID).child("subTaskType").getValue(String.class);
                        String ID = dataSnapshot.child("subTasks").child(subTaskID).child("subTaskID").getValue(String.class);
                        String projectID = dataSnapshot.child("subTasks").child(subTaskID).child("projectID").getValue(String.class);
                        String taskID = dataSnapshot.child("subTasks").child(subTaskID).child("taskID").getValue(String.class);
                        long createdTime = dataSnapshot.child("subTasks").child(subTaskID).child("createdTime").getValue(long.class);
                        subTasks.add(new SubTask(subTaskType,subTaskID,projectID,taskID,isCompleted,createdTime));
                    }
                }
                callPrepaintTaskListViewAdapter(activity, subTasks, backPressed);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return prepaintValueEventListener;
    }

    public void detatchPrepaintValueEventListener(String taskID, ValueEventListener valueEventListener){
        rootRef.child("tasks").child(taskID).child("subTasks").removeEventListener(valueEventListener);
    }

    private void callPrepaintTaskListViewAdapter(Activity activity, List<SubTask> subTasks, boolean[] backPressed){
        PrepaintTaskListViewAdapter adapter = new PrepaintTaskListViewAdapter(activity, subTasks, activity, backPressed);
        ListView prepaintTasksListView = activity.findViewById(R.id.prepaintTaskListView);
        prepaintTasksListView.setAdapter(adapter);
        setPrepaintTasksListViewHeightBasedOnChildren(prepaintTasksListView);
    }

    private static void setPrepaintTasksListViewHeightBasedOnChildren(ListView listView) {
        PrepaintTaskListViewAdapter listAdapter = (PrepaintTaskListViewAdapter) listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 45;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
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

    // ------------------------------------------------ Firebase Painting Methods ------------------------------------------------

    public void setPaintingValues(final TextView mPaintCode, final TextView mBakeTemp, final TextView mBakeTime, final TextView mDescription, final TextView mPaintDescription, String paintingTaskID, final Activity activity) {
        rootRef.child("tasks").child(paintingTaskID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final String paintType = dataSnapshot.child("paintType").getValue(String.class);
                final String paintCode = dataSnapshot.child("paintCode").getValue(String.class);
                final String description = dataSnapshot.child("description").getValue(String.class);

                if (dataSnapshot.child("completed").exists()){
                    if (dataSnapshot.child("completed").getValue(Boolean.class)){
                        EditText viscosityEditText = activity.findViewById(R.id.viscosityEditText);
                        EditText tipSizeEditText = activity.findViewById(R.id.tipSizeEditText);
                        EditText pressureLiquidEditText = activity.findViewById(R.id.pressureLiquidEditText);
                        EditText amountEditText = activity.findViewById(R.id.amountEditText);
                        EditText spreadEditText = activity.findViewById(R.id.spreadEditText);
                        EditText pressurePowderEditText = activity.findViewById(R.id.pressurePowderEditText);

                        Button saveButton = activity.findViewById(R.id.saveButton);

                        LinearLayout timeLinearLayout = activity.findViewById(R.id.timeLinearLayout);
                        Button completeButton = activity.findViewById(R.id.completed_painting_task);
                        LinearLayout newComments = activity.findViewById(R.id.newCommentLinearLayout);
                        Switch reCoatSwitch = activity.findViewById(R.id.reCoatSwitch);

                        viscosityEditText.setEnabled(false);
                        tipSizeEditText.setEnabled(false);
                        pressureLiquidEditText.setEnabled(false);
                        amountEditText.setEnabled(false);
                        spreadEditText.setEnabled(false);
                        pressurePowderEditText.setEnabled(false);

                        saveButton.setVisibility(View.GONE);

                        timeLinearLayout.setVisibility(View.GONE);
                        completeButton.setVisibility(View.GONE);
                        newComments.setVisibility(View.GONE);
                        reCoatSwitch.setClickable(false);


                    }
                }

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

                // check if paintType is powder or liquid
                // if powder paint, then make the linearLayout for liquid gone and vice versa
                switch (paintType) {
                    case "powder": {
                        LinearLayout liquidLinearLayout = activity.findViewById(R.id.liquidLinearLayout);
                        liquidLinearLayout.setVisibility(View.GONE);

                        EditText amountEditText = activity.findViewById(R.id.amountEditText);
                        EditText spreadEditText = activity.findViewById(R.id.spreadEditText);
                        Switch reCoatSwitch = activity.findViewById(R.id.reCoatSwitch);
                        EditText pressurePowderEditText = activity.findViewById(R.id.pressurePowderEditText);

                        if (dataSnapshot.hasChild("amount"))
                            amountEditText.setText(String.valueOf(dataSnapshot.child("amount").getValue(Long.class)));
                        if (dataSnapshot.hasChild("spread"))
                            spreadEditText.setText(String.valueOf(dataSnapshot.child("spread").getValue(Long.class)));
                        if (dataSnapshot.hasChild("reCoat"))
                            reCoatSwitch.setChecked(dataSnapshot.child("reCoat").getValue(Boolean.class));
                        if (dataSnapshot.hasChild("pressure"))
                            pressurePowderEditText.setText(String.valueOf(dataSnapshot.child("pressure").getValue(Long.class)));

                        break;
                    }
                    case "liquid": {
                        LinearLayout powderLinearLayout = activity.findViewById(R.id.powderLinearLayout);
                        powderLinearLayout.setVisibility(View.GONE);

                        EditText viscosityEditText = activity.findViewById(R.id.viscosityEditText);
                        EditText tipSizeEditText = activity.findViewById(R.id.tipSizeEditText);
                        EditText pressureLiquidEditText = activity.findViewById(R.id.pressureLiquidEditText);

                        if (dataSnapshot.hasChild("viscosity"))
                            viscosityEditText.setText(String.valueOf(dataSnapshot.child("viscosity").getValue(Long.class)));
                        if (dataSnapshot.hasChild("tipSize"))
                            tipSizeEditText.setText(String.valueOf(dataSnapshot.child("tipSize").getValue(Long.class)));
                        if (dataSnapshot.hasChild("pressure"))
                            pressureLiquidEditText.setText(String.valueOf(dataSnapshot.child("pressure").getValue(Long.class)));

                        break;
                    }
                    default: {
                        LinearLayout powderLinearLayout = activity.findViewById(R.id.powderLinearLayout);
                        powderLinearLayout.setVisibility(View.GONE);
                        LinearLayout liquidLinearLayout = activity.findViewById(R.id.liquidLinearLayout);
                        liquidLinearLayout.setVisibility(View.GONE);
                        break;
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // setters
    // set viscosity
    void setViscosity(String taskId, long viscosity){
        rootRef.child("tasks").child(taskId).child("viscosity").setValue(viscosity);
    }

    // set tipSize
    void setTipSize(String taskId, long tipSize){
        rootRef.child("tasks").child(taskId).child("tipSize").setValue(tipSize);
    }

    // set pressure
    void setPressure(String taskId, long pressure){
        rootRef.child("tasks").child(taskId).child("pressure").setValue(pressure);
    }

    // set ammount
    void setAmount(String taskId, long amount){
        rootRef.child("tasks").child(taskId).child("amount").setValue(amount);
    }

    // set spread
    void setSpread(String taskId, long spread){
        rootRef.child("tasks").child(taskId).child("spread").setValue(spread);
    }

    // set reCoat
    void setReCoat(String taskId, Boolean reCoat){
        rootRef.child("tasks").child(taskId).child("reCoat").setValue(reCoat);
    }



    /*
       ------------------------------------------------ Firebase PrePaintingTask Methods ------------------------------------------------
     */

    public void createPrepaintingTask(String taskID, String projectPO, String prePainting, String taskDescription, long createdTime, List<SubTask> subTasks) {
        rootRef.child("tasks").child(taskID).setValue(new Task(taskID, projectPO, prePainting, taskDescription, createdTime,false));
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
        rootRef.child("tasks").child(taskID).setValue(new Task(taskID, projectPO,"Painting",taskDescription,createdTime,false));
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

    public ValueEventListener populatePaintInventory(final Activity activity, final Boolean isLiquid) {

        ValueEventListener paintValueEventListener;
        rootRef.child("inventory").addValueEventListener(paintValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<PaintBucket> paintBuckets = new ArrayList<>();

                DataSnapshot paintDataSnapshot;

                if (isLiquid)
                    paintDataSnapshot = dataSnapshot.child("liquid");
                else
                    paintDataSnapshot = dataSnapshot.child("powder");

                for(DataSnapshot currentSnapshot : paintDataSnapshot.getChildren()){
                    String paintCode = currentSnapshot.getKey();
                    String paintDescription = currentSnapshot.child("paintDescription").getValue(String.class);
                    int bakeTemperature = currentSnapshot.child("bakeTemperature").getValue(int.class);
                    int bakeTime = currentSnapshot.child("bakeTime").getValue(int.class);
                    float paintWeight = currentSnapshot.child("paintWeight").getValue(float.class);

                    paintBuckets.add(new PaintBucket("powder", paintCode, paintDescription, bakeTemperature, bakeTime, paintWeight));
                }
                // sort the paintBuckets
                paintBuckets = sortPaintBucketsByFirstLetter(paintBuckets);

                // set the adapter
                //InventoryPaintListViewAdapter adapter = new InventoryPaintListViewAdapter(activity, paintBuckets);
                PaintRecyclerAdapter adapter = new PaintRecyclerAdapter(activity, paintBuckets);
                if (isLiquid) {
                    //ListView liquidPaintListView = activity.findViewById(R.id.liquidPaintListView);
                    //liquidPaintListView.setAdapter(adapter);
                    RecyclerView liquidRecyclerView = activity.findViewById(R.id.liquidRecyclerView);
                    liquidRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
                    liquidRecyclerView.setAdapter(adapter);

                }

                else{
                    //ListView powderPaintListView = activity.findViewById(R.id.powderPaintListView);
                    //powderPaintListView.setAdapter(adapter);
                    RecyclerView powderRecyclerView = activity.findViewById(R.id.powderRecyclerView);
                    powderRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
                    powderRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return paintValueEventListener;
    }

    private List<PaintBucket> sortPaintBucketsByFirstLetter(List<PaintBucket> paintBuckets) {
        Collections.sort(paintBuckets,new Comparator<PaintBucket>() {
            @Override
            public int compare(PaintBucket lhs, PaintBucket rhs) {
                char first = lhs.getPaintDescription().charAt(0);
                char second = rhs.getPaintDescription().charAt(0);
                if (first<second) {
                    return -1;
                }
                else if (first>second) {
                    return 1;
                }
                else {
                    return 0;
                }
            }
        });

        return paintBuckets;
    }

    public void createInventoryPaintItem(String paintType, String paintCode, String paintDescription, int paintBakeTemperature, int paintBakeTime, float paintWeight) {
        rootRef.child("inventory").child(paintType).child(paintCode).setValue(new PaintBucket(paintType,paintCode,paintDescription,paintBakeTemperature,paintBakeTime,paintWeight));
    }

    public void createInventoryMaterialItem(String materialName, Material newMaterial){
        rootRef.child("inventory").child("material").child(materialName).setValue(newMaterial);
        Log.d(TAG, "createInventoryMaterialItem: started inventory material");
    }

    public ValueEventListener populateMaterialInventory(final Activity activity){
        ValueEventListener materialValueEventListener;

        rootRef.child("inventory").child("material").addValueEventListener(materialValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Material> materials = new ArrayList<>();
                for (DataSnapshot currentSnapshot : dataSnapshot.getChildren()){
                    Material newMaterial = currentSnapshot.getValue(Material.class);
                    newMaterial.setMaterialName(currentSnapshot.getKey());
                    Log.d(TAG, "Material Key " + currentSnapshot.getKey());
                    materials.add(newMaterial);
                }

                // set adapter
                materials = sortMaterialsByFirstLetter(materials);
                MaterialRecyclerAdapter adapter = new MaterialRecyclerAdapter(activity, materials);
                RecyclerView materialRecyclerView = activity.findViewById(R.id.materialRecyclerView);
                materialRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
                materialRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return materialValueEventListener;
    }

    private List<Material> sortMaterialsByFirstLetter(List<Material> materials) {
        Collections.sort(materials, new Comparator<Material>() {
            @Override
            public int compare(Material o1, Material o2) {
                char first = o1.getMaterialName().charAt(0);
                char second = o2.getMaterialName().charAt(0);
                if (first < second)
                    return -1;
                else if (first > second)
                    return 1;
                else
                    return 0;
            }
        });

        return materials;
    }

    public void liquidPaintSearch(final Activity activity, final String searchText){
        rootRef.child("inventory").child("liquid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<PaintBucket> paintBuckets = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String paintCode = ds.getKey();
                    String paintDescription = ds.child("paintDescription").getValue(String.class);
                    int bakeTemperature = ds.child("bakeTemperature").getValue(int.class);
                    int bakeTime = ds.child("bakeTime").getValue(int.class);
                    float paintWeight = ds.child("paintWeight").getValue(float.class);
                    if (paintDescription.toLowerCase().contains(searchText)) {
                        paintBuckets.add(new PaintBucket("liquid", paintCode, paintDescription, bakeTemperature, bakeTime, paintWeight));
                    }

                    PaintRecyclerAdapter adapter = new PaintRecyclerAdapter(activity, paintBuckets);
                    RecyclerView liquidRecyclerView = activity.findViewById(R.id.liquidRecyclerView);
                    liquidRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
                    liquidRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


    }

    public void powderPaintSearch(final Activity activity, final String searchText) {
        rootRef.child("inventory").child("powder").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<PaintBucket> paintBuckets = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String paintCode = ds.getKey();
                    String paintDescription = ds.child("paintDescription").getValue(String.class);
                    int bakeTemperature = ds.child("bakeTemperature").getValue(int.class);
                    int bakeTime = ds.child("bakeTime").getValue(int.class);
                    float paintWeight = ds.child("paintWeight").getValue(float.class);

                    if (paintDescription.toLowerCase().contains(searchText)) {
                        paintBuckets.add(new PaintBucket("powder", paintCode, paintDescription, bakeTemperature, bakeTime, paintWeight));
                    }
                }
                PaintRecyclerAdapter adapter = new PaintRecyclerAdapter(activity, paintBuckets);
                RecyclerView powderRecyclerView = activity.findViewById(R.id.powderRecyclerView);
                powderRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
                powderRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }


    public void materialSearch(final Activity activity, final String searchText){
        rootRef.child("inventory").child("material").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Material> materials = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String materialName = ds.getKey();
                    String materialDescription = ds.child("materialDescription").getValue(String.class);
                    Float materialQuantity = ds.child("materialQuantity").getValue(Float.class);
                    Material newMaterial = new Material(materialDescription, materialQuantity);
                    newMaterial.setMaterialName(ds.getKey());
                    
                    if(materialName.toLowerCase().contains(searchText)) {
                        materials.add(newMaterial);
                    }

                    MaterialRecyclerAdapter adapter = new MaterialRecyclerAdapter(activity, materials);
                    RecyclerView materialRecyclerView = activity.findViewById(R.id.materialRecyclerView);
                    materialRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
                    materialRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    // ------------------------------------------------ Firebase Employee Comments ------------------------------------------------

    // function that gets all the comments of the specified task
    void getEmployeeComments(final Activity activity, final String taskID){
        rootRef.child("tasks").child(taskID).addValueEventListener(employeeCommentsValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot employeeDataSnapshot) {
                DataSnapshot dataSnapshot = employeeDataSnapshot.child("employeeComments");

                List<EmployeeComment> comments = new ArrayList<>();
                Log.d(TAG, "onDataChange: Called snapshot " + taskID);
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    EmployeeComment currentComment = postSnapshot.getValue(EmployeeComment.class);
                    Log.d(TAG, "onDataChange: Called snapshot");

                    Log.d(TAG, "onDataChange: got comment");
                    comments.add(currentComment);
                }

                if (comments.size() > 0)
                    callEmployeeCommentListViewAdapter(activity, comments);

                if (employeeDataSnapshot.child("completed").exists()) {
                    if (employeeDataSnapshot.child("completed").getValue(Boolean.class)) {
                        EditText commentEditText = activity.findViewById(R.id.newCommentsEditText);
                        commentEditText.setEnabled(false);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void callEmployeeCommentListViewAdapter(Activity activity, List<EmployeeComment> comments){
        ListView employeeCommentsListView;


            EmployeeCommentListViewAdapter adapter = new EmployeeCommentListViewAdapter(activity, comments);
            employeeCommentsListView = activity.findViewById(R.id.employeeCommentsListView);

            employeeCommentsListView.setAdapter(adapter);
            setEmployeeCommentsListViewHeightBasedOnChildren(employeeCommentsListView);

    }

    // Reference: https://stackoverflow.com/questions/18367522/android-list-view-inside-a-scroll-view/26998840
    private static void setEmployeeCommentsListViewHeightBasedOnChildren(ListView listView) {
        EmployeeCommentListViewAdapter listAdapter = (EmployeeCommentListViewAdapter) listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    // function that saves a comment to the specified task
    void postComment(final String taskID, final String comment){
        // generate an id for comments

        rootRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // get the date, username and save it into Firebase
                Long currentTime = System.currentTimeMillis();
                String username = dataSnapshot.getValue(String.class);
                Log.d(TAG, "postComment: " + username);

                EmployeeComment newComment = new EmployeeComment(username, currentTime, comment);


                // check if the data base
                String commentID = String.valueOf(currentTime);

                rootRef.child("tasks").child(taskID).child("employeeComments").child(commentID).setValue(newComment);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void detatchEmployeeCommentsListView(String taskID){
        rootRef.child("users").child(taskID).child("employeeComments").removeEventListener(employeeCommentsValueEventListener);
    }


    /*
    ---------------------------------------------- Create Work Block -------------------------------------------------------
     */

    public void checkIfCanStart(final String taskID, final Context context, final String taskTitle, final Activity activity, final Button mStartTime, final Button mEndTime,
                                final TextView mTimeElapsed, final boolean backPressed[], final View convertView) {
        final DatabaseReference uIdRef = rootRef.child("users").child(uId);
        uIdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uIdRef.removeEventListener(this);
                if (dataSnapshot.child("workingTasks").child(taskID).child("canStart").exists()) {
                    boolean canStart = dataSnapshot.child("workingTasks").child(taskID).child("canStart").getValue(boolean.class);
                    if (canStart) {
                        createWorkBlock(taskID, taskTitle, context, activity, mStartTime, mEndTime, mTimeElapsed, backPressed, convertView);
                    }
                    else {
                        Toast.makeText(context, "Cannot Start.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    createWorkBlock(taskID, taskTitle, context, activity, mStartTime, mEndTime, mTimeElapsed, backPressed, convertView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void createWorkBlock(final String taskID, final String taskTitle, final Context context, final Activity activity, final Button mStartTime, final Button mEndTime,
                                final TextView mTimeElapsed, final boolean backPressed[], final View convertView) {
        rootRef.child("users").child(uId).child("workingTasks").child(taskID).child("completed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    rootRef.child("users").child(uId).child("workingTasks").child(taskID).child("completed").setValue(false);
                }
                //if its not completed, execute workblock
                else if (!dataSnapshot.getValue(boolean.class)) {
                    //check if workBlockLimitHasBeenReached (limit of 3 running blocks concurrently)
                    rootRef.child("users").child(uId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            rootRef.child("users").child(uId).removeEventListener(this);
                            int count = 0;
                            if (dataSnapshot.hasChild("workBlockLimitCount")) {
                                count = dataSnapshot.child("workBlockLimitCount").getValue(int.class);
                            }
                            if (count < 2) { //<------------specifies how many concurrent tasks and employee can have at the same time
                                Toast.makeText(context, "Time Started.", Toast.LENGTH_SHORT).show();
                                rootRef.child("tasks").child(taskID).child("projectPO").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        rootRef.child("tasks").child(taskID).child("projectPO").removeEventListener(this);
                                        final String projectPO = dataSnapshot.getValue(String.class);
                                        final String workBlockID = WorkBlock.generateRandomChars();
                                        final long timeNow = System.currentTimeMillis();

                                        WorkBlock workBlock = new WorkBlock(workBlockID, timeNow, 0, 0, taskID, uId, taskTitle, projectPO);
                                        rootRef.child("workHistory").child("workingTasks").child(taskID).child("workBlocks").child(workBlockID).setValue(workBlock);

                                        rootRef.child("users").child(uId).child("workingTasks").child(taskID).child("workBlocks").child(workBlockID).setValue(true);
                                        rootRef.child("users").child(uId).child("workingTasks").child(taskID).child("canStart").setValue(false);
                                        rootRef.child("users").child(uId).child("workingTasks").child(taskID).child("canEnd").setValue(true);
                                        rootRef.child("users").child(uId).child("workingTasks").child(taskID).child("workBlockLimitCount").setValue(1);
                                        rootRef.child("users").child(uId).child("workingTasks").child(taskID).child("currentWorkBlock").setValue(workBlockID);
                                        rootRef.child("users").child(uId).child("workingTasks").child(taskID).child("completed").setValue(false);

                                        rootRef.child("tasks").child(taskID).child("workBlocks").child(workBlockID).setValue(true);

                                        Log.d(TAG, "WORKBLOCK CREATED: " + workBlockID + " " + taskTitle + " " + projectPO);

                                        //UPON CREATION OF WORKBLOCK, ALSO CREATE PERSISTING NOTIFICATION
                                        //createNotification(timeNow,context,taskTitle,projectPO,taskID);

                                        startService(timeNow, context, activity, taskTitle, projectPO, taskID);

                                        //this checks to see if the call was made from the pre-paint listview item
                                        if (convertView != null) {
                                            TextView mTimeElapsed1 = convertView.findViewById(R.id.elapsed_time_since_pressed_start_time_prepaint);
                                            createUpdateTimeElapsedThread(mTimeElapsed1, activity, timeNow, backPressed);
                                        }
                                        else {
                                            createUpdateTimeElapsedThread(mTimeElapsed, activity, timeNow, backPressed);
                                        }

                                        //manage workBlockLimitCount
                                        rootRef.child("users").child(uId).child("workBlockLimitCount").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                rootRef.child("users").child(uId).child("workBlockLimitCount").removeEventListener(this);
                                                if (dataSnapshot.exists()) {
                                                    int count = dataSnapshot.getValue(int.class);
                                                    rootRef.child("users").child(uId).child("workBlockLimitCount").setValue(count + 1);
                                                } else {
                                                    rootRef.child("users").child(uId).child("workBlockLimitCount").setValue(1);
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
                            } else {
                                Toast.makeText(context, "You have reached the limit of concurrent workblocks", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    Toast.makeText(context, "Task Completed.", Toast.LENGTH_SHORT).show();
                    mStartTime.setVisibility(View.GONE);
                    mEndTime.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public void checkIfCanEnd(final String taskID, final Context context, final TextView mTimeElapsed, final View finalConvertView) {
        final DatabaseReference uIdRef = rootRef.child("users").child(uId);
        uIdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uIdRef.removeEventListener(this);
                if (dataSnapshot.child("workingTasks").hasChild(taskID)) {
                    if (dataSnapshot.child("workingTasks").child(taskID).hasChild("canEnd")) {
                        boolean canEnd = dataSnapshot.child("workingTasks").child(taskID).child("canEnd").getValue(boolean.class);
                        if (canEnd) {
                            endWorkBlock(taskID, context, mTimeElapsed, finalConvertView);
                            Toast.makeText(context, "Time Ended.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Cannot End.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(context, "Cannot End.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(context, "Cannot End.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void endWorkBlock(final String taskID, final Context context, final TextView mTimeElapsed, final View finalConvertView) {
        final DatabaseReference currentWorkBlockRef = rootRef.child("users").child(uId).child("workingTasks").child(taskID).child("currentWorkBlock");
        currentWorkBlockRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentWorkBlockRef.removeEventListener(this);
                final String currentWorkBlock = dataSnapshot.getValue(String.class);
                final long timeNow = System.currentTimeMillis();

                final DatabaseReference startTimeRef = rootRef.child("workHistory").child("workingTasks").child(taskID).child("workBlocks").child(currentWorkBlock).child("startTime");
                startTimeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        startTimeRef.removeEventListener(this);
                        long startTime = dataSnapshot.getValue(long.class);
                        rootRef.child("workHistory").child("workingTasks").child(taskID).child("workBlocks").child(currentWorkBlock).child("endTime").setValue(timeNow);
                        long duration = timeNow - startTime;
                        rootRef.child("workHistory").child("workingTasks").child(taskID).child("workBlocks").child(currentWorkBlock).child("workingTime").setValue(duration);
                        rootRef.child("users").child(uId).child("workingTasks").child(taskID).child("canStart").setValue(true);
                        rootRef.child("users").child(uId).child("workingTasks").child(taskID).child("canEnd").setValue(false);
                        rootRef.child("users").child(uId).child("workingTasks").child(taskID).child("currentWorkBlock").setValue("none");

                        //UPON ENDING TIME, REMOVE THE PERSISTING NOTIFICATION
                        //removeNotification(startTime, context);

                        stopService(context);
                        if (finalConvertView != null) {
                            TextView mTimeElapsed1 = finalConvertView.findViewById(R.id.elapsed_time_since_pressed_start_time_prepaint);
                            mTimeElapsed1.setVisibility(View.GONE);
                        }
                        else {
                            mTimeElapsed.setVisibility(View.GONE);
                        }
                        //manage workBlockLimitCount
                        rootRef.child("users").child(uId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                rootRef.child("users").child(uId).removeEventListener(this);
                                if (dataSnapshot.hasChild("workBlockLimitCount")) {
                                    int count = dataSnapshot.child("workBlockLimitCount").getValue(int.class);
                                    rootRef.child("users").child(uId).child("workBlockLimitCount").setValue(count-1);
                                }
                                else {
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void populateWorkBlocksForEmployee(final String employeeID, final View view, final Activity activity) {
        final int[] i = {0};
        final List<WorkBlock> employeeWorkBlocks = new ArrayList<>();
        rootRef.child("users").child(employeeID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rootRef.child("users").child(employeeID).removeEventListener(this);
                if (dataSnapshot.hasChild("workingTasks")) {
                    for (final DataSnapshot taskData:dataSnapshot.child("workingTasks").getChildren()) {
                        for (final DataSnapshot workBlockData:taskData.child("workBlocks").getChildren()) {
                            rootRef.child("workHistory").child("workingTasks").child(taskData.getKey()).child("workBlocks").child(workBlockData.getKey()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String workBlockID = dataSnapshot.child("workBlockID").getValue(String.class);
                                    long startTime = 0;
                                    if (dataSnapshot.child("startTime").getValue(long.class) == null) {
                                        Log.d(TAG, "NUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUL: ");
                                    }
                                    else {
                                        startTime = dataSnapshot.child("startTime").getValue(long.class);
                                    }
                                    long endTime = dataSnapshot.child("endTime").getValue(long.class);
                                    long workingTime = dataSnapshot.child("workingTime").getValue(long.class);
                                    String taskID = dataSnapshot.child("taskID").getValue(String.class);
                                    String title = dataSnapshot.child("title").getValue(String.class);
                                    String projectPO = dataSnapshot.child("projectPO").getValue(String.class);
                                    //public WorkBlock(String workBlockID, long startTime, long endTime, long workingTime, String taskID, String employeeID)
                                    employeeWorkBlocks.add(new WorkBlock(workBlockID, startTime, endTime, workingTime, taskID, employeeID, title, projectPO));
                                    Log.d(TAG, "WORKBLOCK: " + i[0] + " " + workBlockID + " " + startTime + " " + endTime + " " + workingTime
                                            + " " + taskID + " " + workBlockID + " " + employeeID + " " + title + " " + projectPO);
                                    i[0] += 1;
                                    callEmployeeWorkBlocksListViewAdapter(view, activity, sortWorkBlocksByLatestCreatedFirst(employeeWorkBlocks));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private List<WorkBlock> sortWorkBlocksByLatestCreatedFirst(List<WorkBlock> employeeWorkBlocks) {
        Collections.sort(employeeWorkBlocks,new Comparator<WorkBlock>(){
            @Override
            public int compare(final WorkBlock lhs, WorkBlock rhs) {
                if (lhs.getStartTime()<rhs.getStartTime()) {
                    return 1;
                }
                else if (lhs.getStartTime()>rhs.getStartTime()) {
                    return -1;
                }
                else {
                    return 0;
                }
            }
        });
        return employeeWorkBlocks;
    }

    //Denies a task to be complete if the end time has not been clicked
    public void allowCompleteTask(final String taskID, final Context context){
        rootRef.child("workHistory").child("workingTasks").child(taskID).child("workBlocks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean allowComplete = true;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    long endTime = ds.child("endTime").getValue(long.class);
                    if(endTime == 0){
                        allowComplete = false;
                        Toast.makeText(context, "Cannot complete task", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        allowComplete = true;
                        Toast.makeText(context, "Complete task successful", Toast.LENGTH_SHORT).show();
                    }
                }
                rootRef.child("workHistory").child("workingTasks").child(taskID).child("workBlocks").removeEventListener(this);
                if (allowComplete == true){
                    completeTask(taskID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    /*
    --------------------------------------WORK BLOCKS NOTIFICATIONS / FOREGROUND SERVICE ---------------------------------------------
     */

    private void createNotification(final long timeNow, final Context context, final String taskTitle, final String projectPO, final String taskID) {

        long idLong = (timeNow % 10000000);
        final int NOTIFICATION_ID = (int)idLong;
        Intent intent;
        if(taskTitle.equals("Inspection") || taskTitle.equals("Final-Inspection")) {
            intent = new Intent(context, TaskInspectionActivity.class);
            // send the TaskInspectionActivity the projectPO
            intent.putExtra("inspectionTaskID", taskID);
        }
        else if(taskTitle.equals("Sanding") || taskTitle.equals("Sand-Blasting") || taskTitle.equals("Manual Solvent Cleaning") || taskTitle.equals("Iridite") || taskTitle.equals("Masking")) {
            intent = new Intent(context, TaskPrePaintingActivity.class);
            intent.putExtra("prepaintingTaskID", taskID);
        }
        else if(taskTitle.equals("Painting")) {
            intent = new Intent(context, TaskPaintingActivity.class);
            intent.putExtra("paintingTaskID", taskID);
        }
        else if(taskTitle.equals("Baking")) {
            intent = new Intent(context, TaskBakingActivity.class);
            intent.putExtra("bakingTaskID", taskID);
        }
        else {
            intent = new Intent(context, TaskPackagingActivity.class);
            // send the taskPackagingActivity the projectPO
            intent.putExtra("packagingTaskID", taskID);
        }

        final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        final long timeAtNotificationCreation = System.currentTimeMillis();

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setContentTitle(taskTitle + " - " + projectPO)
                .setSubText("Start Time: " + getDate(timeNow))
                .setSmallIcon(R.drawable.ic_work_block)
                .setContentIntent(pendingIntent)
                .setOngoing(true);

        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                long timeElapsed = System.currentTimeMillis() - timeAtNotificationCreation;

                long sec = (timeElapsed/1000) % 60;
                long min = ((timeElapsed/1000) / 60) % 60;
                long hour = ((timeElapsed/1000) / 60) / 60;

                builder.setContentText("Time Elapsed: " + hour+":"+min+":"+sec);

                notificationManager.notify(NOTIFICATION_ID, builder.build());
                Log.d(TAG, "updateNotification: " + NOTIFICATION_ID);

            }
        };

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                rootRef.child("users").child(uId).child("workingTasks").child(taskID).child("canEnd").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean canEnd = dataSnapshot.getValue(boolean.class);
                        if (!canEnd) {
                            removeNotification(timeNow,context);
                            timerTask.cancel();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        Timer timer = new Timer(NOTIFICATION_ID+"");
        timer.scheduleAtFixedRate(timerTask, 0, 1000);

        t.start();

//
//        final Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run()
//            {
//                // TODO do your thing
//
//                long timeElapsed = System.currentTimeMillis() - timeAtNotificationCreation;
//
//                long sec = (timeElapsed/1000) % 60;
//                long min = ((timeElapsed/1000) / 60) % 60;
//                long hour = ((timeElapsed/1000) / 60) / 60;
//
//                Notification notification = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
//                        .setContentTitle(taskTitle + " - " + projectPO)
//                        .setSubText("Start Time: " + getDate(timeNow))
//                        .setContentText("Time Elapsed: " + hour+":"+min+":"+sec)
//                        .setSmallIcon(R.drawable.ic_work_block)
//                        .setContentIntent(pendingIntent)
//                        .setOngoing(true)
//                        .build();
//                notificationManager.notify(NOTIFICATION_ID, notification);
//                Log.d(TAG, "updateNotification: " + NOTIFICATION_ID);
//
//                rootRef.child("users").child(uId).child("workingTasks").child(taskID).child("canEnd").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        boolean canEnd = dataSnapshot.getValue(boolean.class);
//                        if (!canEnd) {
//                            removeNotification(timeNow,context);
//                            timer.cancel();
//                            timer.purge();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//            }
//        }, 0, 1000);

    }

    private void removeNotification(long startTime, Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        long idLong = (startTime % 10000000);
        int NOTIFICATION_ID = (int)idLong;
        notificationManager.cancel(NOTIFICATION_ID);
        Log.d(TAG, "removeNotification: " + NOTIFICATION_ID);
    }

    private void startService(final long timeNow, final Context context, Activity activity, final String taskTitle, final String projectPO, String taskID) {
        final Intent serviceIntent = new Intent(context, NotificationForegroundService.class);
        long idLong = (timeNow % 10000000);
        final int NOTIFICATION_ID = (int)idLong;

        serviceIntent.putExtra("NOTIFICATION_ID",NOTIFICATION_ID);
        serviceIntent.putExtra("taskTitle",taskTitle);
        serviceIntent.putExtra("taskID",taskID);
        serviceIntent.putExtra("timeNow",timeNow);
        serviceIntent.putExtra("projectPO",projectPO);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
            Log.d(TAG, "startService: FOREGROUND_SERVICE");
        }
        else {
            //context.startService(serviceIntent);
            //Log.d(TAG, "startService: SERVICE");
        }

    }

    private void stopService(Context context) {
        Intent serviceIntent = new Intent(context, NotificationForegroundService.class);
        context.stopService(serviceIntent);
    }

    /*
    --------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */

    private String getDate(long time) {
        String value;

        if(time == 0) {
            value = "-";
        }
        else {
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss a dd-MM-yyyy");
            value = formatter.format(time);
        }

        return value;
    }

    private void callEmployeeWorkBlocksListViewAdapter(View view, Activity activity, List<WorkBlock> employeeWorkBlocks) {
        // instantiate the custom list adapter
        EmployeeWorkBlocksListViewAdapter adapter = new EmployeeWorkBlocksListViewAdapter(activity, employeeWorkBlocks);

        // get the ListView and attach the adapter
        ListView itemsListView  = (ListView) view.findViewById(R.id.work_blocks_list_view);
        itemsListView.setAdapter(adapter);

    }

    public static void setEmployeeTasksListViewHeightBasedOnChildren(ListView listView) {
        EmployeeTasksListViewAdapter listAdapter = (EmployeeTasksListViewAdapter) listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1) + (32 * listAdapter.getCount()));
        listView.setLayoutParams(params);
    }

    /*
    ----------------------------------------------------Complete Task Methods-------------------------------------------------------------------------
     */

    public void completeTask(String taskID) {
        rootRef.child("tasks").child(taskID).child("completed").setValue(true);
        rootRef.child("users").child(uId).child("completedTasks").child(taskID).setValue(true);
        rootRef.child("users").child(uId).child("workingTasks").child(taskID).child("completed").setValue(true);
    }

    public void setStartTimeEndTimeButtons(final Button mStartTime, final Button mEndTime, final String taskID) {
        rootRef.child("tasks").child(taskID).child("completed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rootRef.child("tasks").child(taskID).child("completed").removeEventListener(this);
                if(dataSnapshot.getValue(boolean.class)) {
                    mStartTime.setVisibility(View.GONE);
                    mEndTime.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*
    --------------------------------GRAPHABLE PROJECTS METHODS ---------------------------------------------------------------
     */

    public void populateGraphableProjects(final View view, final Activity activity, final FloatingActionButton mFab,
                                          final Dialog dialog, final String machineTitle, final boolean machineStatus, final AppCompatEditText mTitle) {
        final List<String> projectPOs = new ArrayList<>();
        rootRef.child("projects").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    projectPOs.add(ds.getKey());
                }
                callGraphableProjectsListViewAdapter(view, activity, projectPOs, mFab, dialog, machineTitle, machineStatus, mTitle);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void callGraphableProjectsListViewAdapter(View view, Activity activity, List<String> projectPOs, FloatingActionButton mFab,
                                                      Dialog dialog, String machineTitle, boolean machineStatus, AppCompatEditText mTitle) {
        GraphableProjectsListViewAdapter graphableProjectsListViewAdapter = new GraphableProjectsListViewAdapter(activity,projectPOs,mFab, dialog, machineTitle, machineStatus, mTitle);
        ListView itemsListView  = (ListView) view.findViewById(R.id.graphable_projects_list_view);
        itemsListView.setAdapter(graphableProjectsListViewAdapter);

    }

    //-----------------------------------------Populating/Fetching Graphs----------------------------------------------

    public void createGraphsForCheckedProjects(ArrayList<String> checkedProjectPOs, final String machineTitle, String graphTitle) {
        final String graphID = Task.generateRandomChars();
        final long startTime = System.currentTimeMillis();
        rootRef.child("graphs").child(graphID).child("graphTitle").setValue(graphTitle);
        rootRef.child("graphs").child(graphID).child("startTime").setValue(startTime);
        rootRef.child("graphs").child(graphID).child("graphingStatus").setValue(true);
        rootRef.child("graphs").child(graphID).child("machineTitle").setValue(machineTitle);
        rootRef.child("machines").child(machineTitle).child("graphingStatus").setValue(true);
        for (String projectPO:checkedProjectPOs) {
            rootRef.child("projects").child(projectPO).child("graphs").child(graphID).setValue(true);
        }
        rootRef.child("machines").child(machineTitle).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot machineDataSnapshot) {
                if (machineDataSnapshot.child("graphingStatus").getValue(boolean.class)) {
                    long timeNow = System.currentTimeMillis();
                    long timeStamp = (timeNow - startTime) / 1000;
                    rootRef.child("graphs").child(graphID).child("temperatures").child(Long.toString(timeStamp)).setValue(machineDataSnapshot.child("temperature").getValue());
                }
                else {
                    rootRef.child("graphs").child(graphID).child("startTime").removeEventListener(this);
                    rootRef.child("machines").child(machineTitle).removeEventListener(this);
                    rootRef.child("graphs").child(graphID).child("graphingStatus").setValue(false);
                    rootRef.child("machines").child(machineTitle).child("graphingStatus").setValue(false);
                    final long endTime = System.currentTimeMillis();
                    rootRef.child("graphs").child(graphID).child("endTime").setValue(endTime);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    public void populateGraphs(final Activity activity, final View view, String projectPO) {
//        rootRef.child("projects").child(projectPO).child("graphs").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    final List<String> graphIDs = new ArrayList<>();
//                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                        graphIDs.add(ds.getKey());
//                    }
//                    rootRef.child("graphs").addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            List<GraphData> graphs = new ArrayList<>();
//                            for (String graphID : graphIDs) {
//                                List<Float> y = new ArrayList<>();
//                                List<Float> x = new ArrayList<>();
//                                String machineTitle = dataSnapshot.child(graphID).child("machineTitle").getValue(String.class);
//                                String graphTitle =  machineTitle + " - " + dataSnapshot.child(graphID).child("graphTitle").getValue(String.class);
//                                for (DataSnapshot TempSnapshot : dataSnapshot.child(graphID).child("temperatures").getChildren()) {
//                                    String time = TempSnapshot.getKey();
//                                    int timei = parseInt(time);
//                                    x.add((float) (timei));
//                                    Float temp = TempSnapshot.getValue(float.class);
//                                    y.add(temp);
//                                }
//                                graphs.add(new GraphData(x, y, graphTitle));
//                            }
//                            callGraphListViewAdapter(activity, view, graphs);
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }


//    public void populateGraphs(final Activity activity, final View view, String projectPO, final Context context) {
//        rootRef.child("graphs").child(projectPO).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                List<GraphData> graphs = new ArrayList<>();
//                for (DataSnapshot graph:dataSnapshot.getChildren()) {
//                    List<Float> y = new ArrayList<>();
//                    List<Float> x = new ArrayList<>();
//                    String graphTitle = graph.child("graphTitle").getValue(String.class);
//                    for (DataSnapshot TempSnapshot : graph.child("temperatures").getChildren()) {
//                        String time = TempSnapshot.getKey();
//                        int timei = parseInt(time);
//                        x.add((float) (timei));
//                        Float temp = TempSnapshot.getValue(float.class);
//                        y.add(temp);
//                    }
//                    graphs.add(new GraphData(x,y,graphTitle));
//                }
//                callGraphListViewAdapter(activity, view, graphs);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    public void populateGraphNames(final FragmentActivity activity, final View view, String projectPO) {
        rootRef.child("projects").child(projectPO).child("graphs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //fetch all the graphIDs references using projectPO
                final List<String> graphIDs = new ArrayList<>();
                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                    graphIDs.add(ds.getKey());
                }
                //create string names for each graph in the listview
                rootRef.child("graphs").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        rootRef.child("graphs").removeEventListener(this);
                        List<Graph> graphs = new ArrayList<>();
                        for (String graphID:graphIDs) {
                            String graphTitle = dataSnapshot.child(graphID).child("graphTitle").getValue(String.class);
                            String machineTitle = dataSnapshot.child(graphID).child("machineTitle").getValue(String.class);
                            long startTime = dataSnapshot.child(graphID).child("startTime").getValue(long.class);
                            graphs.add(new Graph(graphTitle, machineTitle, graphID, startTime));
                        }
                        callGraphListViewAdapter(activity, view, sortGraphsByLatestCreatedFirst(graphs));
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
    //will sort 'graphs' starting with the top row being the latest created one
    private List<Graph> sortGraphsByLatestCreatedFirst(List<Graph> graphs) {
        Collections.sort(graphs,new Comparator<Graph>(){
            @Override
            public int compare(final Graph lhs, Graph rhs) {
                if (lhs.getStartTime()<rhs.getStartTime()) {
                    return 1;
                }
                else if (lhs.getStartTime()>rhs.getStartTime()) {
                    return -1;
                }
                else {
                    return 0;
                }
            }
        });
        return graphs;
    }

    public void callGraphListViewAdapter(Activity activity, View view, List<Graph> graphs) {
        GraphsListViewAdapter adapter = new GraphsListViewAdapter(activity, graphs);
        ListView itemsListView  = view.findViewById(R.id.graph_names_list_view);
        itemsListView.setAdapter(adapter);
    }

    public void stopGraphing(String machineTitle) {
        rootRef.child("machines").child(machineTitle).child("graphingStatus").setValue(false);
    }

    public void populateGraph(String graphID, final GraphView graph, final Context context) {
        rootRef.child("graphs").child(graphID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Float> y = new ArrayList<>();
                List<Float> x = new ArrayList<>();
                String graphTitle = dataSnapshot.child("graphTitle").getValue(String.class);
                for (DataSnapshot TempSnapshot : dataSnapshot.child("temperatures").getChildren()) {
                    String time = TempSnapshot.getKey();
                    int timei = parseInt(time);
                    x.add((float) (timei));
                    Float temp = TempSnapshot.getValue(float.class);
                    y.add(temp);
                }
                GraphData graphData = new GraphData(x,y,graphTitle);

                LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>();
                PointsGraphSeries<DataPoint> series2 = new PointsGraphSeries<>();
        
                for(int i = 0; i<graphData.getY().size(); i++){
                    series1.appendData(new DataPoint(graphData.getX().get(i),graphData.getY().get(i)),true,1000);
                    series2.appendData(new DataPoint(graphData.getX().get(i),graphData.getY().get(i)),true,1000);
                }
                //graphtitle
                graph.setTitle(graphData.getGraphTitle());
                //add the line graph and individual points of temps
                graph.addSeries(series1);
                graph.addSeries(series2);
                series2.setShape(PointsGraphSeries.Shape.POINT);
                series2.setSize((float)8);
                series2.setColor(Color.RED);
                //dynamically change size of axis based on max values stored in x and y
                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setYAxisBoundsManual(true);
                graph.getViewport().setMaxX(getMax(graphData.getX())+10);
                graph.getViewport().setMinX(0);
                graph.getViewport().setMaxY(getMax(graphData.getY())+10);
                graph.getViewport().setMinY(0);
        
                graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if (isValueX) {
                            // show normal x values
                            return super.formatLabel(value, isValueX);
                        } else {
                            // show currency for y values
                            return super.formatLabel(value, isValueX) + " °F";
                        }
                    }
                });
        
                //tap listener
                series2.setOnDataPointTapListener(new OnDataPointTapListener() {
                    @Override
                    public void onTap(Series series, DataPointInterface dataPoint) {
                        Toast.makeText(context, dataPoint+"", Toast.LENGTH_SHORT).show();
                    }
                });
                    
                    
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public float getMax(List<Float> list){
        float max = Integer.MIN_VALUE;
        for(int i=0; i<list.size(); i++){
            if(list.get(i) > max){
                max = list.get(i);
            }
        }
        return max;
    }

    /*
    -------------------------------------------------------ICON TO DISPLAYING IF EMPLOYEE IS WORKING CURRENTLY ON TASK---------------------------------------------
     */

    public void checkIfIconAppliesForProjectsListRow(final ImageView mIcon, final String projectPO) {
        //populate list of projects currently working on
        rootRef.child("users").child(uId).child("workingTasks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> currentlyWorkingTasks = new ArrayList<>();
                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                    if (ds.hasChild("canStart")) {
                        boolean isNotRunning = ds.child("canStart").getValue(boolean.class);
                        if (!isNotRunning) {
                            currentlyWorkingTasks.add(ds.getKey());
                        }
                    }
                }
                rootRef.child("tasks").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        rootRef.child("tasks").removeEventListener(this);
                        final List<String> currentlyWorkingProjects = new ArrayList<>();
                        for(String currentlyWorkingTask:currentlyWorkingTasks) {
                            String projectPO = dataSnapshot.child(currentlyWorkingTask).child("projectPO").getValue(String.class);
                            currentlyWorkingProjects.add(projectPO);
                        }
                        if (currentlyWorkingProjects.contains(projectPO)) {
                            mIcon.setVisibility(View.VISIBLE);
                        }
                        else {
                            mIcon.setVisibility(View.GONE);
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

    public void checkIfIconAppliesForTasksListRow(final ImageView mIcon, final String taskID) {
        //populate list of projects currently working on
        rootRef.child("users").child(uId).child("workingTasks").child(taskID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("canStart")) {
                        boolean isNotRunning = dataSnapshot.child("canStart").getValue(boolean.class);
                        if (!isNotRunning) {
                            mIcon.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void checkIfTaskStartedAlready(final String taskID, final TextView mTimeElapsed, final Activity activity, final boolean backPressed[], final View convertView, final String taskTitle) {
        rootRef.child("users").child(uId).child("workingTasks").child(taskID).child("currentWorkBlock").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rootRef.child("users").child(uId).child("workingTasks").child(taskID).child("currentWorkBlock").removeEventListener(this);
                if(dataSnapshot.exists()) {
                    final String currentWorkBlock = dataSnapshot.getValue(String.class);
                    if (!currentWorkBlock.equals("none")) {
                        rootRef.child("workHistory").child("workingTasks").child(taskID).child("workBlocks").child(currentWorkBlock).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                rootRef.child("workHistory").child("workingTasks").child(taskID).child("workBlocks").child(currentWorkBlock).removeEventListener(this);
                                final String title = dataSnapshot.child("title").getValue(String.class);
                                final long startTime = dataSnapshot.child("startTime").getValue(long.class);
                                //this checks to see if the call was made from the pre-paint listview item
                                if (taskTitle.equals(title)) {
                                    if (convertView != null) {
                                        TextView mTimeElapsed1 = convertView.findViewById(R.id.elapsed_time_since_pressed_start_time_prepaint);
                                        createUpdateTimeElapsedThread(mTimeElapsed1, activity, startTime, backPressed);
                                    } else {
                                        createUpdateTimeElapsedThread(mTimeElapsed, activity, startTime, backPressed);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createUpdateTimeElapsedThread(final TextView mTimeElapsed, final Activity activity, final long startTime, final boolean[] backPressed) {
        mTimeElapsed.setVisibility(View.VISIBLE);
        final Thread updateTimeElapsed = new Thread() {
            @Override
            public void run() {
                while (mTimeElapsed.getVisibility() == View.VISIBLE && !backPressed[0] && !Thread.currentThread().isInterrupted()) {
                    try {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                long entryTime = startTime;
                                long currentTime = System.currentTimeMillis();

                                long milliSec = currentTime - entryTime;
                                long sec = (milliSec / 1000) % 60;
                                long min = ((milliSec / 1000) / 60) % 60;
                                long hour = ((milliSec / 1000) / 60) / 60;

                                String s,m,h;
                                if (sec < 10)
                                    s = "0" + Long.toString(sec);
                                else
                                    s = Long.toString(sec);
                                if (min < 10)
                                    m = "0" + Long.toString(min);
                                else
                                    m = Long.toString(min);
                                if (hour < 10)
                                    h = "0" + Long.toString(hour);
                                else
                                    h = Long.toString(hour);

                                String elapsedTime = h + ":" + m + ":" + s;
                                mTimeElapsed.setText(elapsedTime);

                                Log.d(TAG, "run: UPDATING ELAPSED TIME: " + elapsedTime + " " + mTimeElapsed.getVisibility() + " " + backPressed[0]);

                                if (mTimeElapsed.getVisibility() == View.GONE || backPressed[0]) {
                                    mTimeElapsed.setText("");
                                    mTimeElapsed.setVisibility(View.GONE);
                                    Thread.currentThread().interrupt();
                                }
                            }
                        });
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                if (mTimeElapsed.getVisibility() == View.GONE || backPressed[0]) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTimeElapsed.setText("");
                            mTimeElapsed.setVisibility(View.GONE);
                            Thread.currentThread().interrupt();
                        }
                    });
                }

            }
        };
        updateTimeElapsed.start();
    }

    /*
    -------------------------------------------------------------------STATISTICS GRAPH--------------------------------------------------------------------------
     */

    public void populateStatisticsGraph(final String projectPO, final BarChart statisticsGraph, final TextView mTotalTime) {
        rootRef.child("projects").child(projectPO).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rootRef.child("projects").child(projectPO).removeEventListener(this);
                if (dataSnapshot.hasChild("completed")) {
                    boolean completed = dataSnapshot.child("completed").getValue(boolean.class);
                    if (completed) {
                        final List<String> taskIDs = new ArrayList<>();
                        for(DataSnapshot ds:dataSnapshot.child("tasks").getChildren()) {
                            taskIDs.add(ds.getKey());
                        }
                        rootRef.child("tasks").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                rootRef.child("tasks").removeEventListener(this);
                                String graphTitle = projectPO;
                                final ArrayList<BarEntry> entries = new ArrayList<>();
                                final ArrayList<String> xLabels = new ArrayList<>();
                                int i = 0;
                                long totalTime = 0;
                                for (String taskID:taskIDs) {
                                    long taskTimeLong;
                                    if (dataSnapshot.child(taskID).child("totalTime").exists())
                                        taskTimeLong = dataSnapshot.child(taskID).child("totalTime").getValue(long.class);
                                    else
                                        taskTimeLong = 0;
                                    totalTime += taskTimeLong;
                                    float taskTime = (float) TimeUnit.MILLISECONDS.toHours(taskTimeLong);
                                    String taskType = dataSnapshot.child(taskID).child("taskType").getValue(String.class);
                                    Log.d(TAG, "fetching: " + taskID + " " + taskType + " " + taskTime);
                                    entries.add(new BarEntry(i, taskTime));
                                    xLabels.add(taskType);
                                    i++;
                                }
                                
                                long sec = (totalTime/1000) % 60;
                                long min = ((totalTime/1000) / 60) % 60;
                                long hour = ((totalTime/1000) / 60) / 60;
                                mTotalTime.setText("Total Time: " + hour+"h"+min+"m"+sec+"s");

                                BarDataSet dataSet = new BarDataSet(entries,"Task Time in hours");

                                BarData data = new BarData(dataSet);
                                statisticsGraph.setData(data);
                                statisticsGraph.getLegend().setEnabled(true);
                                statisticsGraph.getDescription().setEnabled(false);
                                statisticsGraph.animateXY(2000, 2000);
                                statisticsGraph.setNoDataText("");

                                XAxis xAxis = statisticsGraph.getXAxis();
                                xAxis.setGranularity(1f);
                                xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));
                                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                                statisticsGraph.invalidate();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    else {
                        Log.d(TAG, "onDataChange: HEREEEEE ");
                        mTotalTime.setVisibility(View.GONE);
                        statisticsGraph.setVisibility(View.GONE);
                    }
                }
                else {
                    Log.d(TAG, "onDataChange: HEREEEEE ");
                    mTotalTime.setVisibility(View.GONE);
                    statisticsGraph.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



}