package com.example.a390project.ListViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a390project.GraphActivity;
import com.example.a390project.Model.Graph;
import com.example.a390project.Model.GraphData;
import com.example.a390project.Model.WorkBlock;
import com.example.a390project.R;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.text.DateFormat;
import java.util.List;

public class GraphsListViewAdapter extends BaseAdapter {

    private Context context; //context
    private List<Graph> items; //data source of the list adapter

    //views
    private TextView mGraphName;
    private TextView mGraphCTime;

    //public constructor
    public GraphsListViewAdapter(Context context, List<Graph> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.row_item_graph_names, parent, false);
        }

        mGraphName = convertView.findViewById(R.id.graph_name_row_item);
        mGraphCTime = convertView.findViewById(R.id.graph_created_time_row_item);

        final Graph currentItem = (Graph) getItem(position);

        DateFormat df = DateFormat.getDateTimeInstance();
        String dateStringstartTime = df.format(currentItem.getStartTime());

        mGraphName.setText(currentItem.getMachineTitle() + " - " + currentItem.getGraphTitle());
        mGraphCTime.setText(dateStringstartTime);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GraphActivity.class);
                intent.putExtra("graphID",currentItem.getGraphID());
                context.startActivity(intent);
            }
        });

        return convertView;
    }


        // get current item to be displayed
//        final GraphData currentItem = (GraphData) getItem(position);
//
//        graph = convertView.findViewById(R.id.graph_v);
//
//        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>();
////        PointsGraphSeries<DataPoint> series2 = new PointsGraphSeries<>();
////
////        for(int i = 0; i<currentItem.getY().size(); i++){
////            series1.appendData(new DataPoint(currentItem.getX().get(i),currentItem.getY().get(i)),true,1000);
////            series2.appendData(new DataPoint(currentItem.getX().get(i),currentItem.getY().get(i)),true,1000);
////        }
////        //graphtitle
////        graph.setTitle(currentItem.getGraphTitle());
////        //add the line graph and individual points of temps
////        graph.addSeries(series1);
////        graph.addSeries(series2);
////        series2.setShape(PointsGraphSeries.Shape.POINT);
////        series2.setSize((float)8);
////        series2.setColor(Color.RED);
////        //dynamically change size of axis based on max values stored in x and y
////        graph.getViewport().setXAxisBoundsManual(true);
////        graph.getViewport().setYAxisBoundsManual(true);
////        graph.getViewport().setMaxX(getMax(currentItem.getX())+10);
////        graph.getViewport().setMinX(0);
////        graph.getViewport().setMaxY(getMax(currentItem.getY())+10);
////        graph.getViewport().setMinY(0);
////
////        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
////            @Override
////            public String formatLabel(double value, boolean isValueX) {
////                if (isValueX) {
////                    // show normal x values
////                    return super.formatLabel(value, isValueX);
////                } else {
////                    // show currency for y values
////                    return super.formatLabel(value, isValueX) + " °F";
////                }
////            }
////        });
////
////        //tap listener
////        series2.setOnDataPointTapListener(new OnDataPointTapListener() {
////            @Override
////            public void onTap(Series series, DataPointInterface dataPoint) {
////                Toast.makeText(context, dataPoint+"", Toast.LENGTH_SHORT).show();
////            }
////        });

        //set views of row item

//    public float getMax(List<Float> list){
//        float max = Integer.MIN_VALUE;
//        for(int i=0; i<list.size(); i++){
//            if(list.get(i) > max){
//                max = list.get(i);
//            }
//        }
//        return max;
//    }
}
