package com.example.anas.movemeantapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class Groups extends Fragment {

    SharedPreferences sharedPreferences;
    String user_id;


    List<String> fam_mem;
    List<String> co_mem;
    List<String> ne_mem;
    List<String> fr_mem;

    List<String> members_names;
    List<String> members_ids;



    ListView famList;
    ListView coList;
    ListView neList;
    ListView frList;



    Button famAddBtn;
    Button coAddBtn;
    Button neAddBtn;
    Button frAddBtn;


    AdapterMembers adapterMembers;
    ListView membersList=null;
    String selectedMem;



    String no_members="You do not share any user this group";

    public Groups() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_groups, container, false);

        getActivity().setTitle("Groups");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        famAddBtn=(Button)v.findViewById(R.id.famAddBtn);
        coAddBtn=(Button)v.findViewById(R.id.coAddBtn);
        neAddBtn=(Button)v.findViewById(R.id.neAddBtn);
        frAddBtn=(Button)v.findViewById(R.id.frAddBtn);

        famList=(ListView) v.findViewById(R.id.famList);
        coList=(ListView)v.findViewById(R.id.coList);
        neList=(ListView)v.findViewById(R.id.neList);
        frList=(ListView)v.findViewById(R.id.frList);



        user_id=sharedPreferences.getString("user_id","");



        if (isConnected(getContext())) {
            final String typeGetGroupM = "GetGroupMembers";
            final String typeMale = "GetMale";




            final BackgroundConnector backgroundconnectorgetFam = new BackgroundConnector(getContext());
            backgroundconnectorgetFam.execute(typeGetGroupM,user_id,"1");

            final Timer timerfam = new Timer();
            TimerTask taskfam = new TimerTask() {
                @Override
                public void run() {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (backgroundconnectorgetFam.getStatus() == AsyncTask.Status.FINISHED) {
                                timerfam.cancel();

                                fam_mem = backgroundconnectorgetFam.getGroupMembers();

                                ListAdapter listAdapterFam = new ArrayAdapter<String>(getContext(), R.layout.reveal_row, fam_mem);
                                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) famList.getLayoutParams();
                                lp.height = listAdapterFam.getCount() * 85;
                                famList.setLayoutParams(lp);
                                famList.setAdapter(listAdapterFam);


                            }
                        }
                    });


                }
            };
            timerfam.scheduleAtFixedRate(taskfam, 1000, 1000);






            final BackgroundConnector backgroundconnectorgetCo = new BackgroundConnector(getContext());
            backgroundconnectorgetCo.execute(typeGetGroupM,user_id,"2");

            final Timer timerco = new Timer();
            TimerTask taskCo = new TimerTask() {
                @Override
                public void run() {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (backgroundconnectorgetCo.getStatus() == AsyncTask.Status.FINISHED) {
                                timerco.cancel();

                                co_mem = backgroundconnectorgetCo.getGroupMembers();

                                ListAdapter listAdapterCo = new ArrayAdapter<String>(getContext(), R.layout.reveal_row, co_mem);
                                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) coList.getLayoutParams();
                                lp.height = listAdapterCo.getCount() * 85;
                                coList.setLayoutParams(lp);
                                coList.setAdapter(listAdapterCo);


                            }
                        }
                    });


                }
            };
            timerco.scheduleAtFixedRate(taskCo, 1000, 1000);





            final BackgroundConnector backgroundconnectorgetNe = new BackgroundConnector(getContext());
            backgroundconnectorgetNe.execute(typeGetGroupM,user_id,"3");

            final Timer timerNe = new Timer();
            TimerTask taskNe = new TimerTask() {
                @Override
                public void run() {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (backgroundconnectorgetNe.getStatus() == AsyncTask.Status.FINISHED) {
                                timerNe.cancel();

                                ne_mem = backgroundconnectorgetNe.getGroupMembers();

                                ListAdapter listAdapterNe = new ArrayAdapter<String>(getContext(), R.layout.reveal_row, ne_mem);
                                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) neList.getLayoutParams();
                                lp.height = listAdapterNe.getCount() * 85;
                                neList.setLayoutParams(lp);
                                neList.setAdapter(listAdapterNe);


                            }
                        }
                    });


                }
            };
            timerNe.scheduleAtFixedRate(taskNe, 1000, 1000);


            final BackgroundConnector backgroundconnectorgetFr = new BackgroundConnector(getContext());
            backgroundconnectorgetFr.execute(typeGetGroupM,user_id,"4");

            final Timer timerFr = new Timer();
            TimerTask taskFr = new TimerTask() {
                @Override
                public void run() {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (backgroundconnectorgetFr.getStatus() == AsyncTask.Status.FINISHED) {
                                timerFr.cancel();

                                fr_mem = backgroundconnectorgetFr.getGroupMembers();

                                ListAdapter listAdapterFr = new ArrayAdapter<String>(getContext(), R.layout.reveal_row, fr_mem);
                                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) frList.getLayoutParams();
                                lp.height = listAdapterFr.getCount() * 85;
                                frList.setLayoutParams(lp);
                                frList.setAdapter(listAdapterFr);


                            }
                        }
                    });


                }
            };
            timerFr.scheduleAtFixedRate(taskFr, 1000, 1000);


        } else {

            Toast.makeText(getContext(), "Network is not available", Toast.LENGTH_LONG).show();
        }










        famAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                members_ids=new ArrayList<String>();
                members_names=new ArrayList<String>();


                if (isConnected(getContext())) {
                    final String typeGetM = "GetMembers";



                    final BackgroundConnector backgroundconnectorgetMem = new BackgroundConnector(getContext());
                    backgroundconnectorgetMem.execute(typeGetM, user_id);

                    final Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (backgroundconnectorgetMem.getStatus() == AsyncTask.Status.FINISHED) {
                                        timer.cancel();
                                        membersList=new ListView(getContext());

                                        members_ids = backgroundconnectorgetMem.getMembersIds();
                                        members_names=backgroundconnectorgetMem.getMembersNames();

                                        adapterMembers =new AdapterMembers(getContext(),members_names,members_ids);


                                        membersList.setAdapter(adapterMembers);

                                        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                                        builder.setCancelable(true);
                                        builder.setPositiveButton("Add to family",null);
                                        builder.setView(membersList);

                                        final AlertDialog dialog=builder.create();
                                        dialog.show();


                                        membersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                ViewGroup vg=(ViewGroup)view;
                                                TextView txtId=(TextView)vg.findViewById(R.id.textId);
                                                selectedMem= txtId.getText().toString();

                                                if (isConnected(getContext())) {
                                                    final String typeNewM = "NewMember";



                                                    final BackgroundConnector backgroundconnectorNewMem = new BackgroundConnector(getContext());
                                                    backgroundconnectorNewMem.execute(typeNewM, user_id,selectedMem,"1");

                                                    final Timer timer = new Timer();
                                                    TimerTask task = new TimerTask() {
                                                        @Override
                                                        public void run() {

                                                            getActivity().runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {

                                                                    if (backgroundconnectorNewMem.getStatus() == AsyncTask.Status.FINISHED) {
                                                                        timer.cancel();

                                                                        Groups groups= new Groups();
                                                                        FragmentManager fManger=getActivity().getSupportFragmentManager();
                                                                        fManger.beginTransaction().replace(R.id.content_layout,groups).commit();

                                                                        dialog.cancel();


                                                                    }
                                                                }
                                                            });


                                                        }
                                                    };
                                                    timer.scheduleAtFixedRate(task, 1000, 1000);
                                                }




                                            }
                                        });
                                    }
                                }
                            });


                        }
                    };
                    timer.scheduleAtFixedRate(task, 500, 1000);
                }







            }
        });





        coAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                members_ids=new ArrayList<String>();
                members_names=new ArrayList<String>();


                if (isConnected(getContext())) {
                    final String typeGetM = "GetMembers";



                    final BackgroundConnector backgroundconnectorgetMem = new BackgroundConnector(getContext());
                    backgroundconnectorgetMem.execute(typeGetM, user_id);

                    final Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (backgroundconnectorgetMem.getStatus() == AsyncTask.Status.FINISHED) {
                                        timer.cancel();
                                        membersList=new ListView(getContext());

                                        members_ids = backgroundconnectorgetMem.getMembersIds();
                                        members_names=backgroundconnectorgetMem.getMembersNames();

                                        adapterMembers =new AdapterMembers(getContext(),members_names,members_ids);


                                        membersList.setAdapter(adapterMembers);

                                        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                                        builder.setCancelable(true);
                                        builder.setPositiveButton("Add to Co-workers",null);
                                        builder.setView(membersList);

                                        final AlertDialog dialog=builder.create();
                                        dialog.show();


                                        membersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                ViewGroup vg=(ViewGroup)view;
                                                TextView txtId=(TextView)vg.findViewById(R.id.textId);
                                                selectedMem= txtId.getText().toString();

                                                if (isConnected(getContext())) {
                                                    final String typeNewM = "NewMember";



                                                    final BackgroundConnector backgroundconnectorNewMem = new BackgroundConnector(getContext());
                                                    backgroundconnectorNewMem.execute(typeNewM, user_id,selectedMem,"2");

                                                    final Timer timer = new Timer();
                                                    TimerTask task = new TimerTask() {
                                                        @Override
                                                        public void run() {

                                                            getActivity().runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {

                                                                    if (backgroundconnectorNewMem.getStatus() == AsyncTask.Status.FINISHED) {
                                                                        timer.cancel();

                                                                        Groups groups= new Groups();
                                                                        FragmentManager fManger=getActivity().getSupportFragmentManager();
                                                                        fManger.beginTransaction().replace(R.id.content_layout,groups).commit();

                                                                        dialog.cancel();


                                                                    }
                                                                }
                                                            });


                                                        }
                                                    };
                                                    timer.scheduleAtFixedRate(task, 1000, 1000);
                                                }




                                            }
                                        });
                                    }
                                }
                            });


                        }
                    };
                    timer.scheduleAtFixedRate(task, 500, 1000);
                }







            }
        });





        neAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                members_ids=new ArrayList<String>();
                members_names=new ArrayList<String>();


                if (isConnected(getContext())) {
                    final String typeGetM = "GetMembers";



                    final BackgroundConnector backgroundconnectorgetMem = new BackgroundConnector(getContext());
                    backgroundconnectorgetMem.execute(typeGetM, user_id);

                    final Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (backgroundconnectorgetMem.getStatus() == AsyncTask.Status.FINISHED) {
                                        timer.cancel();
                                        membersList=new ListView(getContext());

                                        members_ids = backgroundconnectorgetMem.getMembersIds();
                                        members_names=backgroundconnectorgetMem.getMembersNames();

                                        adapterMembers =new AdapterMembers(getContext(),members_names,members_ids);


                                        membersList.setAdapter(adapterMembers);

                                        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                                        builder.setCancelable(true);
                                        builder.setPositiveButton("Add to Neighbors",null);
                                        builder.setView(membersList);

                                        final AlertDialog dialog=builder.create();
                                        dialog.show();


                                        membersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                ViewGroup vg=(ViewGroup)view;
                                                TextView txtId=(TextView)vg.findViewById(R.id.textId);
                                                selectedMem= txtId.getText().toString();

                                                if (isConnected(getContext())) {
                                                    final String typeNewM = "NewMember";



                                                    final BackgroundConnector backgroundconnectorNewMem = new BackgroundConnector(getContext());
                                                    backgroundconnectorNewMem.execute(typeNewM, user_id,selectedMem,"3");

                                                    final Timer timer = new Timer();
                                                    TimerTask task = new TimerTask() {
                                                        @Override
                                                        public void run() {

                                                            getActivity().runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {

                                                                    if (backgroundconnectorNewMem.getStatus() == AsyncTask.Status.FINISHED) {
                                                                        timer.cancel();

                                                                        Groups groups= new Groups();
                                                                        FragmentManager fManger=getActivity().getSupportFragmentManager();
                                                                        fManger.beginTransaction().replace(R.id.content_layout,groups).commit();

                                                                        dialog.cancel();


                                                                    }
                                                                }
                                                            });


                                                        }
                                                    };
                                                    timer.scheduleAtFixedRate(task, 1000, 1000);
                                                }




                                            }
                                        });
                                    }
                                }
                            });


                        }
                    };
                    timer.scheduleAtFixedRate(task, 500, 1000);
                }







            }
        });




        frAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                members_ids=new ArrayList<String>();
                members_names=new ArrayList<String>();


                if (isConnected(getContext())) {
                    final String typeGetM = "GetMembers";



                    final BackgroundConnector backgroundconnectorgetMem = new BackgroundConnector(getContext());
                    backgroundconnectorgetMem.execute(typeGetM, user_id);

                    final Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (backgroundconnectorgetMem.getStatus() == AsyncTask.Status.FINISHED) {
                                        timer.cancel();
                                        membersList=new ListView(getContext());

                                        members_ids = backgroundconnectorgetMem.getMembersIds();
                                        members_names=backgroundconnectorgetMem.getMembersNames();

                                        adapterMembers =new AdapterMembers(getContext(),members_names,members_ids);


                                        membersList.setAdapter(adapterMembers);

                                        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                                        builder.setCancelable(true);
                                        builder.setPositiveButton("Add to friends",null);
                                        builder.setView(membersList);

                                        final AlertDialog dialog=builder.create();
                                        dialog.show();


                                        membersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                ViewGroup vg=(ViewGroup)view;
                                                TextView txtId=(TextView)vg.findViewById(R.id.textId);
                                                selectedMem= txtId.getText().toString();

                                                if (isConnected(getContext())) {
                                                    final String typeNewM = "NewMember";



                                                    final BackgroundConnector backgroundconnectorNewMem = new BackgroundConnector(getContext());
                                                    backgroundconnectorNewMem.execute(typeNewM, user_id,selectedMem,"4");

                                                    final Timer timer = new Timer();
                                                    TimerTask task = new TimerTask() {
                                                        @Override
                                                        public void run() {

                                                            getActivity().runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {

                                                                    if (backgroundconnectorNewMem.getStatus() == AsyncTask.Status.FINISHED) {
                                                                        timer.cancel();

                                                                        Groups groups= new Groups();
                                                                        FragmentManager fManger=getActivity().getSupportFragmentManager();
                                                                        fManger.beginTransaction().replace(R.id.content_layout,groups).commit();

                                                                        dialog.cancel();


                                                                    }
                                                                }
                                                            });


                                                        }
                                                    };
                                                    timer.scheduleAtFixedRate(task, 1000, 1000);
                                                }




                                            }
                                        });
                                    }
                                }
                            });


                        }
                    };
                    timer.scheduleAtFixedRate(task, 500, 1000);
                }







            }
        });


        return v;
    }


    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();


        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
