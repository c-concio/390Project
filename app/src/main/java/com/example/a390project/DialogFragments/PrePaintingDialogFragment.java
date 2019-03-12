package com.example.a390project.DialogFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.a390project.FirebaseHelper;
import com.example.a390project.Model.SubTask;
import com.example.a390project.Model.Task;
import com.example.a390project.R;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class PrePaintingDialogFragment extends DialogFragment {

    //views
    private CheckBox mSandBlasting, mSanding, mManualSolventCleaning, mIridite, mMasking;
    private FloatingActionButton mFab;
    //variables
    private String projectPO;
    private String taskDescription;
    private List<SubTask> subTasks = new ArrayList<>();

    public PrePaintingDialogFragment(String projectPO, String taskDescription) {
        this.projectPO = projectPO;
        this.taskDescription = taskDescription;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prepainting_dialog_fragment, container, false);

        mSandBlasting = view.findViewById(R.id.sandblasting_checkbox);
        mSanding = view.findViewById(R.id.sanding_checkbox);
        mManualSolventCleaning = view.findViewById(R.id.manual_solvent_cleaning_checkbox);
        mIridite = view.findViewById(R.id.iridite_checkbox);
        mMasking = view.findViewById(R.id.masking_checkbox);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskID = Task.generateRandomChars();
                long createdTime = System.currentTimeMillis();
                //public SubTask(String subTaskType, String projectID, String taskID, boolean isCompleted, long createdTime)

                if (mSandBlasting.isChecked()){
                    String subTaskID = Task.generateRandomChars();
                    subTasks.add(new SubTask("SandBlasting", subTaskID, projectPO, taskID,false, createdTime));
                }

                if (mSanding.isChecked()){
                    String subTaskID = Task.generateRandomChars();
                    subTasks.add(new SubTask("Sanding", subTaskID, projectPO, taskID,false, createdTime));
                }

                if (mManualSolventCleaning.isChecked()) {
                    String subTaskID = Task.generateRandomChars();
                    subTasks.add(new SubTask("ManualSolventCleaning", subTaskID, projectPO, taskID,false, createdTime));
                }

                if (mIridite.isChecked()) {
                    String subTaskID = Task.generateRandomChars();
                    subTasks.add(new SubTask("Iridite", subTaskID, projectPO, taskID,false, createdTime));
                }

                if (mMasking.isChecked()) {
                    String subTaskID = Task.generateRandomChars();
                    subTasks.add(new SubTask("Masking", subTaskID, projectPO, taskID,false, createdTime));
                }

                FirebaseHelper firebaseHelper = new FirebaseHelper();
                firebaseHelper.createPrepaintingTask(taskID, projectPO,"PrePainting", taskDescription, createdTime, subTasks);

            }
        });

        return view;
    }
}
