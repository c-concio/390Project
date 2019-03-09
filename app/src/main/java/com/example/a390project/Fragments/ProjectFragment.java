package com.example.a390project.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

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

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.project_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        mProgressbar = view.findViewById(R.id.progress_bar_projects);

        FirebaseHelper firebaseHelper = new FirebaseHelper();
        //populate all projects from firebase to listview
        mProgressbar.setVisibility(View.VISIBLE);
        firebaseHelper.populateProjects(view, getActivity(), mProgressbar);
    }
}
