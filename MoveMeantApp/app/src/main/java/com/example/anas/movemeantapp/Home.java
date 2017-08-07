package com.example.anas.movemeantapp;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
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
import com.google.android.gms.gcm.Task;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
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

/**
 * An activity that displays a map showing the place at the device's current location.
 */
public class Home extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = Home.class.getSimpleName();


    // The entry point to Google Play services, used by the Places API and Fused Location Provider.
    private GoogleApiClient mGoogleApiClient;


    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.

    private Location mLocationBefore;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Used for selecting the current place.
    String mPlaceName;
    int mPlaceType;
    double mPlacelat;
    double mPlacelong;
    String mPlace_id;


    float max = 0;
    int max_item;

    LocationRequest mLocationRequest;

    NotificationManager mNotificationManager;
    NotificationCompat.Builder mBuilder;

    Button button;

    Context context;
    LocationManager locationManager = null;

    long currentTime = System.currentTimeMillis();
    int timePassed;
    Location locationBefore;
    boolean theUserhasSettled = false;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String user_id;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_home);




        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        // Build the Play services client for use by the Fused Location Provider and the Places API.
        // Use the addApi() method to request the Google Places API and the Fused Location Provider.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();



        mBuilder = new NotificationCompat.Builder(this).setContentText("Recognizing your place...").setOngoing(true).setSmallIcon(R.drawable.running);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

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
                Intent intent = new Intent(Home.this, LoginActivity.class);
                startActivity(intent);


            }
        });
/*
        String type="GetVisists";
        BackgroundConnector backgroundConnector = new BackgroundConnector(this);
        backgroundConnector.execute(type);*/

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }

    /**
     * Builds the map when the Google Play services client is successfully connected.
     */
    @Override
    public void onConnected(Bundle connectionHint) {

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        if (mLocationPermissionGranted) {
            locationBefore  = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


        mLocationBefore = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

    }

    /**
     * Handles failure to connect to the Google Play services client.
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        // Refer to the reference doc for ConnectionResult to see what error codes might
        // be returned in onConnectionFailed.
        Log.d(TAG, "Play services connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    /**
     * Handles suspension of the connection to the Google Play services client.
     */
    @Override
    public void onConnectionSuspended(int cause) {
        Log.d(TAG, "Play services connection suspended");
    }





    /**
     * Handles the result of the request for location permissions.
     */
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
                }
            }
        }
        updateLocationUI();
    }

    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
    private void showCurrentPlace() {



        timePassed = 0;
        theUserhasSettled = false;
        final Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                timePassed++;
                if (timePassed >= 10) {
                    timer.cancel();
                    timePassed = 0;
                    theUserhasSettled = true;
                    if (mLocationPermissionGranted && theUserhasSettled) {
                        mBuilder.setContentText("Recognizing you place").setContentTitle("Current place");
                        mNotificationManager.notify(001, mBuilder.build());
                        mPlaceName = "";
                        mPlaceType = 0;
                        // Get the likely places - that is, the businesses and other points of interest that
                        // are the best match for the device's current location.
                        @SuppressWarnings("MissingPermission")
                        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                                .getCurrentPlace(mGoogleApiClient, null);
                        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                            @Override
                            public void onResult(@NonNull PlaceLikelihoodBuffer likelyPlaces) {

                                for (int j = 0; j < likelyPlaces.getCount(); j++) {
                                    if (likelyPlaces.get(j).getLikelihood() > max) {
                                        max = likelyPlaces.get(j).getLikelihood();
                                        max_item = j;

                                        if (j > 4)
                                            break;

                                    }
                                }

                                mPlaceName = (String) likelyPlaces.get(max_item).getPlace().getName();
                                mPlaceType = likelyPlaces.get(max_item).getPlace().getPlaceTypes().get(0);
                                mPlacelat = likelyPlaces.get(max_item).getPlace().getLatLng().latitude;
                                mPlacelong = likelyPlaces.get(max_item).getPlace().getLatLng().longitude;
                                mPlace_id = likelyPlaces.get(max_item).getPlace().getId();


                                // Release the place likelihood buffer, to avoid memory leaks.
                                likelyPlaces.release();


                                // Show a dialog offering the user the list of likely places, and add a
                                // marker at the selected place.
                                openPlacesDialog();


                            }
                        });


                    }
                }

            }

        };
        timer.scheduleAtFixedRate(task, 1000, 1000);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        locationBefore =  LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);


    }

    /**
     * Displays a form allowing the user to select a place from a list of likely places.
     */
    private void openPlacesDialog() {

        int place_type_number = mPlaceType;
        String place_type_name_1 = getPlaceType(place_type_number);
        String place_type_name_2 = place_type_name_1.replaceAll("_", " ");
        String placeType = place_type_name_2.substring(0, 1).toUpperCase() + place_type_name_2.substring(1).toLowerCase();


        if (max > 0.1) {
            mBuilder.setContentTitle("Current Place").setContentText(mPlaceName+" Type: "+placeType);
            mNotificationManager.notify(001, mBuilder.build());
        } else {
            mBuilder.setContentTitle("Current Place").setContentText("Nothing");
            mNotificationManager.notify(001, mBuilder.build());
        }
        max = 0;


        String type = "NewPlace";

        if (!mPlace_id.isEmpty()) {
            BackgroundConnector backgroundConnector = new BackgroundConnector(this);
            backgroundConnector.execute(type, mPlace_id, mPlaceName, String.valueOf(mPlaceType), String.valueOf(mPlacelat), String.valueOf(mPlacelong), user_id);
        }
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {


        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }


    }

    @Override
    public void onLocationChanged(Location location) {

        if (locationBefore.getAltitude() != location.getAltitude() || locationBefore.getLongitude() != location.getLongitude()) {

            currentTime = System.currentTimeMillis();
            showCurrentPlace();


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


}
