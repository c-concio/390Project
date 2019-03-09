package com.example.a390project.DialogFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.a390project.FirebaseHelper;
import com.example.a390project.ProjectActivity;
import com.example.a390project.R;

public class CreateTaskDialogFragment extends DialogFragment {

    private static final String TAG = "CreateTaskDailogF";

    private Spinner mSpinner;
    private AppCompatEditText mDescription;
    private FloatingActionButton mFab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_task_dialog_fragment, container, false);

        mSpinner = view.findViewById(R.id.task_spinner);
        mDescription = view.findViewById(R.id.task_description);
        mFab = view.findViewById(R.id.fab_task_dialog_fragment);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.task_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final String taskType = adapterView.getItemAtPosition(i).toString().trim();
                final String projectPO = ((ProjectActivity)getActivity()).getProjectPO();
                Log.d(TAG, "Task selected: " + taskType);

                if (taskType.equals("Inspection") || taskType.equals("Baking") ||
                        taskType.equals("Packaging") || taskType.equals("Final-Inspection")) {
                    mFab.setImageResource(R.drawable.plus_sign);
                    mFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String taskDescription = mDescription.getText().toString().trim();
                            if (!taskDescription.isEmpty()) {
                                FirebaseHelper firebaseHelper = new FirebaseHelper();
                                firebaseHelper.createTask(taskType, taskDescription, projectPO);
                                getDialog().dismiss();
                            }
                            else {
                                Toast.makeText(getActivity(), "Enter task description...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else if (taskType.equals("Pre-Painting")) {
                    mFab.setImageResource(R.drawable.next_sign);
                    mFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Open 'pre-painting dialog fragment' and follow the design:
                            //https://drive.google.com/file/d/1TIN4Cvezqyr3FZDBwEYyEbq7wecrdxFb/view?usp=sharing
                        }
                    });
                }
                else if (taskType.equals("Painting")) {
                    mFab.setImageResource(R.drawable.next_sign);
                    mFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Open 'painting dialog fragment' and follow the design:
                            //https://drive.google.com/file/d/1TIN4Cvezqyr3FZDBwEYyEbq7wecrdxFb/view?usp=sharing
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        return view;
    }
}
