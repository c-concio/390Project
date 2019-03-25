package com.example.a390project.DialogFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.a390project.FirebaseHelper;
import com.example.a390project.R;

@SuppressLint("ValidFragment")
public class DeleteTaskDialogFragment extends DialogFragment {

    private String projectPO;
    private Button Yesbutton;
    private Button Nobutton;
    private String taskID;



    public DeleteTaskDialogFragment(String taskID, String projectPO) {
        this.taskID = taskID;
        this.projectPO = projectPO;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delete_dialog_fragment, container, false);
        Yesbutton = view.findViewById(R.id.Yes_button);
        Nobutton = view.findViewById(R.id.No_button);

        Yesbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseHelper FBHelper = new FirebaseHelper();
                FBHelper.deleteTask(taskID, projectPO);
                getDialog().dismiss();
            }
        });
        Nobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return view;

    }
}
