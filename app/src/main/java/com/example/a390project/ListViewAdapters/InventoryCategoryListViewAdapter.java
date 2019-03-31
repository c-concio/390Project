package com.example.a390project.ListViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a390project.InventoryActivities.LiquidPaintActivity;
import com.example.a390project.InventoryActivities.PackagingMaterialActivity;
import com.example.a390project.InventoryActivities.PowderPaintActivity;
import com.example.a390project.R;
import com.example.a390project.TaskInspectionActivity;

import java.util.List;

public class InventoryCategoryListViewAdapter extends BaseAdapter {

    // Variables
    private List<String> categories;
    private Context context;

    private static final String TAG = "InventoryCategoryListVi";

    public InventoryCategoryListViewAdapter(Context context, List<String> categories){
        this.categories = categories;
        this.context = context;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public String getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.row_item_inventory_category, parent, false);

        TextView categoryTitleTextView = convertView.findViewById(R.id.categoryTitleTextView);
        categoryTitleTextView.setText(categories.get(position));

        // set a list view on click listener whenever an item is clicked
        switch (categories.get(position)){
            case "Liquid Paint":
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, LiquidPaintActivity.class);
                        context.startActivity(intent);
                        Log.d(TAG, "onClick: starting Liquid");
                    }
                });
                break;
            case "Powder Paint":
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, PowderPaintActivity.class);
                        context.startActivity(intent);
                    }
                });
                break;
            case "Packaging Material":
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, PackagingMaterialActivity.class);
                        context.startActivity(intent);
                    }
                });
                break;
        }

        return convertView;
    }

 

}
