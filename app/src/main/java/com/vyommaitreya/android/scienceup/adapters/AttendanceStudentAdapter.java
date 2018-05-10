package com.vyommaitreya.android.scienceup.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vyommaitreya.android.scienceup.R;

import java.util.ArrayList;

public class AttendanceStudentAdapter extends BaseAdapter {
    private Context context;

    private ArrayList<String> mSubjectName;
    private ArrayList<Integer> mPresent, mTotal;
    private String user;

    public void setmSubjectName(ArrayList<String> mSubjectName) {
        this.mSubjectName = mSubjectName;
    }

    public void setmPresent(ArrayList<Integer> mPresent) {
        this.mPresent = mPresent;
    }

    public void setmTotal(ArrayList<Integer> mTotal) {
        this.mTotal = mTotal;
    }

    public void setUser(String user) {
        this.user = user;
    }

    private LayoutInflater layoutInflater;

    public AttendanceStudentAdapter(Context context, ArrayList<String> mSubjectName, ArrayList<Integer> mPresent, ArrayList<Integer> mTotal, String user) {
        this.context = context;
        this.mSubjectName = mSubjectName;
        this.mPresent = mPresent;
        this.mTotal = mTotal;
        this.user = user;
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateData(ArrayList<String> mSubjectName, ArrayList<Integer> mPresent, ArrayList<Integer> mTotal) {
        setmPresent(mPresent);
        setmSubjectName(mSubjectName);
        setmTotal(mTotal);
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
        convertView = layoutInflater.inflate(R.layout.list_component_attendance_student, null);
        TextView textView = convertView.findViewById(R.id.subject);
        textView.setText(mSubjectName.get(position));
        textView = convertView.findViewById(R.id.total);
        textView.setText("Classes held: " + mTotal.get(position));
        textView = convertView.findViewById(R.id.present);
        float attendance = mPresent.get(position) * 100 / (float) mTotal.get(position);
        int color;
        if (attendance > 75) color = Color.GREEN;
        else if (attendance > 67) color = Color.YELLOW;
        else color = Color.RED;
        String lAttendance;
        try {
            lAttendance = (attendance + "").substring(0, 5);
        } catch (IndexOutOfBoundsException e) {
            lAttendance = attendance+"";
        }
        textView.setText("Attendance: " + lAttendance + "%");
        ProgressBar progressBar = convertView.findViewById(R.id.attendance_progress);
        progressBar.setProgress(Math.round(attendance));
        progressBar.setProgressTintList(ColorStateList.valueOf(color));
        return convertView;
    }


}