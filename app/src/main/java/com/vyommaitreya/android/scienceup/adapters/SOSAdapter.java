package com.vyommaitreya.android.scienceup.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vyommaitreya.android.scienceup.R;

import java.util.ArrayList;


public class SOSAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> mCourseName;
    private ArrayList<Integer> mCount;
    private LayoutInflater layoutInflater;
    public SOSAdapter(Context context, ArrayList<String> mCourseName, ArrayList<Integer> mCount)
    {
        this.context=context;
        this.mCount = mCount;
        this.mCourseName = mCourseName;
        layoutInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

    }

    public void updateData(ArrayList<String> mCourseName, ArrayList<Integer> mCount)
    {
        this.mCourseName = mCourseName;
        this.mCount = mCount;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCount.size();
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
        convertView=layoutInflater.inflate(R.layout.list_component_sos,null);
        TextView textView=convertView.findViewById(R.id.course_name);
        textView.setText(mCourseName.get(position));
        textView=convertView.findViewById(R.id.count);
        textView.setText(String.valueOf(mCount.get(position)));
        return convertView;
    }


}
