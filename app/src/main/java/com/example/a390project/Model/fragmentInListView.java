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
    private String TASKID;

    public fragmentInListView(String taskID){
        TASKID = taskID;

    }

    public void deletetaskfragment(Context context) {
        DeleteTaskDialogFragment dialog = new DeleteTaskDialogFragment(TASKID);
        dialog.show(((ProjectActivity) context).getSupportFragmentManager(),"delete");
    }

}
