package com.example.a390project.ListViewAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a390project.Model.Material;
import com.example.a390project.R;

import java.util.List;

public class MaterialRecyclerAdapter extends RecyclerView.Adapter<MaterialRecyclerAdapter.ViewHolder> {

    private List<Material> mMaterial;
    private Context context;

    public MaterialRecyclerAdapter(Context context, List<Material> material){
        this.context = context;
        this.mMaterial = material;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mMaterialName, mMaterialDescription, mMaterialQuantity;

        public ViewHolder(View itemView){
            super(itemView);
            mMaterialName = itemView.findViewById(R.id.materialNameTextView);
            mMaterialDescription = itemView.findViewById(R.id.materialDescriptionTextView);
            mMaterialQuantity = itemView.findViewById(R.id.materialQuantityTextView);
        }
    }

    @Override
    public MaterialRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //Inflate custom layout
        View materialView = inflater.inflate(R.layout.row_item_material_inventory, parent, false);

        //Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(materialView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialRecyclerAdapter.ViewHolder holder, int position) {
        Material material = mMaterial.get(position);

        TextView textView1 = holder.mMaterialName;
        textView1.setText(material.getMaterialName());
        TextView textView2 = holder.mMaterialDescription;
        textView2.setText(material.getMaterialDescription());
        TextView textView3 = holder.mMaterialQuantity;
        textView3.setText(Float.toString(material.getMaterialQuantity()));

    }

    @Override
    public int getItemCount(){ return mMaterial.size(); }
}
