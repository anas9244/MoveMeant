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
import java.io.ObjectStreamException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Anas on 04.07.2017.
 */

public class BackgroundConnector extends AsyncTask<String, Void, JSONArray> {

    Context context;

    String result = "";

    BackgroundConnector(Context ctx) {
        context = ctx;
    }


    String user_id = "";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    String type;
    private static StringBuffer visitsBuffer = new StringBuffer();

    @Override
    protected JSONArray doInBackground(String... params) {
        type = params[0];
        String login_url = "http://141.54.154.231:1234/LogIn.php";
        String newPlace_url = "http://141.54.154.231:1234/NewVisit.php";
        String getVisits_url = "http://141.54.154.231:1234/GetVisited.php";
        String newMember_url= "http://141.54.154.231:1234/NewMember.php";
        String getMembers_url= "http://141.54.154.231:1234/GetMembers.php";

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


                JSONArray jsonArray = new JSONArray(result);


                return jsonArray;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
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
                e.printStackTrace();
            } catch (IOException e) {
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
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }else if (type.equals("NewMember")) {
            try {
                String user_id = params[1];
                String member_id = params[2];
                String group_id= params[3];
                URL url = new URL(newMember_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
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
                e.printStackTrace();
            } catch (IOException e) {
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
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
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
                e.printStackTrace();
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
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);

        if (type.equals("login")) {


            if(jsonArray!=null) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    user_id = jsonObject.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editor = sharedPreferences.edit();
                editor.putString("user_id", user_id);
                editor.commit();
            }
            else {
                editor = sharedPreferences.edit();
                editor.putString("user_id", null);
                editor.commit();
            }

        } else if (type.equals("NewVisit")) {

        } else if (type.equals("GetVisits")) {
            if (jsonArray!=null) {
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String place_id = jsonObject.getString("place_id");
                        String numOfUsers = jsonObject.getString("NumOfUsers");

                        visitsBuffer.append("Place: " + place_id + " - NUM: " + numOfUsers + "\n");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        } else if (type.equals("NewMember")) {

        }
        else if (type.equals("GetMembers")) {

        }


    }

    public String getVisitis() {
        return (visitsBuffer.toString());
    }
}
