package com.example.a390project.DialogFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.a390project.FirebaseHelper;
import com.example.a390project.R;
import com.google.firebase.database.FirebaseDatabase;

public class DeleteTaskDialogFragment extends DialogFragment {

    private Button Yesbutton;
    private Button Nobutton;
    private String TaskID;

    public DeleteTaskDialogFragment(String taskid){
        TaskID = taskid;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delete_dialog_fragment, container, false);
        Yesbutton = view.findViewById(R.id.Yes_button);
        Nobutton = view.findViewById(R.id.No_button);

        Yesbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseHelper FBHelper = new FirebaseHelper();
                FBHelper.deleteTask(TaskID);
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
