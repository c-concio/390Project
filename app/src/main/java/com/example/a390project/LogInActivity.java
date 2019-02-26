package com.example.a390project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a390project.Model.Employee;

import java.util.List;

public class LogInActivity extends AppCompatActivity {
    private List<Employee> employees;
    private EditText UsernameTV;
    private EditText PasswordTV;
    private Button LogInButton;
    String User;
    String Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        UsernameTV = findViewById(R.id.UserNameTV);
        PasswordTV = findViewById(R.id.UserPasswordTV);
        LogInButton = findViewById(R.id.LogInButton);
        DummyDatabase DBhelper = new DummyDatabase();//to be changed to databese helper
        employees = DBhelper.generateDummyEmployees(5);//to be changed to database helper

        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User = UsernameTV.getText().toString();
                Password = PasswordTV.getText().toString();
                Check_Log_In(User,Password,employees);
            }
        });
    }
    void Check_Log_In(String Uname, String Pass, List<Employee> E){
        int id = 0;

        for(int i = 0; i<E.size();i++){
            if((E.get(i).getEmployeeName().equals(Uname))&&(E.get(i).getEmployeePassword().equals(Pass))){
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                }
                id = i;
        }
        Toast.makeText(this, "Employee name or password incorrect" + id, Toast.LENGTH_LONG).show();
    }
}
