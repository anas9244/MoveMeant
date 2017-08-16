package com.example.anas.movemeantapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anas on 16.08.2017.
 */

public class AdapterMembers  extends BaseAdapter {

    Context context;
    List<String> membersNames= new ArrayList<>();
    List<String> membersIds= new ArrayList<>();

    static LayoutInflater inflater=null;
    ViewMemHolder holder;


    public AdapterMembers(Context ctx, List<String> membersNames,List<String>membersIds)
    {
        this.context=ctx;
        this.membersNames=membersNames;
        this.membersIds=membersIds;
    }

    @Override
    public int getCount() {
        return membersIds.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;

        if (row==null){
            holder = new ViewMemHolder();
            inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(R.layout.member_row,null);
            holder.textMemName=(TextView) row.findViewById(R.id.textMemName);
            holder.textId=(TextView) row.findViewById(R.id.textId);

            holder.itemLayout=(LinearLayout)row.findViewById(R.id.memLayout);
            row.setTag(holder);



        }else{
            holder=(ViewMemHolder) row.getTag();
        }



        holder.textMemName.setText(membersNames.get(position));
        holder.textId.setText(membersIds.get(position));






        return row;
    }

    static class ViewMemHolder {
        TextView textMemName;
        TextView textId;

        LinearLayout itemLayout;

    }
}
