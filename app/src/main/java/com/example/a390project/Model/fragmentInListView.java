package com.example.a390project.Model;

import android.support.v7.app.AppCompatActivity;

import com.example.a390project.DialogFragments.DeleteTaskDialogFragment;

public class fragmentInListView extends AppCompatActivity {
    public void deletetaskfragment(){
        DeleteTaskDialogFragment dialog = new DeleteTaskDialogFragment();
        dialog.show(getSupportFragmentManager(), "Create Machine");
    }
}
