package com.vyommaitreya.android.scienceup.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.vyommaitreya.android.scienceup.R;

import java.util.ArrayList;

public class AttendanceTeacherAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> mSubjectName, mSubjectID, mCourseID;
    private LayoutInflater layoutInflater;

    public AttendanceTeacherAdapter(Context context, ArrayList<String> mSubjectName, ArrayList<String> mSubjectID, ArrayList<String> mCourseID) {
        this.context = context;
        this.mSubjectName = mSubjectName;
        this.mSubjectID = mSubjectID;
        this.mCourseID = mCourseID;
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    public void setmSubjectName(ArrayList<String> mSubjectName) {
        this.mSubjectName = mSubjectName;
    }

    public void setmSubjectID(ArrayList<String> mSubjectID) {
        this.mSubjectID = mSubjectID;
    }

    public void setmCourseID(ArrayList<String> mCourseID) {
        this.mCourseID = mCourseID;
    }

    public void updateData(ArrayList<String> mSubjectName, ArrayList<String> mSubjectID, ArrayList<String> mCourseID) {
        setmSubjectID(mSubjectID);
        setmSubjectName(mSubjectName);
        setmCourseID(mCourseID);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mSubjectName.size();
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
        convertView = layoutInflater.inflate(R.layout.list_component_attendance_teacher, null);
        TextView textView = convertView.findViewById(R.id.subject);
        textView.setText(mSubjectName.get(position));
        return convertView;
    }

}