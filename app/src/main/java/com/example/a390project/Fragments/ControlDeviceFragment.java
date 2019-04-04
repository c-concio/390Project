package com.example.a390project.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.a390project.DummyDatabase;
import com.example.a390project.FirebaseHelper;
import com.example.a390project.ListViewAdapters.ControlDeviceListViewAdapter;
import com.example.a390project.MainActivity;
import com.example.a390project.Model.ControlDevice;
import com.example.a390project.Model.Task;
import com.example.a390project.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ControlDeviceFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "CDevices";

    private FirebaseHelper firebaseHelper;

    //mapview
    private MapView mMapView;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    //Location Permission
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private View mView;
    private Bundle mSavedInstanceState;
    private Marker marker = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.control_device_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        firebaseHelper = new FirebaseHelper();
        firebaseHelper.populateControlDevices(view, getActivity());
        mMapView = view.findViewById(R.id.mapView);

        mView = view;
        mSavedInstanceState = savedInstanceState;
        getLocationPermission(mView, mSavedInstanceState);
    }

    private void getLocationPermission(View view, Bundle savedInstanceState){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap(view, savedInstanceState);
            }else{
                ActivityCompat.requestPermissions(getActivity(),
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(getActivity(),
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void initMap(View view, Bundle savedInstanceState) {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);
        Log.d(TAG, "onViewCreated: ");

        mMapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Toast.makeText(getContext(), "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation(mMap);

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

        }
    }

    private void getDeviceLocation(final GoogleMap mMap){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        try{
            if(mLocationPermissionsGranted){
                //shop marker and radius
                final LatLng shopLatLng = new LatLng(45.456750,-73.727390);
                final Marker shopMarker = mMap.addMarker(new MarkerOptions().position(shopLatLng).title("MPC Finition Shop")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                final CircleOptions circle = new CircleOptions()
                        .center(shopLatLng)
                        .radius(200)
                        .strokeWidth(0f)
                        .fillColor(0x550000FF);
                mMap.addCircle(circle);

                com.google.android.gms.tasks.Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            LocationListener locationListener = new LocationListener() {
                                @Override
                                public void onLocationChanged(Location currentLocation) {

                                    LatLng currentUserLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                                    CameraUpdate zoom=CameraUpdateFactory.zoomTo(DEFAULT_ZOOM);
                                    CameraUpdate center= CameraUpdateFactory.newLatLng(currentUserLatLng);

                                    if (marker != null) {
                                        marker.remove();
                                    }
                                    marker = mMap.addMarker(new MarkerOptions().position(currentUserLatLng).title("You are Here"));
                                    mMap.moveCamera(center);
                                    mMap.animateCamera(zoom);


                                    float[] distance = new float[2];

                                    Location.distanceBetween( marker.getPosition().latitude, marker.getPosition().longitude,
                                            circle.getCenter().latitude, circle.getCenter().longitude, distance);

                                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                    String uId = FirebaseAuth.getInstance().getUid();

                                    if(distance[0] > circle.getRadius()){
                                        //Toast.makeText(getContext(), "Outside " + distance[0], Toast.LENGTH_LONG).show();
                                        Log.d(TAG, "onLocationChanged: " + "Outside " + distance[0]);
                                        rootRef.child("users").child(uId).child("canToggleCDevices").setValue(false);
                                    } else {
                                        //Toast.makeText(getContext(), "Inside " + distance[0], Toast.LENGTH_LONG).show();
                                        Log.d(TAG, "onLocationChanged: " + "Inside " + distance[0]);
                                        rootRef.child("users").child(uId).child("canToggleCDevices").setValue(true);
                                        //Allow control of switch
                                        //...
                                    }


                                }

                                @Override
                                public void onStatusChanged(String provider, int status, Bundle extras) {

                                }

                                @Override
                                public void onProviderEnabled(String provider) {

                                }

                                @Override
                                public void onProviderDisabled(String provider) {

                                }
                            };
                            LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, locationListener);

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(getContext(), "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap(mView, mSavedInstanceState);
                }
            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }


    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}
