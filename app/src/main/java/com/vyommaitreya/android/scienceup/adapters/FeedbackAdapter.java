package com.vyommaitreya.android.scienceup.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vyommaitreya.android.scienceup.R;

import java.util.ArrayList;


public class FeedbackAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> date, feedback, id;
    private String user;
    private LayoutInflater layoutInflater;

    public FeedbackAdapter(Context context, ArrayList<String> date, ArrayList<String> feedback, ArrayList<String> id, String user) {
        this.context = context;
        this.date = date;
        this.feedback = feedback;
        this.id = id;
        this.user=user;
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateData(ArrayList<String> date, ArrayList<String> feedback, ArrayList<String> id) {
        this.date = date;
        this.feedback = feedback;
        this.id = id;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return date.size();
    }

    @Override
    public Object getItem(int i) {
        return this;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.list_component_feedback, null);
        TextView textView = convertView.findViewById(R.id.date);
        textView.setText(date.get(position));
        textView = convertView.findViewById(R.id.feedback);
        textView.setText(feedback.get(position));
        return convertView;
    }


}


