package com.example.anas.movemeantapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class PlaceDetails extends AppCompatActivity implements OnMapReadyCallback {


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String user_id;

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


    TextView textVisitStatus;

    LinearLayout FamLayout;
    LinearLayout coLayout;
    LinearLayout neLayout;
    LinearLayout frLayout;

    Button famReavealBtn;
    Button coReavealBtn;
    Button neReavealBtn;
    Button frReavealBtn;

    ListView famList;
    ListView coList;
    ListView neList;
    ListView frList;

    List<String> fam_reveals;
    List<String> co_reveals;
    List<String> ne_reveals;
    List<String> fr_reveals;


    String not_revealed="Plesae reveal your idenity to view who reveald thier id here";


    boolean is_visited;

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

        textVisitStatus=(TextView)findViewById(R.id.textVisitStatus);


        FamLayout=(LinearLayout)findViewById(R.id.FamLayout);
        coLayout=(LinearLayout)findViewById(R.id.coLayout);
        neLayout=(LinearLayout)findViewById(R.id.neLayout);
        frLayout=(LinearLayout)findViewById(R.id.frLayout);

        famReavealBtn=(Button)findViewById(R.id.famReavealBtn);
        coReavealBtn=(Button)findViewById(R.id.coReavealBtn);
        neReavealBtn=(Button)findViewById(R.id.neReavealBtn);
        frReavealBtn=(Button)findViewById(R.id.frReavealBtn);

        famList=(ListView) findViewById(R.id.famList);
        coList=(ListView)findViewById(R.id.coList);
        neList=(ListView)findViewById(R.id.neList);
        frList=(ListView)findViewById(R.id.frList);




        selectedPName=sharedPreferences.getString("selectedPName","");
        selectedPtype=sharedPreferences.getString("selectedPtype","");
        selectedPid=sharedPreferences.getString("selectedPid","");
        selectedNumofvis=sharedPreferences.getString("selectedNumofvis","");
        selectedPlat=sharedPreferences.getString("selectedPlat","");
        selectedPlong=sharedPreferences.getString("selectedPlong","");

        user_id=sharedPreferences.getString("user_id","");


        if (isConnected(this)) {
            final String typeFemale = "GetFemale";
            final String typeMale = "GetMale";
            final String typeCheckVisited = "CheckVisited";
            final String typeFamReveals = "FamReveals";
            final String typeCoReveals = "CoReveals";
            final String typeNeReveals = "NeReveals";
            final String typeFrReveals = "FrReveals";


            final BackgroundConnector backgroundconnectorFemale = new BackgroundConnector(this);
            backgroundconnectorFemale.execute(typeFemale, selectedPid);

            final Timer timerfemale = new Timer();
            TimerTask taskfemale = new TimerTask() {
                @Override
                public void run() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (backgroundconnectorFemale.getStatus() == AsyncTask.Status.FINISHED) {
                                timerfemale.cancel();
                                if (!sharedPreferences.getString("female_num", "").isEmpty()) {

                                    female_num = sharedPreferences.getString("female_num", "");
                                    textFemale.setText("Female: " + female_num);

                                } else {
                                    female_num = "0";
                                }


                            }
                        }
                    });


                }
            };
            timerfemale.scheduleAtFixedRate(taskfemale, 1000, 1000);


            final BackgroundConnector backgroundconnectormale = new BackgroundConnector(this);
            backgroundconnectormale.execute(typeMale, selectedPid);

            final Timer timermale = new Timer();
            TimerTask taskmale = new TimerTask() {
                @Override
                public void run() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (backgroundconnectormale.getStatus() == AsyncTask.Status.FINISHED) {
                                timermale.cancel();
                                if (!sharedPreferences.getString("male_num", "").isEmpty()) {

                                    male_num = sharedPreferences.getString("male_num", "");
                                    textMale.setText("Male: " + male_num);

                                } else {
                                    male_num = "0";
                                }
                            }
                        }
                    });


                }
            };
            timermale.scheduleAtFixedRate(taskmale, 1000, 1000);


            final BackgroundConnector backgroundconnectorCheck = new BackgroundConnector(this);
            backgroundconnectorCheck.execute(typeCheckVisited, selectedPid, user_id);

            final Timer timercheck = new Timer();
            TimerTask taskcheck = new TimerTask() {
                @Override
                public void run() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (backgroundconnectorCheck.getStatus() == AsyncTask.Status.FINISHED) {
                                timercheck.cancel();
                                if (backgroundconnectorCheck.isVisited()) {

                                    is_visited = true;
                                    textVisitStatus.setText("You have visited this place");


                                } else {
                                    is_visited = false;
                                    textVisitStatus.setText("You have not visited this place");

                                    FamLayout.setVisibility(View.GONE);
                                    coLayout.setVisibility(View.GONE);
                                    neLayout.setVisibility(View.GONE);
                                    frLayout.setVisibility(View.GONE);

                                }
                            }
                        }
                    });


                }
            };
            timercheck.scheduleAtFixedRate(taskcheck, 1000, 1000);






            final Timer timerVisited = new Timer();
            TimerTask timerTask= new TimerTask() {
                @Override
                public void run() {
                    if (is_visited)
                    {
                        timerVisited.cancel();

                    final BackgroundConnector backgroundconnectorfamReveal = new BackgroundConnector(getApplicationContext());
                    backgroundconnectorfamReveal.execute(typeFamReveals, selectedPid, user_id);

                    final Timer timerfamReveal = new Timer();
                    TimerTask taskfamReveal = new TimerTask() {
                        @Override
                        public void run() {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (backgroundconnectorfamReveal.getStatus() == AsyncTask.Status.FINISHED) {
                                        timerfamReveal.cancel();

                                        fam_reveals = backgroundconnectorfamReveal.getFamReveals();

                                        ListAdapter listAdapterFam = new ArrayAdapter<String>(getApplicationContext(),R.layout.reveal_row, fam_reveals);
                                        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) famList.getLayoutParams();
                                        lp.height = listAdapterFam.getCount()* 85;
                                        famList.setLayoutParams(lp);
                                        famList.setAdapter(listAdapterFam);



                                        if (!fam_reveals.get(0).equals(not_revealed)){
                                            famReavealBtn.setText("Revealed");
                                            famReavealBtn.setEnabled(false);
                                        }
                                    }
                                }
                            });


                        }
                    };
                    timerfamReveal.scheduleAtFixedRate(taskfamReveal, 1000, 1000);


                    final BackgroundConnector backgroundconnectorcoReveal = new BackgroundConnector(getApplicationContext());
                    backgroundconnectorcoReveal.execute(typeCoReveals, selectedPid, user_id);

                    final Timer timercoReveal = new Timer();
                    TimerTask taskcoReveal = new TimerTask() {
                        @Override
                        public void run() {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (backgroundconnectorcoReveal.getStatus() == AsyncTask.Status.FINISHED) {
                                        timercoReveal.cancel();

                                        co_reveals = backgroundconnectorcoReveal.getCoReveals();
                                        ListAdapter listAdapterCo = new ArrayAdapter<String>(getApplicationContext(), R.layout.reveal_row, co_reveals);
                                        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) coList.getLayoutParams();
                                        lp.height = listAdapterCo.getCount()* 90;
                                        coList.setLayoutParams(lp);
                                        coList.setAdapter(listAdapterCo);

                                        if (!co_reveals.get(0).equals(not_revealed)){
                                            coReavealBtn.setText("Revealed");
                                            coReavealBtn.setEnabled(false);
                                        }


                                    }
                                }
                            });


                        }
                    };
                    timercoReveal.scheduleAtFixedRate(taskcoReveal, 1000, 1000);


                    final BackgroundConnector backgroundconnectorNeReveals = new BackgroundConnector(getApplicationContext());
                    backgroundconnectorNeReveals.execute(typeNeReveals, selectedPid, user_id);

                    final Timer timerNeReveal = new Timer();
                    TimerTask taskNeReveal = new TimerTask() {
                        @Override
                        public void run() {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (backgroundconnectorNeReveals.getStatus() == AsyncTask.Status.FINISHED) {
                                        timerNeReveal.cancel();

                                        ne_reveals = backgroundconnectorNeReveals.getNeReveals();

                                        ListAdapter listAdapterNe = new ArrayAdapter<String>(getApplicationContext(), R.layout.reveal_row, ne_reveals);
                                        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) neList.getLayoutParams();
                                        lp.height = listAdapterNe.getCount()* 90;
                                        neList.setLayoutParams(lp);
                                        neList.setAdapter(listAdapterNe);


                                        if (!ne_reveals.get(0).equals(not_revealed)){
                                            neReavealBtn.setText("Revealed");
                                            neReavealBtn.setEnabled(false);
                                        }
                                    }
                                }
                            });


                        }
                    };
                    timerNeReveal.scheduleAtFixedRate(taskNeReveal, 1000, 1000);


                    final BackgroundConnector backgroundconnectorFrReveals = new BackgroundConnector(getApplicationContext());
                    backgroundconnectorFrReveals.execute(typeFrReveals, selectedPid, user_id);

                    final Timer timerFrReveal = new Timer();
                    TimerTask taskFrReveal = new TimerTask() {
                        @Override
                        public void run() {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (backgroundconnectorFrReveals.getStatus() == AsyncTask.Status.FINISHED) {
                                        timerFrReveal.cancel();

                                        fr_reveals = backgroundconnectorFrReveals.getFrReveals();
                                        ListAdapter listAdapterFr = new ArrayAdapter<String>(getApplicationContext(), R.layout.reveal_row, fr_reveals);
                                        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) frList.getLayoutParams();
                                        lp.height = listAdapterFr.getCount()* 90;
                                        frList.setLayoutParams(lp);
                                        frList.setAdapter(listAdapterFr);



                                        if (!fr_reveals.get(0).equals(not_revealed)){
                                            frReavealBtn.setText("Revealed");
                                            frReavealBtn.setEnabled(false);
                                        }
                                    }
                                }
                            });


                        }
                    };
                    timerFrReveal.scheduleAtFixedRate(taskFrReveal, 1000, 1000);


                }
                }
            };
            timerVisited.scheduleAtFixedRate(timerTask,500,500);









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


        famReavealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final BackgroundConnector backgroundConnector = new BackgroundConnector(getApplicationContext());
                backgroundConnector.execute("Reveal", selectedPid, user_id, "1");

                final Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (backgroundConnector.getStatus() == AsyncTask.Status.FINISHED) {
                                    timer.cancel();

                                    fam_reveals = backgroundConnector.getAfterReveal();

                                    ListAdapter listAdapterFam = new ArrayAdapter<String>(getApplicationContext(),R.layout.reveal_row, fam_reveals);
                                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) famList.getLayoutParams();
                                    lp.height = listAdapterFam.getCount()* 85;
                                    famList.setLayoutParams(lp);
                                    famList.setAdapter(listAdapterFam);




                                    famReavealBtn.setText("Revealed");
                                    famReavealBtn.setEnabled(false);


                                }
                            }
                        });


                    }
                };
                timer.scheduleAtFixedRate(task, 1000, 1000);

            }
        });



        coReavealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final BackgroundConnector backgroundConnector = new BackgroundConnector(getApplicationContext());
                backgroundConnector.execute("Reveal", selectedPid, user_id, "2");

                final Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (backgroundConnector.getStatus() == AsyncTask.Status.FINISHED) {
                                    timer.cancel();

                                    co_reveals = backgroundConnector.getAfterReveal();

                                    ListAdapter listAdapterCo = new ArrayAdapter<String>(getApplicationContext(),R.layout.reveal_row, co_reveals);
                                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) coList.getLayoutParams();
                                    lp.height = listAdapterCo.getCount()* 85;
                                    coList.setLayoutParams(lp);
                                    coList.setAdapter(listAdapterCo);




                                    coReavealBtn.setText("Revealed");
                                    coReavealBtn.setEnabled(false);


                                }
                            }
                        });


                    }
                };
                timer.scheduleAtFixedRate(task, 1000, 1000);

            }
        });


        neReavealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BackgroundConnector backgroundConnector = new BackgroundConnector(getApplicationContext());
                backgroundConnector.execute("Reveal", selectedPid, user_id, "3");

                final Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (backgroundConnector.getStatus() == AsyncTask.Status.FINISHED) {
                                    timer.cancel();

                                    ne_reveals = backgroundConnector.getAfterReveal();

                                    ListAdapter listAdapterNe = new ArrayAdapter<String>(getApplicationContext(),R.layout.reveal_row, ne_reveals);
                                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) neList.getLayoutParams();
                                    lp.height = listAdapterNe.getCount()* 85;
                                    neList.setLayoutParams(lp);
                                    neList.setAdapter(listAdapterNe);




                                    neReavealBtn.setText("Revealed");
                                    neReavealBtn.setEnabled(false);


                                }
                            }
                        });


                    }
                };
                timer.scheduleAtFixedRate(task, 1000, 1000);

            }
        });


        frReavealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BackgroundConnector backgroundConnector = new BackgroundConnector(getApplicationContext());
                backgroundConnector.execute("Reveal", selectedPid, user_id, "4");

                final Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (backgroundConnector.getStatus() == AsyncTask.Status.FINISHED) {
                                    timer.cancel();

                                    fr_reveals = backgroundConnector.getAfterReveal();

                                    ListAdapter listAdapterFr = new ArrayAdapter<String>(getApplicationContext(),R.layout.reveal_row, fr_reveals);
                                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) frList.getLayoutParams();
                                    lp.height = listAdapterFr.getCount()* 85;
                                    frList.setLayoutParams(lp);
                                    frList.setAdapter(listAdapterFr);




                                    neReavealBtn.setText("Revealed");
                                    neReavealBtn.setEnabled(false);


                                }
                            }
                        });


                    }
                };
                timer.scheduleAtFixedRate(task, 1000, 1000);


            }
        });




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
