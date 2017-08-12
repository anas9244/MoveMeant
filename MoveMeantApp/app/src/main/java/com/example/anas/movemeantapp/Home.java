package com.example.anas.movemeantapp;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;


import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.provider.Settings.Secure;

import java.lang.reflect.Array;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.sql.Date;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * An activity that displays a map showing the place at the device's current location.
 */
public class Home extends AppCompatActivity {


    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;


    LocationRequest mLocationRequest;

    NotificationManager mNotificationManager;
    NotificationCompat.Builder mBuilder;

    Button button;
    TextView textView;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String user_id;

    String android_id;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_home);

        android_id = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;

            if (!NewVisitService.mRunning)
                startService(new Intent(Home.this, NewVisitService.class));


        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }


        mBuilder = new NotificationCompat.Builder(this);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mBuilder.setContentTitle("Device ID").setContentText(android_id).setSmallIcon(R.drawable.running);
        mNotificationManager.notify(004, mBuilder.build());


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        user_id = sharedPreferences.getString("user_id", "");


        mBuilder.setContentText("User id= " + user_id).setContentTitle("User ID");
        mNotificationManager.notify(002, mBuilder.build());


        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = sharedPreferences.edit();
                editor.putString("user_id", null);
                editor.commit();
                stopService(new Intent(Home.this, NewVisitService.class));
                Intent intent = new Intent(Home.this, LoginActivity.class);
                startActivity(intent);
                finish();


            }
        });

        textView = (TextView) findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());


        if (isConnected(this)) {
            String type = "GetVisits";
            final BackgroundConnector backgroundConnector = new BackgroundConnector(this);
            backgroundConnector.execute(type);

            final Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (backgroundConnector.getStatus() == AsyncTask.Status.FINISHED) {
                        timer.cancel();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(backgroundConnector.getVisitis());
                            }
                        });

                    }

                }
            };
            timer.scheduleAtFixedRate(task, 1000, 1000);

        } else {
            Toast.makeText(this, "Network is not available", Toast.LENGTH_LONG).show();
        }


    }


    @Override
    protected void onResume() {
        super.onResume();


    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;

                    if (!NewVisitService.mRunning)
                        startService(new Intent(Home.this, NewVisitService.class));
                }
            }
        }

    }


    private String getPlaceType(int i)

    {
        String placeType = "";

        switch (i) {

            case Place.TYPE_ACCOUNTING:
                placeType = "ACCOUNTING";
                break;
            case Place.TYPE_AIRPORT:
                placeType = "AIRPORT";
                break;
            case Place.TYPE_AMUSEMENT_PARK:
                placeType = "AMUSEMENT PARK";
                break;
            case Place.TYPE_AQUARIUM:
                placeType = "AQUARIUM";
                break;
            case Place.TYPE_ART_GALLERY:
                placeType = "ART_GALLERY";
                break;
            case Place.TYPE_ATM:
                placeType = "ATM";
                break;
            case Place.TYPE_BAKERY:
                placeType = "BAKERY";
                break;
            case Place.TYPE_BANK:
                placeType = "BANK";
                break;
            case Place.TYPE_BAR:
                placeType = "BAR";
                break;
            case Place.TYPE_BEAUTY_SALON:
                placeType = "BEAUTY_SALON";
                break;
            case Place.TYPE_BICYCLE_STORE:
                placeType = "BICYCLE_STORE";
                break;
            case Place.TYPE_BOOK_STORE:
                placeType = "BOOK_STORE";
                break;
            case Place.TYPE_BOWLING_ALLEY:
                placeType = "BOWLING_ALLEY";
                break;
            case Place.TYPE_BUS_STATION:
                placeType = "BUS_STATION";
                break;
            case Place.TYPE_CAFE:
                placeType = "CAFE";
                break;
            case Place.TYPE_CAMPGROUND:
                placeType = "CAMPGROUND";
                break;
            case Place.TYPE_CAR_DEALER:
                placeType = "CAR_DEALER";
                break;
            case Place.TYPE_CAR_REPAIR:
                placeType = "CAR_REPAIR";
                break;
            case Place.TYPE_CASINO:
                placeType = "CASINO";
                break;
            case Place.TYPE_CEMETERY:
                placeType = "CEMETERY";
                break;
            case Place.TYPE_CHURCH:
                placeType = "CHURCH";
                break;
            case Place.TYPE_CITY_HALL:
                placeType = "CITY_HALL";
                break;
            case Place.TYPE_CLOTHING_STORE:
                placeType = "CLOTHING_STORE";
                break;
            case Place.TYPE_COLLOQUIAL_AREA:
                placeType = "COLLOQUIAL_AREA";
                break;
            case Place.TYPE_CONVENIENCE_STORE:
                placeType = "CONVENIENCE_STORE";
                break;
            case Place.TYPE_COUNTRY:
                placeType = "COUNTRY";
                break;
            case Place.TYPE_COURTHOUSE:
                placeType = "COURTHOUSE";
                break;
            case Place.TYPE_DENTIST:
                placeType = "DENTIST";
                break;
            case Place.TYPE_DEPARTMENT_STORE:
                placeType = "DEPARTMENT_STORE";
                break;
            case Place.TYPE_DOCTOR:
                placeType = "DOCTOR";
                break;
            case Place.TYPE_ELECTRICIAN:
                placeType = "ELECTRICIAN";
                break;
            case Place.TYPE_ELECTRONICS_STORE:
                placeType = "ELECTRONICS_STORE";
                break;
            case Place.TYPE_EMBASSY:
                placeType = "EMBASSY";
                break;
            case Place.TYPE_ESTABLISHMENT:
                placeType = "ESTABLISHMENT";
                break;
            case Place.TYPE_FINANCE:
                placeType = "FINANCE";
                break;
            case Place.TYPE_FIRE_STATION:
                placeType = "FIRE_STATION";
                break;
            case Place.TYPE_FLOOR:
                placeType = "FLOOR";
                break;
            case Place.TYPE_FLORIST:
                placeType = "FLORIST";
                break;
            case Place.TYPE_FOOD:
                placeType = "FOOD";
                break;
            case Place.TYPE_FUNERAL_HOME:
                placeType = "FUNERAL_HOME";
                break;
            case Place.TYPE_FURNITURE_STORE:
                placeType = "FURNITURE_STORE";
                break;
            case Place.TYPE_GAS_STATION:
                placeType = "GAS_STATION";
                break;
            case Place.TYPE_GENERAL_CONTRACTOR:
                placeType = "GENERAL_CONTRACTOR";
                break;
            case Place.TYPE_GEOCODE:
                placeType = "GEOCODE";
                break;
            case Place.TYPE_GROCERY_OR_SUPERMARKET:
                placeType = "GROCERY_OR_SUPERMARKET";
                break;
            case Place.TYPE_GYM:
                placeType = "GYM";
                break;
            case Place.TYPE_HAIR_CARE:
                placeType = "HAIR_CARE";
                break;
            case Place.TYPE_HARDWARE_STORE:
                placeType = "HARDWARE_STORE";
                break;
            case Place.TYPE_HEALTH:
                placeType = "HEALTH";
                break;
            case Place.TYPE_HINDU_TEMPLE:
                placeType = "HINDU_TEMPLE";
                break;
            case Place.TYPE_HOME_GOODS_STORE:
                placeType = "HOME_GOODS_STORE";
                break;
            case Place.TYPE_HOSPITAL:
                placeType = "HOSPITAL";
                break;
            case Place.TYPE_INSURANCE_AGENCY:
                placeType = "INSURANCE_AGENCY";
                break;
            case Place.TYPE_INTERSECTION:
                placeType = "INTERSECTION";
                break;
            case Place.TYPE_JEWELRY_STORE:
                placeType = "JEWELRY_STORE";
                break;
            case Place.TYPE_LAUNDRY:
                placeType = "LAUNDRY";
                break;
            case Place.TYPE_LAWYER:
                placeType = "LAWYER";
                break;
            case Place.TYPE_LIBRARY:
                placeType = "LIBRARY";
                break;
            case Place.TYPE_LIQUOR_STORE:
                placeType = "LIQUOR_STORE";
                break;
            case Place.TYPE_LOCALITY:
                placeType = "LOCALITY";
                break;
            case Place.TYPE_LOCAL_GOVERNMENT_OFFICE:
                placeType = "LOCAL_GOVERNMENT_OFFICE";
                break;
            case Place.TYPE_LOCKSMITH:
                placeType = "LOCKSMITH";
                break;
            case Place.TYPE_LODGING:
                placeType = "LODGING";
                break;
            case Place.TYPE_MEAL_DELIVERY:
                placeType = "MEAL_DELIVERY";
                break;
            case Place.TYPE_MEAL_TAKEAWAY:
                placeType = "MEAL_TAKEAWAY";
                break;
            case Place.TYPE_MOSQUE:
                placeType = "MOSQUE";
                break;
            case Place.TYPE_MOVIE_RENTAL:
                placeType = "MOVIE_RENTAL";
                break;
            case Place.TYPE_MOVIE_THEATER:
                placeType = "MOVIE_THEATER";
                break;
            case Place.TYPE_MOVING_COMPANY:
                placeType = "MOVING_COMPANY";
                break;
            case Place.TYPE_MUSEUM:
                placeType = "MUSEUM";
                break;
            case Place.TYPE_NATURAL_FEATURE:
                placeType = "NATURAL_FEATURE";
                break;
            case Place.TYPE_NEIGHBORHOOD:
                placeType = "NEIGHBORHOOD";
                break;
            case Place.TYPE_NIGHT_CLUB:
                placeType = "NIGHT_CLUB";
                break;
            case Place.TYPE_OTHER:
                placeType = "OTHER";
                break;
            case Place.TYPE_PAINTER:
                placeType = "PAINTER";
                break;
            case Place.TYPE_PARK:
                placeType = "PARK";
                break;
            case Place.TYPE_PARKING:
                placeType = "PARKING";
                break;
            case Place.TYPE_PET_STORE:
                placeType = "PET_STORE";
                break;
            case Place.TYPE_PHARMACY:
                placeType = "PHARMACY";
                break;
            case Place.TYPE_PHYSIOTHERAPIST:
                placeType = "PHYSIOTHERAPIST";
                break;
            case Place.TYPE_PLACE_OF_WORSHIP:
                placeType = "PLACE_OF_WORSHIP";
                break;
            case Place.TYPE_PLUMBER:
                placeType = "PLUMBER";
                break;
            case Place.TYPE_POINT_OF_INTEREST:
                placeType = "POINT_OF_INTEREST";
                break;
            case Place.TYPE_POLICE:
                placeType = "POLICE";
                break;
            case Place.TYPE_POLITICAL:
                placeType = "POLITICAL";
                break;
            case Place.TYPE_POSTAL_CODE:
                placeType = "POSTAL_CODE";
                break;
            case Place.TYPE_POSTAL_CODE_PREFIX:
                placeType = "POSTAL_CODE_PREFIX";
                break;
            case Place.TYPE_POSTAL_TOWN:
                placeType = "POSTAL_TOWN";
                break;
            case Place.TYPE_POST_BOX:
                placeType = "POST_BOX";
                break;
            case Place.TYPE_POST_OFFICE:
                placeType = "POST_OFFICE";
                break;
            case Place.TYPE_PREMISE:
                placeType = "PREMISE";
                break;
            case Place.TYPE_REAL_ESTATE_AGENCY:
                placeType = "REAL_ESTATE_AGENCY";
                break;
            case Place.TYPE_RESTAURANT:
                placeType = "RESTAURANT";
                break;
            case Place.TYPE_ROOFING_CONTRACTOR:
                placeType = "ROOFING_CONTRACTOR";
                break;
            case Place.TYPE_ROOM:
                placeType = "ROOM";
                break;
            case Place.TYPE_ROUTE:
                placeType = "ROUTE";
                break;
            case Place.TYPE_RV_PARK:
                placeType = "RV_PARK";
                break;
            case Place.TYPE_SCHOOL:
                placeType = "SCHOOL";
                break;
            case Place.TYPE_SHOE_STORE:
                placeType = "SHOE_STORE";
                break;
            case Place.TYPE_SHOPPING_MALL:
                placeType = "SHOPPING_MALL";
                break;
            case Place.TYPE_SPA:
                placeType = "SPA";
                break;
            case Place.TYPE_STADIUM:
                placeType = "STADIUM";
                break;
            case Place.TYPE_STORAGE:
                placeType = "STORAGE";
                break;
            case Place.TYPE_STORE:
                placeType = "STORE";
                break;
            case Place.TYPE_STREET_ADDRESS:
                placeType = "STREET_ADDRESS";
                break;
            case Place.TYPE_SUBLOCALITY:
                placeType = "SUBLOCALITY";
                break;
            case Place.TYPE_SUBPREMISE:
                placeType = "SUBPREMISE";
                break;
            case Place.TYPE_SUBWAY_STATION:
                placeType = "SUBWAY_STATION";
                break;
            case Place.TYPE_SYNAGOGUE:
                placeType = "SYNAGOGUE";
                break;
            case Place.TYPE_SYNTHETIC_GEOCODE:
                placeType = "SYNTHETIC_GEOCODE";
                break;
            case Place.TYPE_TAXI_STAND:
                placeType = "TAXI_STAND";
                break;
            case Place.TYPE_TRAIN_STATION:
                placeType = "TRAIN_STATION";
                break;
            case Place.TYPE_TRANSIT_STATION:
                placeType = "TRANSIT_STATION";
                break;
            case Place.TYPE_TRAVEL_AGENCY:
                placeType = "TRAVEL_AGENCY";
                break;
            case Place.TYPE_UNIVERSITY:
                placeType = "UNIVERSITY";
                break;
            case Place.TYPE_VETERINARY_CARE:
                placeType = "VETERINARY_CARE";
                break;
            case Place.TYPE_ZOO:
                placeType = "ZOO";
                break;
            default:
                placeType = "Unknown";
                break;

        }

        return placeType;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();


        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


}
