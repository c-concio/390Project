package com.example.a390project.DialogFragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a390project.FirebaseHelper;
import com.example.a390project.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateProjectDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private AppCompatEditText mPO;
    private AppCompatEditText mTitle;
    private AppCompatEditText mClient;
    private TextView mStartText;
    private TextView mDueText;
    private Button mStartButton;
    private Button mDueButton;
    private FloatingActionButton mFabCreateProject;

    private static char when;
    private long startDate;
    private long dueDate;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_project_dialog_fragment, container, false);

        mPO = view.findViewById(R.id.project_PO_edit_text);
        mTitle = view.findViewById(R.id.project_title_edit_text);
        mClient = view.findViewById(R.id.project_client_edit_text);
        mStartText = view.findViewById(R.id.startdate_textview);
        mDueText = view.findViewById(R.id.duedate_project_textview);
        mStartButton = view.findViewById(R.id.startdate_project_dialog_fragment);
        mDueButton = view.findViewById(R.id.duedate_project_dialog_fragment);
        mFabCreateProject = view.findViewById(R.id.fab_create_project);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                when = 's';
                DialogFragment newFragment = new DatePickerFragment(CreateProjectDialogFragment.this);
                newFragment.show(getFragmentManager(), "datePickerStartTime");
            }
        });

        mDueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                when = 'd';
                DialogFragment newFragment = new DatePickerFragment(CreateProjectDialogFragment.this);
                newFragment.show(getFragmentManager(), "datePickerStartTime");
            }
        });

        mFabCreateProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String po = mPO.getText().toString().trim();
                String title = mTitle.getText().toString().trim();
                String client = mClient.getText().toString().trim();

                if (!po.isEmpty() && !client.isEmpty() && !title.isEmpty() && !mStartText.equals("-") && !mDueText.equals("-")) {
                    if (startDate < dueDate) {
                        FirebaseHelper firebaseHelper = new FirebaseHelper();
                        firebaseHelper.createProject(po, title, client, startDate, dueDate);
                        getDialog().dismiss();
                    }
                    else {
                        Toast.makeText(getActivity(), "Due-date cannot be before Start-date", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Invalid Inputs", Toast.LENGTH_SHORT).show();
                }
            }
        });



        return view;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        //Toast.makeText(getActivity(), "Start Date: " + day + "-" + month + "-" + year, Toast.LENGTH_SHORT).show();
        String str_date = day + "-" + (month+1) + "-" + year;
        String str_date_format = day + "-" + (month+1) + "-" + year;
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            if (when == 's') {
                mStartText.setText(str_date);
                Date date = formatter.parse(str_date_format);
                startDate = date.getTime();
            }
            else if (when == 'd') {
                mDueText.setText(str_date);
                Date date = formatter.parse(str_date_format);
                dueDate = date.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }



}
