package com.example.a390project.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.a390project.DummyDatabase;
import com.example.a390project.EmployeeActivity;
import com.example.a390project.ListViewAdapters.ProjectListViewAdapter;
import com.example.a390project.Model.Project;
import com.example.a390project.ProjectActivity;
import com.example.a390project.R;

import java.util.List;

public class ProjectFragment extends Fragment {
    private View view;
    private List<Project> Projects;
    private ListView ProjectListView;


    private String TAG = "pexception";

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.project_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.view = view;

        ProjectListView = view.findViewById(R.id.ProjectListView);
        DummyDatabase db = new DummyDatabase();
        Projects = db.generateDummyProjects(10);


        ProjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                try {
                    Intent intent = new Intent(view.getContext(), ProjectActivity.class);
                    intent.putExtra("projectID", Projects.get(position).getID());
                    startActivity(intent);
                }catch (Exception e){
                    Log.d("exepf","exception " + e);
                }
            }
        });
        SetListView();


    }
    private void SetListView(){
        ProjectListViewAdapter projectAdapter = new ProjectListViewAdapter(view.getContext(), Projects);
        ProjectListView.setAdapter(projectAdapter);
    }
}
