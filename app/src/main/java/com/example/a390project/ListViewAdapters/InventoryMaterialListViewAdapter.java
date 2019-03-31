package com.example.a390project.ListViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a390project.Model.Material;
import com.example.a390project.R;

import java.util.List;

public class InventoryMaterialListViewAdapter extends BaseAdapter {

    Context context;
    List<Material> materials;

    public InventoryMaterialListViewAdapter(Context context, List<Material> materials){
        this.context = context;
        this.materials = materials;
    }

    @Override
    public int getCount() {
        return materials.size();
    }

    @Override
    public Object getItem(int position) {
        return materials.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.row_item_material_inventory, parent, false);

        TextView materialName = convertView.findViewById(R.id.materialNameTextView);
        TextView materialDescription = convertView.findViewById(R.id.materialDescriptionTextView);
        TextView materialQuantity = convertView.findViewById(R.id.materialQuantityTextView);

        materialName.setText(materials.get(position).getMaterialName());
        materialDescription.setText(materials.get(position).getMaterialDescription());
        materialQuantity.setText(String.valueOf(materials.get(position).getMaterialQuantity()));

        return convertView;
    }
}
