package com.example.a390project.DialogFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.a390project.FirebaseHelper;
import com.example.a390project.R;
import com.google.firebase.database.FirebaseDatabase;

public class CompleteProjectDialogFragment extends DialogFragment {

    private TextView outputTextView;
    private Button yesButton;
    private Button noButton;
    private Button returnButton;
    Boolean completedProject;
    Boolean completedTasks;
    private FirebaseHelper firebaseHelper;
    private String projectPO;

    private static final String TAG = "CompleteProjectDialogFr";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseHelper = new FirebaseHelper();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.complete_project_dialog_fragment, container, false);
        setupUI(view);

        Log.d(TAG, "onCreateView: started dialog");



        return view;
    }


    private void setupUI(View view){
        TextView outputTextView = view.findViewById(R.id.outputTextView);
        Button yesButton = view.findViewById(R.id.yesButton);
        Button noButton = view.findViewById(R.id.noButton);
        Button returnButton = view.findViewById(R.id.returnButton);

        Log.d(TAG, "setupUI: completedTasks is " + completedTasks);
        Log.d(TAG, "setupUI: completedProject is " + completedProject);

        if (completedTasks && !completedProject){
            outputTextView.setText(getString(R.string.completeProjectHeader));
            yesButton.setVisibility(View.VISIBLE);
            noButton.setVisibility(View.VISIBLE);
            returnButton.setVisibility(View.GONE);

            yesButton.setOnClickListener(yesOnClickListener);
            noButton.setOnClickListener(noOnClickListener);
        }
        else if (completedTasks && completedProject){
            outputTextView.setText(getString(R.string.alreadyCompletedHeader));
            yesButton.setVisibility(View.GONE);
            noButton.setVisibility(View.GONE);
            returnButton.setVisibility(View.VISIBLE);

            returnButton.setOnClickListener(returnOnClickListener);
        }

        else{
            outputTextView.setText(getString(R.string.completeProjectReturnHeader));
            yesButton.setVisibility(View.GONE);
            noButton.setVisibility(View.GONE);
            returnButton.setVisibility(View.VISIBLE);

            returnButton.setOnClickListener(returnOnClickListener);
        }
    }


    View.OnClickListener returnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getDialog().dismiss();
        }
    };

    View.OnClickListener yesOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // calculate the total time for each task and output them onto the task activity
            firebaseHelper.calculateTotalTimes(projectPO);

            getDialog().dismiss();
        }
    };

    View.OnClickListener noOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getDialog().dismiss();
        }
    };


    public void setCompletedProject(Boolean completedProject) {
        this.completedProject = completedProject;
    }

    public void setProjectPO(String projectPO) {
        this.projectPO = projectPO;
    }


    public void setCompletedTasks(Boolean completedTasks) {
        this.completedTasks = completedTasks;
    }
}
