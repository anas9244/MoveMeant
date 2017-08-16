package com.example.anas.movemeantapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectStreamException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Created by Anas on 04.07.2017.
 */

public class BackgroundConnector extends AsyncTask<String, Void, JSONArray> {

    Context context;

    String result = "";

    BackgroundConnector(Context ctx) {
        context = ctx;
    }

    boolean error=false;

    String user_id = "";
    String user_name="";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    String type;
    List<String> placesID;
    List<String> numOFVisits;


    List<String> famReveals;
    List<String> coReveals;
    List<String> neReveals;
    List<String> frReveals;

    List<String> afterReveal;

    List<String> groupMembers;

    List<String> membersNames;
    List<String> membersIds;




    String femaleNum;
    String maleNum;

    boolean isVisited;
    private static StringBuffer visitsBuffer = new StringBuffer();

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

        if (error)
        {
            Toast.makeText(context,"Can't connect to server",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected JSONArray doInBackground(String... params) {
        type = params[0];
        String login_url = "http://141.54.154.231:1234/LogIn.php";
        String newPlace_url = "http://141.54.154.231:1234/NewVisit.php";
        String getVisits_url = "http://141.54.154.231:1234/GetVisited.php";
        String newMember_url = "http://141.54.154.231:1234/NewMember.php";
        String getMembers_url = "http://141.54.154.231:1234/GetMembers.php";
        String getFemale_url = "http://141.54.154.231:1234/GetFemale.php";
        String getMale_url = "http://141.54.154.231:1234/GetMale.php";
        String checkVisited_url="http://141.54.154.231:1234/CheckVisited.php";
        String reveal_url = "http://141.54.154.231:1234/Reveal.php";
        String famReveals_url = "http://141.54.154.231:1234/GetFamReveal.php";
        String coReveals_url = "http://141.54.154.231:1234/GetCoReveal.php";
        String neReveals_url = "http://141.54.154.231:1234/GetNeReveal.php";
        String frReveals_url = "http://141.54.154.231:1234/GetFrReveal.php";
        String getGroupMembers_url= "http://141.54.154.231:1234/GetGroupMembers.php";




        if (type.equals("login")) {
            try {
                String user_name = params[1];
                String password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setReadTimeout(6000);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();


                JSONArray jsonArray = new JSONArray(result);


                return jsonArray;
            } catch (MalformedURLException e) {

                e.printStackTrace();
            } catch (IOException e) {
                if (e instanceof SocketTimeoutException)
                    error=true;
                if (e instanceof ConnectException)
                    error=true;
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else if (type.equals("NewVisit")) {
            try {
                String place_id = params[1];
                String user_id = params[2];
                URL url = new URL(newPlace_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setReadTimeout(6000);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("place_id", "UTF-8") + "=" + URLEncoder.encode(place_id, "UTF-8")
                        + "&" + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                JSONArray jsonArray = new JSONArray(result);

                return jsonArray;

            } catch (MalformedURLException e) {
                error=true;
                e.printStackTrace();
            } catch (IOException e) {
                if (e instanceof SocketTimeoutException)
                    error=true;
                if (e instanceof ConnectException)
                    error=true;
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (type.equals("GetVisits")) {

            try {

                URL url = new URL(getVisits_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));


                String inputBuffer = "";
                while ((inputBuffer = in.readLine()) != null) {
                    result += inputBuffer;
                }

                in.close();
                httpURLConnection.disconnect();

                JSONArray jsonArray = new JSONArray(result);

                return jsonArray;

            } catch (MalformedURLException e) {
                error=true;
                e.printStackTrace();
            } catch (IOException e) {
                if (e instanceof SocketTimeoutException)
                    error=true;
                if (e instanceof ConnectException)
                    error=true;
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else if (type.equals("NewMember")) {
            try {
                String user_id = params[1];
                String member_id = params[2];
                String group_id = params[3];
                URL url = new URL(newMember_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setReadTimeout(6000);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8")
                        + "&" + URLEncoder.encode("member_id", "UTF-8") + "=" + URLEncoder.encode(member_id, "UTF-8")
                        + "&" + URLEncoder.encode("group_id", "UTF-8") + "=" + URLEncoder.encode(group_id, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                JSONArray jsonArray = new JSONArray(result);

                return jsonArray;

            } catch (MalformedURLException e) {
                error=true;
                e.printStackTrace();
            } catch (IOException e) {
                if (e instanceof SocketTimeoutException)
                    error=true;
                if (e instanceof ConnectException)
                    error=true;
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (type.equals("GetGroupMembers")) {
            try {
                String user_id = params[1];

                String group_id = params[2];
                URL url = new URL(getGroupMembers_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setReadTimeout(6000);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8")
                        + "&" + URLEncoder.encode("group_id", "UTF-8") + "=" + URLEncoder.encode(group_id, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                JSONArray jsonArray = new JSONArray(result);

                return jsonArray;

            } catch (MalformedURLException e) {
                error=true;
                e.printStackTrace();
            } catch (IOException e) {
                error=true;
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (type.equals("GetMembers")) {
            try {
                String user_id = params[1];

                URL url = new URL(getMembers_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setReadTimeout(6000);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();


                JSONArray jsonArray = new JSONArray(result);


                return jsonArray;
            } catch (MalformedURLException e) {
                error=true;
                e.printStackTrace();
            } catch (IOException e) {
                if (e instanceof SocketTimeoutException)
                    error=true;
                if (e instanceof ConnectException)
                    error=true;
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else if (type.equals("GetFemale")) {
            try {
                String place_id = params[1];

                URL url = new URL(getFemale_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setReadTimeout(6000);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("place_id", "UTF-8") + "=" + URLEncoder.encode(place_id, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                JSONArray jsonArray = new JSONArray(result);

                return jsonArray;

            } catch (MalformedURLException e) {
                error=true;
                e.printStackTrace();
            } catch (IOException e) {
                if (e instanceof SocketTimeoutException)
                    error=true;
                if (e instanceof ConnectException)
                    error=true;
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (type.equals("GetMale")) {
            try {
                String place_id = params[1];

                URL url = new URL(getMale_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setReadTimeout(6000);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("place_id", "UTF-8") + "=" + URLEncoder.encode(place_id, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                JSONArray jsonArray = new JSONArray(result);

                return jsonArray;

            } catch (MalformedURLException e) {
                error=true;
                e.printStackTrace();
            } catch (IOException e) {
                if (e instanceof SocketTimeoutException)
                    error=true;
                if (e instanceof ConnectException)
                    error=true;
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (type.equals("CheckVisited")) {
            try {
                String place_id = params[1];
                String user_id = params[2];
                URL url = new URL(checkVisited_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setReadTimeout(6000);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("place_id", "UTF-8") + "=" + URLEncoder.encode(place_id, "UTF-8") + "&"
                        + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();


                JSONArray jsonArray = new JSONArray(result);
                    return jsonArray;

            } catch (MalformedURLException e) {
                error=true;
                e.printStackTrace();
            } catch (IOException e) {
                if (e instanceof SocketTimeoutException)
                    error=true;
                if (e instanceof ConnectException)
                    error=true;
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else if (type.equals("Reveal")) {
            try {
                String place_id = params[1];
                String user_id = params[2];
                String revealGroup = params[3];

                URL url = new URL(reveal_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setReadTimeout(6000);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("place_id", "UTF-8") + "=" + URLEncoder.encode(place_id, "UTF-8")
                        + "&" + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8")
                        + "&" + URLEncoder.encode("revealGroup", "UTF-8") + "=" + URLEncoder.encode(revealGroup, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                JSONArray jsonArray = new JSONArray(result);


                return jsonArray;

            } catch (MalformedURLException e) {
                error=true;
                e.printStackTrace();
            } catch (IOException e) {
                if (e instanceof SocketTimeoutException)
                    error=true;
                if (e instanceof ConnectException)
                    error=true;
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (type.equals("FamReveals")) {
            try {
                String place_id = params[1];
                String user_id = params[2];
                URL url = new URL(famReveals_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setReadTimeout(6000);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("place_id", "UTF-8") + "=" + URLEncoder.encode(place_id, "UTF-8") + "&"
                        + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();


                JSONArray jsonArray = new JSONArray(result);
                return jsonArray;

            } catch (MalformedURLException e) {
                error=true;
                e.printStackTrace();
            } catch (IOException e) {
                if (e instanceof SocketTimeoutException)
                    error=true;
                if (e instanceof ConnectException)
                    error=true;
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }else if (type.equals("CoReveals")) {
            try {
                String place_id = params[1];
                String user_id = params[2];
                URL url = new URL(coReveals_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setReadTimeout(6000);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("place_id", "UTF-8") + "=" + URLEncoder.encode(place_id, "UTF-8") + "&"
                        + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();


                JSONArray jsonArray = new JSONArray(result);
                return jsonArray;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                error=true;
            } catch (IOException e) {
                if (e instanceof SocketTimeoutException)
                    error=true;
                if (e instanceof ConnectException)
                    error=true;
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }else if (type.equals("NeReveals")) {
            try {
                String place_id = params[1];
                String user_id = params[2];
                URL url = new URL(neReveals_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setReadTimeout(6000);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("place_id", "UTF-8") + "=" + URLEncoder.encode(place_id, "UTF-8") + "&"
                        + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();


                JSONArray jsonArray = new JSONArray(result);
                return jsonArray;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                error=true;
            } catch (IOException e) {
                if (e instanceof SocketTimeoutException)
                    error=true;
                if (e instanceof ConnectException)
                    error=true;
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        else if (type.equals("FrReveals")) {
            try {
                String place_id = params[1];
                String user_id = params[2];
                URL url = new URL(frReveals_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setReadTimeout(6000);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("place_id", "UTF-8") + "=" + URLEncoder.encode(place_id, "UTF-8") + "&"
                        + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();


                JSONArray jsonArray = new JSONArray(result);
                return jsonArray;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                error=true;
            }

            catch (IOException e) {
                e.printStackTrace();

                if (e instanceof SocketTimeoutException)
                    error=true;
                if (e instanceof ConnectException)
                    error=true;
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }



        return null;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        //alertDialog.setMessage(result);
        //alertDialog.show();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (type.equals("login")) {


            if (jsonArray != null) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    user_id = jsonObject.getString("id");
                    user_name=jsonObject.getString("user_name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editor = sharedPreferences.edit();
                editor.putString("user_id", user_id);
                editor.putString("user_name", user_name);
                editor.commit();
            } else {
                editor = sharedPreferences.edit();
                editor.putString("user_id", null);
                editor.putString("user_name", null);
                editor.commit();
            }

        } else if (type.equals("NewVisit")) {

        } else if (type.equals("GetVisits")) {


            if (jsonArray != null) {
                placesID = new ArrayList<>();
                numOFVisits = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);


                        placesID.add(jsonObject.getString("place_id"));
                        numOFVisits.add(jsonObject.getString("NumOfUsers"));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        } else if (type.equals("NewMember")) {

        } else if (type.equals("GetMembers")) {

            if (jsonArray != null) {


                membersNames=new ArrayList<>();
                membersIds=new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (!jsonObject.isNull("user_name")) {
                            membersNames.add(jsonObject.getString("user_name"));
                            membersIds.add(jsonObject.getString("id"));


                        }
                        else if(!jsonObject.isNull("no_members"))
                        {
                            membersNames.add(jsonObject.getString("no_members"));
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

        } else if (type.equals("GetGroupMembers")) {

            if (jsonArray != null) {


                groupMembers=new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (!jsonObject.isNull("user_name")) {
                            groupMembers.add(jsonObject.getString("user_name"));
                        }
                        else if(!jsonObject.isNull("no_members"))
                        {
                            groupMembers.add(jsonObject.getString("no_members"));
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }




        }else if (type.equals("GetFemale")) {


                if (jsonArray != null) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        femaleNum = jsonObject.getString("FemaleNum");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    editor = sharedPreferences.edit();
                    editor.putString("female_num", femaleNum);
                    editor.commit();
                } else {
                    editor = sharedPreferences.edit();
                    editor.putString("female_num", null);
                    editor.commit();
                }




        } else if (type.equals("GetMale")) {
            if (jsonArray != null) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    maleNum = jsonObject.getString("MaleNum");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editor = sharedPreferences.edit();
                editor.putString("male_num", maleNum);
                editor.commit();
            } else {
                editor = sharedPreferences.edit();
                editor.putString("male_num", null);
                editor.commit();
            }

        }else if (type.equals("CheckVisited")) {


            if (jsonArray != null) {
                    isVisited=true;
            } else {
                    isVisited=false;
            }

        }else if (type.equals("FamReveals")) {


            if (jsonArray != null) {


                famReveals=new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (!jsonObject.isNull("user_name")) {
                            famReveals.add(jsonObject.getString("user_name"));
                        }
                        else if(!jsonObject.isNull("no_reveals"))
                        {
                            famReveals.add(jsonObject.getString("no_reveals"));
                        }
                        else if(!jsonObject.isNull("not_revealed"))
                        {
                            famReveals.add(jsonObject.getString("not_revealed"));
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

        }else if (type.equals("CoReveals")) {


            if (jsonArray != null) {
                coReveals=new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (!jsonObject.isNull("user_name")) {
                            coReveals.add(jsonObject.getString("user_name"));
                        }
                        else if(!jsonObject.isNull("no_reveals"))
                        {
                            coReveals.add(jsonObject.getString("no_reveals"));
                        }
                        else if(!jsonObject.isNull("not_revealed"))
                        {
                            coReveals.add(jsonObject.getString("not_revealed"));
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

        }else if (type.equals("NeReveals")) {


            if (jsonArray != null) {
                neReveals=new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (!jsonObject.isNull("user_name")) {
                            neReveals.add(jsonObject.getString("user_name"));
                        }
                        else if(!jsonObject.isNull("no_reveals"))
                        {
                            neReveals.add(jsonObject.getString("no_reveals"));
                        }
                        else if(!jsonObject.isNull("not_revealed"))
                        {
                            neReveals.add(jsonObject.getString("not_revealed"));
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

        }
        else if (type.equals("FrReveals")) {


            if (jsonArray != null) {
                frReveals=new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (!jsonObject.isNull("user_name")) {
                            frReveals.add(jsonObject.getString("user_name"));
                        }
                        else if(!jsonObject.isNull("no_reveals"))
                        {
                            frReveals.add(jsonObject.getString("no_reveals"));
                            Log.i("Friends",jsonObject.getString("no_reveals"));
                        }
                        else if(!jsonObject.isNull("not_revealed"))
                        {
                            frReveals.add(jsonObject.getString("not_revealed"));
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

        }

        else if (type.equals("Reveal")) {
            if (jsonArray != null) {
                afterReveal=new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (!jsonObject.isNull("user_name")) {
                            afterReveal.add(jsonObject.getString("user_name"));
                        }
                        else if(!jsonObject.isNull("no_reveals"))
                        {
                            afterReveal.add(jsonObject.getString("no_reveals"));

                        }
                        else if(!jsonObject.isNull("not_revealed"))
                        {
                            afterReveal.add(jsonObject.getString("not_revealed"));
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        }


    }

    public List<String> getPlacesID() {
        return placesID;
    }

    public List<String> getNumOFVisits() {
        return numOFVisits;
    }

    public List<String> getFamReveals() {
        return famReveals;
    }
    public List<String> getCoReveals() {
        return coReveals;
    }
    public List<String> getNeReveals() {
        return neReveals;
    }
    public List<String> getFrReveals() {
        return frReveals;
    }

    public List<String> getAfterReveal() {
        return afterReveal;
    }

    public List<String> getGroupMembers() {
        return groupMembers;
    }

    public List<String> getMembersNames() {
        return membersNames;
    }

    public List<String> getMembersIds() {
        return membersIds;
    }

    public boolean isVisited() {
        return isVisited;
    }



}
