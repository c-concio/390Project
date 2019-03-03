package com.example.a390project.ListViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a390project.Model.Project;
import com.example.a390project.R;

import java.util.List;

public class ProjectListViewAdapter extends BaseAdapter {

    private Context context;
    private List<Project> Projects;

    private TextView ProjectTitle;
    private TextView PO;

    public ProjectListViewAdapter(Context cont, List<Project> projects){
        context=cont;
        Projects=projects;
    }

    @Override
    public int getCount() {
        return Projects.size();
    }

    @Override
    public Object getItem(int position) {
        return Projects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_item_project, parent, false);
        }
        ProjectTitle = convertView.findViewById(R.id.ProjectTitleTV);
        PO = convertView.findViewById(R.id.POTV);

        ProjectTitle.setText(Projects.get(position).getTitle()+"\n");
        PO.setText(Projects.get(position).getPO() + "\n");

        return convertView;
    }
}
