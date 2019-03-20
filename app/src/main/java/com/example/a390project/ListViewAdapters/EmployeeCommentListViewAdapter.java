package com.example.a390project.ListViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a390project.Model.EmployeeComment;
import com.example.a390project.R;

import java.util.List;

public class EmployeeCommentListViewAdapter extends BaseAdapter {

    Context context;
    List<EmployeeComment> employeeComments;

    public EmployeeCommentListViewAdapter(Context context, List<EmployeeComment> employeeComments){
        this.context = context;
        this.employeeComments = employeeComments;
    }

    @Override
    public int getCount() {
        return employeeComments.size();
    }

    @Override
    public Object getItem(int position) {
        return employeeComments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView ==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.row_item_employee_comment, parent, false);
        }

        // views initialization
        TextView usernameTextView = convertView.findViewById(R.id.usernameTextView);
        TextView createdDateTextView = convertView.findViewById(R.id.createdDateTextView);
        TextView commentTextView = convertView.findViewById(R.id.commentTextView);

        EmployeeComment currentComment = employeeComments.get(position);

        usernameTextView.setText(currentComment.getUsername());
        createdDateTextView.setText(String.valueOf(currentComment.getDate()));
        commentTextView.setText(currentComment.getComment());

        return convertView;
    }
}
