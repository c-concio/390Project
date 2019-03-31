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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.a390project.FirebaseHelper;
import com.example.a390project.Model.Material;
import com.example.a390project.R;

public class CreateInventoryDialogFragment extends DialogFragment {

    //views
    private Spinner mPaintType;
    private AppCompatEditText mPaintDescription, mPaintCode, mPaintBakeTemperature, mPaintBakeTime, mPaintWeight;
    private FloatingActionButton mFab;
    private AppCompatEditText materialNameEditText, materialDescriptionEditText, materialQuantityEditText;

    // linearLayouts
    private LinearLayout paintLinearLayout;
    private LinearLayout materialLinearLayout;

    //variables
    private String paintType, paintCode, paintDescription;
    private int paintBakeTemperature, paintBakeTime;
    private float paintWeight;

    private String materialName, materialDescription;
    private float materialQuantity;

    private static final String TAG = "CreateInventoryDialogFr";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_inventory_dialog_fragment, container, false);

        mPaintType = view.findViewById(R.id.paint_type_inventory_df);
        mPaintCode = view.findViewById(R.id.paint_code_inventory_df);
        mPaintDescription = view.findViewById(R.id.paint_description_inventory_df);
        mPaintBakeTemperature = view.findViewById(R.id.bake_temperature_inventory_df);
        mPaintBakeTime = view.findViewById(R.id.bake_time_inventory_df);
        mPaintWeight = view.findViewById(R.id.paint_weight_inventory_df);
        mFab = view.findViewById(R.id.fab_inventory_dialog_fragment);

        // material view initialization
        materialNameEditText = view.findViewById(R.id.materialNameEditText);
        materialDescriptionEditText = view.findViewById(R.id.materialDescriptionEditText);
        materialQuantityEditText = view.findViewById(R.id.materialQuantityEditText);

        // linearLayout initialization
        paintLinearLayout = view.findViewById(R.id.paintDetailsLinearLayout);
        materialLinearLayout = view.findViewById(R.id.materialLinearLayout);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.paint_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPaintType.setAdapter(adapter);
        mPaintType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                paintType = adapterView.getItemAtPosition(i).toString().trim().toLowerCase();

                // check what type of category is selected
                // if the chosen field is liquid or powder paint, then the paint details are shown
                // else if the chosen field is materials, then the material details are shown

                Log.d(TAG, "onItemSelected: paintType " + paintType);
                if (paintType.equals("liquid") || paintType.equals("powder")){
                    materialLinearLayout.setVisibility(View.GONE);
                    paintLinearLayout.setVisibility(View.VISIBLE);
                }
                else if (paintType.equals("material")){
                    paintLinearLayout.setVisibility(View.GONE);
                    materialLinearLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // if paintType is a type of paint, check paint fields and save to firebase
                if (paintType.equals("liquid") || paintType.equals("powder")) {
                    paintCode = mPaintCode.getText().toString().trim();
                    paintDescription = mPaintDescription.getText().toString().trim();
                    String temp = mPaintBakeTemperature.getText().toString().trim();
                    String time = mPaintBakeTime.getText().toString().trim();
                    String weight = mPaintWeight.getText().toString().trim();
                    if (!paintType.isEmpty() && !paintCode.isEmpty() && !paintDescription.isEmpty() && !temp.isEmpty() && !time.isEmpty() && !weight.isEmpty()) {
                        Toast.makeText(getContext(), paintCode + " Created!", Toast.LENGTH_SHORT).show();
                        paintBakeTemperature = Integer.parseInt(temp);
                        paintBakeTime = Integer.parseInt(time);
                        paintWeight = Float.parseFloat(weight);

                        FirebaseHelper firebaseHelper = new FirebaseHelper();
                        firebaseHelper.createInventoryPaintItem(paintType, paintCode, paintDescription, paintBakeTemperature, paintBakeTime, paintWeight);

                    }
                }
                // if paintType is material, check fields and save to firebase
                else{
                    materialName = materialNameEditText.getText().toString().trim();
                    materialDescription = materialDescriptionEditText.getText().toString().trim();
                    String materialQuantityString = materialQuantityEditText.getText().toString().trim();

                    Log.d(TAG, "onClick: material " + materialQuantityString);
                    if (!materialName.isEmpty() && !materialDescription.isEmpty() && !materialQuantityString.isEmpty()){
                        Toast.makeText(getContext(), "Material Added", Toast.LENGTH_SHORT).show();
                        materialQuantity = Float.parseFloat(materialQuantityString);

                        Material newMaterial = new Material(materialDescription, materialQuantity);
                        FirebaseHelper firebaseHelper = new FirebaseHelper();
                        firebaseHelper.createInventoryMaterialItem(materialName,newMaterial);
                    }
                }
            }
        });

        return view;
    }
}
