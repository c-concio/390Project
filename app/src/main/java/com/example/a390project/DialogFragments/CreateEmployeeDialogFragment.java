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

// dialog fragment to add a new employee
public class CreateEmployeeDialogFragment extends DialogFragment {

    // fragment widgets
    EditText usernameEditText;
    EditText passwordEditText;
    Button addEmployeeButton;

    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.create_employee_dialog_fragment, container, false);
        setupUI(view);

        // add functionality to addEmployeeButton: creates a new employee with the username and password given
        addEmployeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // if no username of password was entered
                if(username.isEmpty()){
                    usernameEditText.setError("Please enter a username");
                }
                if(password.isEmpty()){
                    passwordEditText.setError("Please enter a password");
                }

                // add employee into database
                if(!username.isEmpty() && !password.isEmpty()){
                    DummyDatabase dd = new DummyDatabase();
                    dd.addEmployee(username);

                    Toast toast = Toast.makeText(view.getContext(), "Employee account added", Toast.LENGTH_SHORT);
                    toast.show();
                    getDialog().dismiss();

                }
            }
        });


        return view;
    }

    public void onDestroyView() {
        super.onDestroyView();

    }

    private void setupUI(View view){
        usernameEditText = view.findViewById(R.id.userNameEditView);
        passwordEditText = view.findViewById(R.id.passwordEditView);
        addEmployeeButton = view.findViewById(R.id.addEmployeeButton);
    }

}
