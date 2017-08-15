package com.example.anas.movemeantapp;

/**
 * Created by Anas on 13.08.2017.
 */

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.InterpolatorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.graphics.Color.argb;


public class AdapterPlaces extends BaseAdapter{
    Context context;
    List<String> placesName= new ArrayList<>();
    List<String> placesType= new ArrayList<>();
    List<String> numOfvisits= new ArrayList<>();
    static LayoutInflater inflater=null;
    ViewHolder holder;
    LatLng currentLoc;
    int progress;
    LatLng[] placeLoc;


    public AdapterPlaces(Context ctx, List<String>placesName, List<String> placesType, List<String>numOfvisits ){
        context=ctx;
        this.placesName=placesName;
        this.placesType=placesType;
        this.numOfvisits=numOfvisits;

    }
    @Override
    public int getCount() {


        return placesName.size();
    }

    @Override
    public Object getItem(int position) {
        return getItemId(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View row=view;

        if (row==null){
        holder = new ViewHolder();
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row=inflater.inflate(R.layout.place_row,null);
        holder.text_placeName=(TextView) row.findViewById(R.id.text_placeName);
        holder.text_placeType=(TextView) row.findViewById(R.id.text_placeType);
        holder.textNum=(TextView) row.findViewById(R.id.textNum);
        holder.circleNum=(RelativeLayout)row.findViewById(R.id.circleNum);
        holder.textDetails=(TextView)row.findViewById(R.id.textDetails);
        holder.itemLayout=(LinearLayout)row.findViewById(R.id.itemLayout);
            row.setTag(holder);



        }else{
            holder=(ViewHolder)row.getTag();
        }



        holder.text_placeName.setText(placesName.get(position));
        holder.text_placeType.setText(placesType.get(position));
        holder.textNum.setText(numOfvisits.get(position) +" ppl");
        holder.textDetails.setVisibility(View.GONE);
        int alphaNumof=Integer.parseInt(numOfvisits.get(position))*255/Integer.parseInt(numOfvisits.get(0));
        int color = Color.argb(alphaNumof, 76, 90, 160);

        holder.circleNum.setBackgroundColor(color);


        if (PlacesFrag.placePos()==position)
        {

            holder.textDetails.setVisibility(View.VISIBLE);
            holder.itemLayout.setBackgroundColor(Color.parseColor("#ffffff"));
         }
        else {holder.textDetails.setVisibility(View.GONE);
            holder.itemLayout.setBackgroundColor(Color.parseColor("#e6e6e6"));
        }



        return row;
    }

    static class ViewHolder {
        TextView text_placeName;
        TextView text_placeType;
        TextView textNum;
        RelativeLayout circleNum;
        LinearLayout itemLayout;
        TextView textDetails;
    }
}
