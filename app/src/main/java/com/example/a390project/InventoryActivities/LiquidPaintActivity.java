package com.example.a390project.InventoryActivities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.a390project.FirebaseHelper;
import com.example.a390project.ListViewAdapters.InventoryPaintListViewAdapter;
import com.example.a390project.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LiquidPaintActivity extends AppCompatActivity {

    FirebaseHelper firebaseHelper;
    private static final String TAG = "LiquidPaintActivity";
    private ValueEventListener paintValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liquid_paint);
        Log.d(TAG, "onCreate: created Liquid");
        setActionBar("Liquid Paint Inventory");
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseHelper = new FirebaseHelper();
        paintValueEventListener = firebaseHelper.populatePaintInventory(this, true);
    }

    @Override
    protected void onPause(){
        super.onPause();
        FirebaseDatabase.getInstance().getReference().child("inventory").child("liquid").removeEventListener(paintValueEventListener);
    }

    //custom heading and back button
    public void setActionBar(String heading) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(heading);
        actionBar.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                this.finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
