package com.example.a390project.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.a390project.DummyDatabase;
import com.example.a390project.FirebaseHelper;
import com.example.a390project.ListViewAdapters.ProjectListViewAdapter;
import com.example.a390project.Model.Project;
import com.example.a390project.R;

import java.util.ArrayList;
import java.util.List;

public class ProjectFragment extends Fragment {

    private String TAG = "ProjectFragment";
    private ProgressBar mProgressbar;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButtonDue;
    private RadioButton mRadioButtonStart;
    private View mView = null;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.project_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mView = view;
        mProgressbar = view.findViewById(R.id.progress_bar_projects);
        mRadioGroup = view.findViewById(R.id.radio_group_projects);
        mRadioButtonDue = view.findViewById(R.id.radio_button_due);
        mRadioButtonStart = view.findViewById(R.id.radio_button_start);

        final FirebaseHelper firebaseHelper = new FirebaseHelper();
        //populate all projects from firebase to listview
        mProgressbar.setVisibility(View.VISIBLE);
        firebaseHelper.populateProjects(view, getActivity(), mProgressbar,false, true);
        mRadioButtonStart.setChecked(true);

        mRadioButtonDue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    firebaseHelper.populateProjects(mView, getActivity(), mProgressbar,true, false);
                    Toast.makeText(getActivity(), "Projects sorted by " + buttonView.getText() + " first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mRadioButtonStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    firebaseHelper.populateProjects(mView, getActivity(), mProgressbar,false, true);
                    Toast.makeText(getActivity(), "Projects sorted by " + buttonView.getText() + " first", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
