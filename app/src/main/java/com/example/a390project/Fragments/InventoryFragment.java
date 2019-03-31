package com.example.a390project.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.a390project.FirebaseHelper;
import com.example.a390project.ListViewAdapters.InventoryCategoryListViewAdapter;
import com.example.a390project.R;

import java.util.ArrayList;
import java.util.List;

public class InventoryFragment extends Fragment {

    private String TAG = "InventoryFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.inventory_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        /*FirebaseHelper firebaseHelper = new FirebaseHelper();
        //populate all projects from firebase to listview
        firebaseHelper.populateInventory(view, getActivity());*/

        // list of String that holds all the inventory categories
        List<String> categories = new ArrayList<>();
        categories.add("Liquid Paint");
        categories.add("Powder Paint");
        categories.add("Packaging Material");

        // create an adapter and set the listView
        ListView inventoryListView = view.findViewById(R.id.inventory_list_view);
        InventoryCategoryListViewAdapter adapter = new InventoryCategoryListViewAdapter(view.getContext(), categories);
        inventoryListView.setAdapter(adapter);
    }
}
