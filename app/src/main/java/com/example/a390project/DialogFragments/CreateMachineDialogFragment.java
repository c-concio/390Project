package com.example.a390project.DialogFragments;

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
import android.widget.Toast;

import com.example.a390project.FirebaseHelper;
import com.example.a390project.R;

public class CreateMachineDialogFragment extends DialogFragment {

    private static final String TAG = "CreateMachineDF";
    //views
    private AppCompatEditText mTtitle;
    private FloatingActionButton mFabCreate;
    private FirebaseHelper machine_db;// database helper class

    //variables
    String machineTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_machine_dialog_fragment, container, false);

        mTtitle = view.findViewById(R.id.machine_title_edit_text);
        mFabCreate = view.findViewById(R.id.fab_create_machine);
        machine_db = new FirebaseHelper();//database helper class helps connect to fire base

        mFabCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if editText is empty or not
                machineTitle = mTtitle.getText().toString().trim();
                if (!machineTitle.isEmpty()) {
                    machine_db.add_machine(machineTitle);// add machine to farebase using add_machine function of databse helper
                    Log.d(TAG, machineTitle + " created!");
                    Toast.makeText(getContext(), machineTitle + " created!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "Invalid Machine Title...", Toast.LENGTH_SHORT).show();
                }
                getDialog().dismiss();
            }
        });
        return view;
    }
}
