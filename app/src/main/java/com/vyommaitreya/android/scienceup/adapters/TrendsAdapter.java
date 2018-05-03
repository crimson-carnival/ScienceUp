package com.vyommaitreya.android.scienceup.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vyommaitreya.android.scienceup.R;

import java.util.ArrayList;

public class TrendsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> links;
    private LayoutInflater layoutInflater;

    public TrendsAdapter(Context context, ArrayList<String> links) {
        this.context = context;
        this.links = links;
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateData(ArrayList<String> links) {
        this.links = links;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return links.size();
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
        convertView = layoutInflater.inflate(R.layout.list_component_trends, null);
        TextView textView = convertView.findViewById(R.id.link);
        textView.setText(links.get(position));
        return convertView;
    }


}

