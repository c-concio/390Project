package com.example.a390project.ListViewAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.a390project.FirebaseHelper;
import com.example.a390project.Model.PaintBucket;
import com.example.a390project.R;

import java.util.List;

public class PaintRecyclerAdapter extends RecyclerView.Adapter<PaintRecyclerAdapter.ViewHolder> {

    private List<PaintBucket> mPaintBucket;
    private Context context;

    public PaintRecyclerAdapter(Context context, List<PaintBucket> paintBucket){
        this.context = context;
        this.mPaintBucket = paintBucket;
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mPaintDescription, mPaintCode, mPaintType, mPaintBakeTemperature, mPaintBakeTime;
        public EditText mPaintWeight;

        public ViewHolder(View itemView){
            super(itemView);
            mPaintDescription = itemView.findViewById(R.id.paint_description_row_item);
            mPaintCode = itemView.findViewById(R.id.paint_code_row_item);
            mPaintType = itemView.findViewById(R.id.paint_type_row_item);
            mPaintBakeTemperature = itemView.findViewById(R.id.bake_temperature_row_item);
            mPaintBakeTime = itemView.findViewById(R.id.bake_time_row_item);
            mPaintWeight = itemView.findViewById(R.id.paint_weight_row_item);
        }
    }

    @Override
    public PaintRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //Inflate custom layout
        View paintView = inflater.inflate(R.layout.row_item_paint_inventory, parent, false);

        //Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(paintView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull PaintRecyclerAdapter.ViewHolder holder, int position) {
        PaintBucket paintBucket = mPaintBucket.get(position);

        TextView textView1 = holder.mPaintDescription;
        textView1.setText(paintBucket.getPaintDescription());
        TextView textView2 = holder.mPaintCode;
        textView2.setText(paintBucket.getPaintCode());
        TextView textView3 = holder.mPaintType;
        textView3.setText(paintBucket.getPaintType());
        TextView textView4 = holder.mPaintBakeTemperature;
        textView4.setText(Integer.toString(paintBucket.getBakeTemperature()));
        TextView textView5 = holder.mPaintBakeTime;
        textView5.setText(Integer.toString(paintBucket.getBakeTime()));
        EditText editView1 = holder.mPaintWeight;
        editView1.setText(Float.toString(paintBucket.getPaintWeight()));

        FirebaseHelper firebaseHelper = new FirebaseHelper();
        firebaseHelper.editInventoryWeight(holder.mPaintWeight, paintBucket);
    }

    @Override
    public int getItemCount() {
        return mPaintBucket.size();
    }
}
