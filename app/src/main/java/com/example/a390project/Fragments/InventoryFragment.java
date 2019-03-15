package com.example.a390project.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a390project.FirebaseHelper;
import com.example.a390project.R;

public class InventoryFragment extends Fragment {

    private String TAG = "InventoryFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.inventory_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        FirebaseHelper firebaseHelper = new FirebaseHelper();
        //populate all projects from firebase to listview
        firebaseHelper.populateInventory(view, getActivity());
    }
}
