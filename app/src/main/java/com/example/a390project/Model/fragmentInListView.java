package com.example.a390project.Model;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.a390project.DialogFragments.DeleteTaskDialogFragment;
import com.example.a390project.ProjectActivity;

import java.lang.reflect.Field;

public class fragmentInListView extends AppCompatActivity {

    String TAG = "deletetask";
    private String taskID;
    private String projectPO;

    public fragmentInListView(String taskID, String projectPO){
        this.taskID = taskID;
        this.projectPO = projectPO;
    }

    public void deletetaskfragment(Context context) {
        DeleteTaskDialogFragment dialog = new DeleteTaskDialogFragment(taskID, projectPO);
        dialog.show(((ProjectActivity) context).getSupportFragmentManager(),"delete");
    }

}
