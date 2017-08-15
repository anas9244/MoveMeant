package com.example.anas.movemeantapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Timer;
import java.util.TimerTask;


public class PlaceDetails extends AppCompatActivity implements OnMapReadyCallback {


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String selectedPName;
    String selectedPtype;
    String selectedPid;
    String selectedNumofvis;
    String selectedPlat;
    String selectedPlong;
    String female_num;
    String male_num;
    ListView detailsList;
    TextView textType;
    TextView textNum;
    TextView textFemale;
    TextView textMale;

    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);



        textFemale= (TextView)findViewById(R.id.textFemale);
        textMale= (TextView)findViewById(R.id.textMale);

        selectedPName=sharedPreferences.getString("selectedPName","");
        selectedPtype=sharedPreferences.getString("selectedPtype","");
        selectedPid=sharedPreferences.getString("selectedPid","");
        selectedNumofvis=sharedPreferences.getString("selectedNumofvis","");
        selectedPlat=sharedPreferences.getString("selectedPlat","");
        selectedPlong=sharedPreferences.getString("selectedPlong","");


        if (isConnected(this)) {
            final String  typeFemale = "GetFemale";
            final String  typeMale="GetMale";


            final BackgroundConnector backgroundconnector = new BackgroundConnector(this);
            backgroundconnector.execute(typeFemale,selectedPid);

            final Timer timerfemale = new Timer();
            TimerTask taskfemale = new TimerTask() {
                @Override
                public void run() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (backgroundconnector.getStatus() == AsyncTask.Status.FINISHED) {
                                timerfemale.cancel();
                                if (!sharedPreferences.getString("female_num", "").isEmpty()) {

                                        female_num=sharedPreferences.getString("female_num", "");
                                        textFemale.setText("Female: "+female_num);

                                } else {
                                    female_num="0";
                                }


                            }
                        }
                    });


                }
            };
            timerfemale.scheduleAtFixedRate(taskfemale, 1000, 1000);


            final BackgroundConnector backgroundconnectormale = new BackgroundConnector(this);
            backgroundconnectormale.execute(typeMale,selectedPid);

            final Timer timermale = new Timer();
            TimerTask taskmale = new TimerTask() {
                @Override
                public void run() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (backgroundconnector.getStatus() == AsyncTask.Status.FINISHED) {
                                timermale.cancel();
                                if (!sharedPreferences.getString("male_num", "").isEmpty()) {

                                    male_num=sharedPreferences.getString("male_num", "");
                                    textMale.setText("Male: "+male_num);

                                } else {
                                    male_num="0";
                                }
                            }
                        }
                    });


                }
            };
            timermale.scheduleAtFixedRate(taskmale, 1000, 1000);




        } else {

            Toast.makeText(this, "Network is not available", Toast.LENGTH_LONG).show();
        }




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


    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
