package com.example.a390project.DialogFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.a390project.FirebaseHelper;
import com.example.a390project.R;

@SuppressLint("ValidFragment")
public class DeleteProjectDialogFragment extends DialogFragment {

    private TextView mDeleteProjectTitle;
    private Button Yesbutton;
    private Button Nobutton;

    private String projectPO;
    private String title;

    public DeleteProjectDialogFragment(String projectPO, String title) {
        this.projectPO = projectPO;
        this.title = title;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delete_project_dialog_fragment, container, false);
        mDeleteProjectTitle = view.findViewById(R.id.delete_project_title);
        Yesbutton = view.findViewById(R.id.Yes_button_project);
        Nobutton = view.findViewById(R.id.No_button_project);

        mDeleteProjectTitle.setText("Delete " + title);

        Yesbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseHelper FBHelper = new FirebaseHelper();
                FBHelper.deleteProject(projectPO);
                getDialog().dismiss();
            }
        });
        Nobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return view;
    }

}


