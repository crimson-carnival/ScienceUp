package com.vyommaitreya.android.scienceup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> time, subject, room;
    LayoutInflater layoutInflater;
    public CustomAdapter(Context context, ArrayList<String> time, ArrayList<String> subject, ArrayList<String> room)
    {
        this.context=context;
        this.time=time;
        this.subject=subject;
        this.room=room;
        layoutInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

    }

    public void updateData(ArrayList<String> time, ArrayList<String> subject, ArrayList<String> room)
    {
        this.time=time;
        this.subject=subject;
        this.room=room;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return time.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=layoutInflater.inflate(R.layout.card,null);
        TextView textView=convertView.findViewById(R.id.time);
        textView.setText(time.get(position));
        textView=convertView.findViewById(R.id.subject);
        textView.setText(subject.get(position));
        textView=convertView.findViewById(R.id.room);
        textView.setText(room.get(position));
        return convertView;
    }


}
