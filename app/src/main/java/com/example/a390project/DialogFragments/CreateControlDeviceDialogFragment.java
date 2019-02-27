package com.example.a390project.DialogFragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a390project.DummyDatabase;
import com.example.a390project.R;

public class CreateControlDeviceDialogFragment extends DialogFragment {

    EditText control_device_edit_text;
    Button control_device_add_button;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.create_control_device_dialog_fragment, container, false);
        setupUI(view);

        control_device_add_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String name = control_device_edit_text.getText().toString();

                if(name.isEmpty()){
                    control_device_edit_text.setError("Please enter a device name");
                }

                if(!name.isEmpty()){
                    DummyDatabase dd = new DummyDatabase();
                    dd.addControlDevice(name);


                    Toast toast = Toast.makeText(view.getContext(), "Device added", Toast.LENGTH_SHORT);
                    toast.show();
                    getDialog().dismiss();

                }
            }
        });

        return view;
    }


    public void onDestroyView(){
        super.onDestroyView();
    }

    private void setupUI(View view){
        control_device_edit_text = view.findViewById(R.id.control_device_edit_text);
        control_device_add_button = view.findViewById(R.id.control_device_add_button);
    }
}
