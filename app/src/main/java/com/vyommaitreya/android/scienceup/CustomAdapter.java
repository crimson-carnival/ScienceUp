package com.vyommaitreya.android.scienceup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class CustomAdapter extends BaseAdapter {
    Context context;
    String[] time, subject, room;
    LayoutInflater layoutInflater;
    public CustomAdapter(Context context, String[] time, String[] subject, String[] room)
    {
        this.context=context;
        this.time=time;
        this.subject=subject;
        this.room=room;
        layoutInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return time.length;
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
        textView.setText(time[position]);
        textView=convertView.findViewById(R.id.subject);
        textView.setText(subject[position]);
        textView=convertView.findViewById(R.id.room);
        textView.setText(room[position]);
        return convertView;
    }


}
