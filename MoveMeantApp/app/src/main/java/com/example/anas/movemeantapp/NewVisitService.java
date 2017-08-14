package com.example.anas.movemeantapp;

import android.Manifest;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
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
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import java.util.Timer;
import java.util.TimerTask;

public class NewVisitService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private GoogleApiClient mGoogleApiClient;

    String mPlace_id;
    String mPlaceName;
    int mPlaceType;

    float max = 0;
    float second_max = 0;
    int second_maxItem;
    int max_item;
    int place_item;
    boolean isSeconitem = false;
    boolean worthy = false;
    Location currentLoc=new Location("");

    LocationRequest mLocationRequest;

    NotificationManager mNotificationManager;
    NotificationCompat.Builder mBuilder;

    int timePassed;

    LatLng locationBefore = new LatLng(50.977703, 11.036397);
    boolean theUserhasSettled = true;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private String  user_id;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    public static boolean mRunning;
    public NewVisitService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();


        mRunning=true;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(1000 );
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        // Build the Play services client for use by the Fused Location Provider and the Places API.
        // Use the addApi() method to request the Google Places API and the Fused Location Provider.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        mGoogleApiClient.connect();



        mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.running);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        user_id = sharedPreferences.getString("user_id", "");
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        mRunning=false;
        user_id="";

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
        Toast.makeText(this,"Service Stopped",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        checkLocation();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {


        double subLatBefore = 0.0, subLngBefore = 0.0, subLoclat = 0.0, subLoclng = 0.0;

        currentLoc=location;
        subLatBefore = Double.parseDouble(Double.toString(locationBefore.latitude).substring(0, 7));
        subLngBefore = Double.parseDouble(Double.toString(locationBefore.longitude).substring(0, 7));
        subLoclat = Double.parseDouble(Double.toString(location.getLatitude()).substring(0, 7));
        subLoclng = Double.parseDouble(Double.toString(location.getLongitude()).substring(0, 7));

        if (isConnected(this)) {
            if (theUserhasSettled) {
                if (subLatBefore != subLoclat || subLngBefore != subLoclng) {

                    showCurrentPlace();
                }
            }
        } else {
            Toast.makeText(this, "Network is not available", Toast.LENGTH_SHORT).show();}

    }


    private void showCurrentPlace() {

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationBefore = new LatLng(LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient).getLatitude(), LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient).getLongitude());


        timePassed = 0;
        theUserhasSettled = false;
        final Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                timePassed++;
                Log.i("TimePAsssssed", String.valueOf(timePassed));
                if (timePassed >= 1) {
                    timer.cancel();

                    timePassed = 0;

                    theUserhasSettled = true;

                        mBuilder.setContentText("Recognizing you place").setContentTitle("place");
                        mNotificationManager.notify(001, mBuilder.build());
                        mPlaceName = "";
                        mPlaceType = 0;
                        // Get the likely places - that is, the businesses and other points of interest that
                        // are the best match for the device's current location.
                        @SuppressWarnings("MissingPermission")
                        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient,null);
                        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                            @Override
                            public void onResult(@NonNull PlaceLikelihoodBuffer likelyPlaces) {


                                try{



                                    max = likelyPlaces.get(0).getLikelihood();
                                    second_max = likelyPlaces.get(1).getLikelihood();
                                    max_item = 0;
                                    second_maxItem = 1;


                                    if (likelyPlaces.get(max_item).getPlace().getPlaceTypes().get(0) == Place.TYPE_STREET_ADDRESS) {
                                        place_item = second_maxItem;
                                        isSeconitem = true;
                                    } else {
                                        place_item = max_item;
                                        isSeconitem = false;
                                    }


                                    mPlaceName = (String) likelyPlaces.get(place_item).getPlace().getName();
                                    mPlaceType = likelyPlaces.get(place_item).getPlace().getPlaceTypes().get(0);
                                    mPlace_id = likelyPlaces.get(place_item).getPlace().getId();
                                    Log.i("Placeeeee IIIID",mPlace_id);


                                    // Release the place likelihood buffer, to avoid memory leaks.
                                    likelyPlaces.release();


                                    Places.GeoDataApi.getPlaceById(mGoogleApiClient, mPlace_id).setResultCallback(new ResultCallback<PlaceBuffer>() {
                                        @Override
                                        public void onResult(@NonNull PlaceBuffer places) {
                                            if (places.getStatus().isSuccess() && places.getCount() > 0) {
                                                final Place myPlace = places.get(0);
                                                Log.i("Place name",myPlace.getName().toString());

                                            } else {

                                            }
                                            places.release();
                                        }
                                    });

                                    // Show a dialog offering the user the list of likely places, and add a
                                    // marker at the selected place.
                                    if (mPlaceType != Place.TYPE_STREET_ADDRESS) {
                                        openPlacesDialog();
                                    } else {
                                        mBuilder.setContentText("Street").setContentTitle("Place Type");
                                        mNotificationManager.notify(001, mBuilder.build());
                                    }


                                } catch (SecurityException e) {
                                    e.printStackTrace();
                                }
                                catch (IllegalStateException e)
                                {
                                    e.printStackTrace();
                                }

                            }

                        });





                }

            }

        };
        timer.scheduleAtFixedRate(task, 1000, 1000);


    }

    private void openPlacesDialog() {

        int place_type_number = mPlaceType;
        String place_type_name_1 = getPlaceType(place_type_number);
        String place_type_name_2 = place_type_name_1.replaceAll("_", " ");
        String placeType = place_type_name_2.substring(0, 1).toUpperCase() + place_type_name_2.substring(1).toLowerCase();


        if (!isSeconitem) {

            if (max >= 0.0) {

                mBuilder.setContentText(placeType + " max: " + max).setContentTitle("Place Type");
                mNotificationManager.notify(001, mBuilder.build());

                mBuilder.setContentTitle("PLace Name").setContentText(mPlaceName + " max: " + max);
                mNotificationManager.notify(003, mBuilder.build());
            } else {
                mBuilder.setContentTitle("Place").setContentText("Nothing:firs " + max);
                mNotificationManager.notify(001, mBuilder.build());
                mBuilder.setContentTitle("PLace Name").setContentText("Nothing");
                mNotificationManager.notify(003, mBuilder.build());
            }

        } else if (second_max > 0.0) {

            mBuilder.setContentText(placeType + " secMax: " + second_max).setContentTitle("Place Type");
            mNotificationManager.notify(001, mBuilder.build());

            mBuilder.setContentTitle("PLace Name").setContentText(mPlaceName + " secMax: " + second_max);
            mNotificationManager.notify(003, mBuilder.build());
        } else {
            mBuilder.setContentTitle("Place").setContentText("Nothing: sec: " + second_max);
            mNotificationManager.notify(001, mBuilder.build());
            mBuilder.setContentTitle("PLace Name").setContentText("Nothing");
            mNotificationManager.notify(003, mBuilder.build());
        }




        if (!mPlace_id.isEmpty()) {
            if (!isSeconitem) {
                if (max >= 0.0)
                    worthy = true;
                else worthy = false;
            } else {
                if (second_max > 0.0)
                    worthy = true;
                else worthy = false;
            }

            if (worthy) {

                String type = "NewVisit";
                BackgroundConnector backgroundConnector = new BackgroundConnector(this);
                backgroundConnector.execute(type, mPlace_id, user_id);
            }
        }

        max = 0;
        second_max = 0;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();



        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
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

    private void checkLocation()
    {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());


        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {

                final Status status = locationSettingsResult.getStatus();
                final LocationSettingsStates LS_state = locationSettingsResult.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:



                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.

                           Toast.makeText(getApplicationContext(),"Please Enable Location",Toast.LENGTH_LONG).show();

                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.

                        break;
                }
            }
        });
    }

    public  Location getCurrentLoc()
    {

        return currentLoc;
    }


}
