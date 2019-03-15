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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.a390project.FirebaseHelper;
import com.example.a390project.Model.Task;
import com.example.a390project.R;

@SuppressLint("ValidFragment")
class PaintingDialogFragment extends DialogFragment {

    //views
    private Spinner mPaintType, mPaintCode;
    private TextView mPaintDescription, mPaintBakeTemperature, mPaintBakeTime, mPaintWeight;
    private FloatingActionButton mFab;

    //variables
    private String projectPO;
    private String taskDescription;
    private String paintType;
    private String paintCode;

    public PaintingDialogFragment(String projectPO, String taskDescription) {
        this.projectPO = projectPO;
        this.taskDescription = taskDescription;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.painting_dialog_fragment, container, false);

        mPaintType = view.findViewById(R.id.paint_type_painting_task);
        mPaintCode = view.findViewById(R.id.paint_code_painting_task);
        mPaintDescription = view.findViewById(R.id.paint_description_painting_task);
        mPaintBakeTemperature = view.findViewById(R.id.bake_temperature_painting_task);
        mPaintBakeTime = view.findViewById(R.id.bake_time_painting_task);
        mPaintWeight = view.findViewById(R.id.paint_weight_painting_task);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.paint_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPaintType.setAdapter(adapter);
        mPaintType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                paintType = adapterView.getItemAtPosition(i).toString().trim();
                ArrayAdapter<CharSequence> adapter;
                if (paintType.equals("Liquid")) {
                     adapter = ArrayAdapter.createFromResource(getContext(), R.array.paint_liquid, android.R.layout.simple_spinner_item);
                }
                else {
                    adapter = ArrayAdapter.createFromResource(getContext(), R.array.paint_powder, android.R.layout.simple_spinner_item);
                }
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mPaintCode.setAdapter(adapter);
                mPaintCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        paintCode = adapterView.getItemAtPosition(i).toString().trim();
                        //populate fields of painting characteristics
                        FirebaseHelper firebaseHelper = new FirebaseHelper();
                        firebaseHelper.populatePaintingTaskCharacteristics(paintCode, mPaintDescription, mPaintBakeTemperature, mPaintBakeTime, mPaintWeight);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!paintCode.isEmpty() && !paintType.isEmpty()) {
                    FirebaseHelper firebaseHelper = new FirebaseHelper();
                    String taskID = Task.generateRandomChars();
                    long createdTime = System.currentTimeMillis();
                    firebaseHelper.createPaintingTask(projectPO, taskID, taskDescription, createdTime, paintCode);
                }
            }
        });

        return view;
    }
}
