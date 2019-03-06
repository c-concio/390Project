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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.a390project.FirebaseHelper;
import com.example.a390project.Model.Machine;
import com.example.a390project.Model.Oven;
import com.example.a390project.Model.Paintbooth;
import com.example.a390project.R;

public class CreateMachineDialogFragment extends DialogFragment {

    private static final String TAG = "CreateMachineDF";
    //views
    private AppCompatEditText mTtitle;
    private Spinner mSpinner;
    private FloatingActionButton mFabCreate;
    //variables
    private FirebaseHelper firebaseHelper;// database helper class

    //variables
    String machineTitle;
    private String machineType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_machine_dialog_fragment, container, false);

        mTtitle = view.findViewById(R.id.machine_title_edit_text);
        mSpinner = view.findViewById(R.id.machine_type_spinner);
        mFabCreate = view.findViewById(R.id.fab_create_machine);

        firebaseHelper = new FirebaseHelper();//database helper class helps connect to fire base

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.machine_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                machineType = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getContext(), machineType, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mFabCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if editText is empty or not
                machineTitle = mTtitle.getText().toString().trim();
                if (!machineTitle.isEmpty()) {
                    if (machineType.equals("Oven")) {
                        firebaseHelper.add_machine(new Oven(machineTitle,"None",false,0,machineType,
                                0, 0));
                    }
                    else if (machineType.equals("PaintBooth")) {
                        firebaseHelper.add_machine(new Paintbooth(machineTitle, "None", false, 0, machineType,
                                0));
                    }
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
