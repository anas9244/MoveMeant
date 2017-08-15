package com.example.anas.movemeantapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class PlaceDetails extends AppCompatActivity implements OnMapReadyCallback {


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String selectedPName;
    String selectedPtype;
    String selectedPid;
    String selectedNumofvis;
    String selectedPlat;
    String selectedPlong;
    ListView detailsList;
    TextView textType;
    TextView textNum;

    private GoogleMap mMap;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        selectedPName=sharedPreferences.getString("selectedPName","");
        selectedPtype=sharedPreferences.getString("selectedPtype","");
        selectedPid=sharedPreferences.getString("selectedPid","");
        selectedNumofvis=sharedPreferences.getString("selectedNumofvis","");
        selectedPlat=sharedPreferences.getString("selectedPlat","");
        selectedPlong=sharedPreferences.getString("selectedPlong","");

        setTitle(selectedPName);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        textType=(TextView)findViewById(R.id.textType);
        textNum=(TextView)findViewById(R.id.textNum);

        textType.setText("Type: "+ selectedPtype);
        textNum.setText("Number of users who visted this place: "+ selectedNumofvis);




    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(selectedPlat), Double.parseDouble(selectedPlong))));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(Double.parseDouble(selectedPlat),
                        Double.parseDouble(selectedPlong)), 16));
    }
}
