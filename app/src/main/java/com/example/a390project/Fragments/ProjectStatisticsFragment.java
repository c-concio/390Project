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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.jjoe64.graphview.GraphView;

@SuppressLint("ValidFragment")
public class ProjectStatisticsFragment extends Fragment {

    //variables
    private String projectPO;
    //views
    private BarChart statisticsGraph;
    private TextView mTotalTime;

    public ProjectStatisticsFragment(String projectPO) {
        this.projectPO = projectPO;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.project_statistics_fragment, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        statisticsGraph = view.findViewById(R.id.graph_v_statistics);
        mTotalTime = view.findViewById(R.id.total_time_statistics);

        FirebaseHelper firebaseHelper = new FirebaseHelper();
        firebaseHelper.populateStatisticsGraph(projectPO, statisticsGraph, mTotalTime);

    }
}
