package com.example.a390project.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.a390project.DummyDatabase;
import com.example.a390project.ListViewAdapters.ProjectListViewAdapter;
import com.example.a390project.Model.Project;
import com.example.a390project.R;

import java.util.List;

public class ProjectFragment extends Fragment {

    private String TAG = "ProjectFragment";
    private List<Project> projects;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.project_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        DummyDatabase db = new DummyDatabase();
        projects = db.generateDummyProjects(10);

        callListViewAdapter(view, projects);


    }
    private void callListViewAdapter(View view, List<Project> projects){
        ProjectListViewAdapter adapter = new ProjectListViewAdapter(view.getContext(), projects);
        ListView itemsListView  = (ListView) view.findViewById(R.id.project_list_view);
        itemsListView.setAdapter(adapter);
    }
}
