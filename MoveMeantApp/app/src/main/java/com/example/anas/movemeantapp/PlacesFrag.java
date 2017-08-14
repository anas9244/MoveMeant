package com.example.anas.movemeantapp;


import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlacesFrag extends Fragment implements OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;


    LocationRequest mLocationRequest;

    NotificationManager mNotificationManager;
    NotificationCompat.Builder mBuilder;

    Button button;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String user_id;

    String android_id;
    private GoogleMap mMap;
    private MapView mapView;
    private SeekBar seekBar;
    private TextView textView;
    ListView placeslist;

    LatLng currentLatLng;
    Circle circle;
    boolean initCircle = true;
    int mprogress;
    List<String> placesID = new ArrayList<>();
    List<String> numOfvisits = new ArrayList<>();

    List<String> placesType = new ArrayList<>();
    List<String> placesName = new ArrayList<>();
    List<LatLng> placesLoc = new ArrayList<>();

    List<String> placeIdIndex = new ArrayList<>();
    List<String> numOfvisitsIndex = new ArrayList<>();
    AdapterPlaces adapterPlaces;

    Location currentLoc = new Location("");
    Location placeLoc = new Location("");
    Marker marker;


    private GoogleApiClient mGoogleApiClient;


    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    public PlacesFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_places, container, false);


        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        mGoogleApiClient.connect();


        textView = (TextView) v.findViewById(R.id.textView2);
        seekBar = (SeekBar) v.findViewById(R.id.seekBar);
        placeslist = (ListView) v.findViewById(R.id.placeslist);

        textView.setText("Radius\n" + String.valueOf(seekBar.getProgress()) + " m");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mprogress=progress;
                circle.setRadius(progress);
                circle.setCenter(new LatLng(currentLatLng.latitude, currentLatLng.longitude));
                textView.setText("Radius\n" + String.valueOf(seekBar.getProgress()) + " m");


                if (progress > 2000) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(currentLatLng.latitude,
                                    currentLatLng.longitude), 12));
                } else if (progress > 1060 && progress < 2000) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(currentLatLng.latitude,
                                    currentLatLng.longitude), 13));
                } else if (progress < 1060 && progress > 600) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(currentLatLng.latitude,
                                    currentLatLng.longitude), 14));
                } else if (progress < 600 && progress>160) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(currentLatLng.latitude,
                                    currentLatLng.longitude), 15));
                } else if (progress < 160) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(currentLatLng.latitude,
                                    currentLatLng.longitude), 16));
                }



            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                fillList(mprogress,currentLoc);
                placeslist.setAdapter(adapterPlaces);
            }
        });

        android_id = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        if (ContextCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;

            if (!NewVisitService.mRunning)

                getContext().startService(new Intent(getActivity(), NewVisitService.class));


        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        mBuilder = new NotificationCompat.Builder(getContext());
        mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        mBuilder.setContentTitle("Device ID").setContentText(android_id).setSmallIcon(R.drawable.running);
        mNotificationManager.notify(004, mBuilder.build());


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        user_id = sharedPreferences.getString("user_id", "");


        mBuilder.setContentText("User id= " + user_id).setContentTitle("User ID");
        mNotificationManager.notify(002, mBuilder.build());


        if (isConnected(getContext())) {
            String type = "GetVisits";
            final BackgroundConnector backgroundConnector = new BackgroundConnector(getContext());
            backgroundConnector.execute(type);

            final Timer timer1 = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    }
                    currentLoc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    if (backgroundConnector.getStatus() == AsyncTask.Status.FINISHED && currentLoc!=null) {
                        timer1.cancel();
                        placeIdIndex=backgroundConnector.getPlacesID();
                        numOfvisitsIndex=backgroundConnector.getNumOFVisits();



                      fillList(1000,currentLoc);
                       /*
                        adapterPlaces=new AdapterPlaces(getContext(), placesName, placesType, numOfvisits);
                        for (int i = 0; i < placeIdIndex.size(); i++) {
                            final int pos = i;

                            Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeIdIndex.get(i)).setResultCallback(new ResultCallback<PlaceBuffer>() {
                                @Override
                                public void onResult(@NonNull PlaceBuffer places) {

                                    if (places.getStatus().isSuccess() && places.getCount() > 0) {
                                        Place myPlace = places.get(0);
                                        placesLoc.add(myPlace.getLatLng());



                                        placeLoc.setLatitude(placesLoc.get(pos).latitude);
                                        placeLoc.setLongitude(placesLoc.get(pos).longitude);

                                        if (currentLoc.distanceTo(placeLoc)<100)
                                        {
                                            placesID.add(backgroundConnector.getPlacesID().get(pos));
                                            numOfvisits.add(backgroundConnector.getNumOFVisits().get(pos));
                                            placesName.add(myPlace.getName().toString());
                                            placesType.add(getPlaceType(myPlace.getPlaceTypes().get(0)));
                                            adapterPlaces.notifyDataSetChanged();
                                        }
                                    } else {

                                    }
                                    places.release();



                                }
                            });



                        }*/




                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                placeslist.setAdapter(adapterPlaces);

                            }
                        });

                    }
                }
            };
            timer1.scheduleAtFixedRate(task, 2000, 1000);


        } else {
            Toast.makeText(getContext(), "Network is not available", Toast.LENGTH_LONG).show();
        }


        placeslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);

                if (marker != null) {
                    marker.remove();
                }
                marker = mMap.addMarker(new MarkerOptions()
                        .position(
                                new LatLng(placesLoc.get(position).latitude,
                                        placesLoc.get(position).longitude)).visible(true).title(placesName.get(position)));
                //mMap.addMarker(new MarkerOptions().position(new LatLng(placesLoc.get(position).latitude,placesLoc.get(position).longitude)).title(placesName.get(position)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(placesLoc.get(position).latitude, placesLoc.get(position).longitude)));
            }
        });


        return v;

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
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
                        getContext().startService(new Intent(getActivity(), NewVisitService.class));

                }
            }
        }

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

        int place_type_number = i;
        String place_type_name_1 = placeType;
        String place_type_name_2 = place_type_name_1.replaceAll("_", " ");
        String mplaceType = place_type_name_2.substring(0, 1).toUpperCase() + place_type_name_2.substring(1).toLowerCase();
        return mplaceType;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

            @Override
            public void onMyLocationChange(Location arg0) {
                // TODO Auto-generated method stub

                currentLatLng = new LatLng(arg0.getLatitude(), arg0.getLongitude());

                // mMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("It's Me!"));
                if (initCircle) {
                    initCircle = false;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(arg0.getLatitude(),
                                    arg0.getLongitude()), 14));
                    circle = mMap.addCircle(new CircleOptions()
                            .center(new LatLng(currentLatLng.latitude, currentLatLng.longitude))
                            .radius(1000)
                            .strokeWidth(3)
                            .strokeColor(0x400000ff)
                            .fillColor(0x150000ff));
                }

            }
        });
    }


    private void fillList(int progress,Location currentLocation){

        placesID.clear();
        numOfvisits.clear();
        placesType.clear();
        placesName.clear();
        placesLoc.clear();

        adapterPlaces=new AdapterPlaces(getContext(), placesName, placesType, numOfvisits);
        for (int i = 0; i < placeIdIndex.size(); i++) {
            final int pos = i;
            final int mprogress=progress;
            final Location curLoc=currentLocation;

            Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeIdIndex.get(i)).setResultCallback(new ResultCallback<PlaceBuffer>() {
                @Override
                public void onResult(@NonNull PlaceBuffer places) {

                    if (places.getStatus().isSuccess() && places.getCount() > 0) {
                        Place myPlace = places.get(0);


                        placeLoc.setLatitude(myPlace.getLatLng().latitude);
                        placeLoc.setLongitude(myPlace.getLatLng().longitude);

                        if (curLoc.distanceTo(placeLoc)<mprogress)
                        {
                            placesLoc.add(myPlace.getLatLng());
                            placesID.add(placeIdIndex.get(pos));
                            numOfvisits.add(numOfvisitsIndex.get(pos));
                            placesName.add(myPlace.getName().toString());
                            placesType.add(getPlaceType(myPlace.getPlaceTypes().get(0)));
                            adapterPlaces.notifyDataSetChanged();
                        }
                    } else {

                    }
                    places.release();



                }
            });



        }
    }
}
