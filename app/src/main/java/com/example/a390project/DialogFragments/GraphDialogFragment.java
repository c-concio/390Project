package com.example.a390project.DialogFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a390project.FirebaseHelper;
import com.example.a390project.R;

public class GraphDialogFragment extends DialogFragment {

    FloatingActionButton mFab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.graph_dialog_fragment, container, false);

        mFab = view.findViewById(R.id.fab_graphable_project);

        FirebaseHelper firebaseHelper = new FirebaseHelper();
        firebaseHelper.populateGraphableProjects(view, getActivity(), mFab);

        return view;
    }
}
