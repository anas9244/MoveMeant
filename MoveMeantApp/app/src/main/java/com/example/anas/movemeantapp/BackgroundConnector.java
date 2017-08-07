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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Anas on 04.07.2017.
 */

public class BackgroundConnector extends AsyncTask<String, Void, String> {

    Context context;
    AlertDialog alertDialog;
    String result = "";

    BackgroundConnector(Context ctx) {
        context = ctx;
    }

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String user_id;
    boolean login_failed;
    LoginActivity loginActivity = new LoginActivity();
    String type;

    @Override
    protected String doInBackground(String... params) {
        type = params[0];
        String login_url = "http://141.54.154.231:1234/LogIn.php";
        String newPlace_url = "http://141.54.154.231:1234/NewPlace.php";
        String getVisits_url="http://141.54.154.231:1234/GetVisited.php";
        if (type.equals("login")) {
            try {
                String user_name = params[1];
                String password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
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
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type.equals("NewPlace")) {
            try {
                String place_id = params[1];
                String place_name = params[2];
                String place_type = params[3];
                String lat = params[4];
                String lng = params[5];
                String user_id = params[6];
                URL url = new URL(newPlace_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data =URLEncoder.encode("place_id", "UTF-8") + "=" + URLEncoder.encode(place_id, "UTF-8")
                        + "&" +URLEncoder.encode("place_name", "UTF-8") + "=" + URLEncoder.encode(place_name, "UTF-8")
                        + "&" + URLEncoder.encode("place_type", "UTF-8") + "=" + URLEncoder.encode(place_type, "UTF-8")
                        + "&" + URLEncoder.encode("lat", "UTF-8") + "=" + URLEncoder.encode(lat, "UTF-8")
                        + "&" + URLEncoder.encode("lng", "UTF-8") + "=" + URLEncoder.encode(lng, "UTF-8")
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
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }/*
        else if (type.equals("GetVisists")) {
            String response="";
            try {

                URL url = new URL(getVisits_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));


                String  inputBuffer = "";
                while ((inputBuffer = in.readLine()) != null){
                    response+=inputBuffer;
                }

                in.close();
                httpURLConnection.disconnect();

                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            JSONObject results = new JSONObject();
            try {
                results.getString(response);
                JSONArray jsonArray = results.getJSONArray("items");


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonArray;


        }*/

        return null;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(String result) {
        //alertDialog.setMessage(result);
        //alertDialog.show();


        if (type.equals("login")) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            editor = sharedPreferences.edit();
            editor.putString("user_id", result);
            editor.commit();

            user_id = sharedPreferences.getString("user_id", "");
            if (!user_id.equals("0")) {

                Intent intent = new Intent(context, Home.class);
                context.startActivity(intent);
            }
        }
        else if (type.equals("NewPlace"))
        {

        }
        else if (type.equals("GetVisists"))
        {

        }


    }
}
