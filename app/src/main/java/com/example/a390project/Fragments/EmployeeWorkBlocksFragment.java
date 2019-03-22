package com.example.a390project.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a390project.FirebaseHelper;
import com.example.a390project.R;

@SuppressLint("ValidFragment")
public class EmployeeWorkBlocksFragment extends Fragment {
    //variables
    private String employeeID;
    private FirebaseHelper firebaseHelper = new FirebaseHelper();
    private View mView;

    public EmployeeWorkBlocksFragment(String employeeID) {
        this.employeeID = employeeID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.employee_work_blocks_fragment, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mView = view;
        firebaseHelper.populateWorkBlocksForEmployee(employeeID, view, getActivity());
    }

    @Override
    public void onResume() {
        firebaseHelper.populateWorkBlocksForEmployee(employeeID, mView, getActivity());
        super.onResume();
    }
}
