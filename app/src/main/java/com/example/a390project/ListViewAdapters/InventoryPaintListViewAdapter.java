package com.example.a390project.ListViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a390project.Model.PaintBucket;
import com.example.a390project.R;

import java.util.List;

public class InventoryPaintListViewAdapter extends BaseAdapter {

    private Context context;
    private List<PaintBucket> paintBuckets;

    //views
    private TextView mPaintDescription, mPaintCode, mPaintType, mPaintBakeTemperature, mPaintBakeTime, mPaintWeight;

    //variables

    public InventoryPaintListViewAdapter(Context context, List<PaintBucket> paintBuckets){
        this.context = context;
        this.paintBuckets = paintBuckets;
    }

    @Override
    public int getCount() {
        return paintBuckets.size();
    }

    @Override
    public Object getItem(int position) {
        return paintBuckets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_item_inventory, parent, false);
        }

        // get current item to be displayed
        final PaintBucket currentItem = (PaintBucket) getItem(position);

        mPaintDescription = convertView.findViewById(R.id.paint_description_row_item);
        mPaintCode = convertView.findViewById(R.id.paint_code_row_item);
        mPaintType = convertView.findViewById(R.id.paint_type_row_item);
        mPaintBakeTemperature = convertView.findViewById(R.id.bake_temperature_row_item);
        mPaintBakeTime = convertView.findViewById(R.id.bake_time_row_item);
        mPaintWeight = convertView.findViewById(R.id.paint_weight_row_item);

        mPaintDescription.setText(currentItem.getPaintDescription());
        mPaintCode.setText(currentItem.getPaintCode());
        mPaintType.setText(currentItem.getPaintType());
        mPaintBakeTemperature.setText(Integer.toString(currentItem.getBakeTemperature()));
        mPaintBakeTime.setText(Integer.toString(currentItem.getBakeTime()));
        mPaintWeight.setText(Float.toString(currentItem.getPaintWeight()));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //execute when click row item
            }
        });

        return convertView;
    }
}
