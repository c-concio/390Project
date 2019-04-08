package com.example.a390project.InventoryActivities;

import android.renderscript.Sampler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.a390project.FirebaseHelper;
import com.example.a390project.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PackagingMaterialActivity extends AppCompatActivity {

    FirebaseHelper firebaseHelper;
    private ValueEventListener materialValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_packaging_material2);
        setActionBar("Material Inventory");
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseHelper = new FirebaseHelper();
        materialValueEventListener = firebaseHelper.populateMaterialInventory(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseDatabase.getInstance().getReference().child("inventory").child("material").removeEventListener(materialValueEventListener);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        firebaseHelper = new FirebaseHelper();
        getMenuInflater().inflate(R.menu.inventory, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String s) {
                firebaseHelper.materialSearch(PackagingMaterialActivity.this, s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                firebaseHelper.materialSearch(PackagingMaterialActivity.this, s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
