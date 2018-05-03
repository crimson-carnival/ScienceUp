package com.vyommaitreya.android.scienceup.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vyommaitreya.android.scienceup.R;

import java.util.ArrayList;

public class StudentsListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> mName, mID;
    private LayoutInflater layoutInflater;

    public StudentsListAdapter(Context context, ArrayList<String> name, ArrayList<String> id) {
        this.context = context;
        mName = name;
        mID = id;
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateData(ArrayList<String> name, ArrayList<String> id) {
        mName = name;
        mID = id;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mName.size();
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
        convertView = layoutInflater.inflate(R.layout.list_component_show_student, null);
        TextView textView = convertView.findViewById(R.id.student_name);
        textView.setText(mName.get(position));
        return convertView;
    }


}
