package com.vyommaitreya.android.scienceup.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vyommaitreya.android.scienceup.R;

public class AttendanceTeacherDialogue extends Dialog implements
        View.OnClickListener {

    private String mSubjectName, mSubjectID, mCourseID;
    private DatabaseReference mRef;

    public AttendanceTeacherDialogue(@NonNull Context context, String mSubjectName, String mSubjectID, String mCourseID) {
        super(context);
        this.mSubjectName = mSubjectName;
        this.mSubjectID = mSubjectID;
        this.mCourseID = mCourseID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialogue_attendance_teacher);

        Button dismiss;
        Switch switch_attendance;

        dismiss = findViewById(R.id.dismiss);
        switch_attendance = findViewById(R.id.open);

        switch_attendance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mRef.child("courses").child(mCourseID).child("students").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                final String uid = ds.child("id").getValue(String.class);
                                mRef.child("users").child(uid).child("attendance_open").child("status").setValue("open");
                                mRef.child("users").child(uid).child("attendance_open").child("subject_id").setValue(mSubjectID);
                                mRef.child("users").child(uid).child("subjects").child(mSubjectID).child("attendance").child("total").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        int total = dataSnapshot.getValue(Integer.class);
                                        mRef.child("users").child(uid).child("subjects").child(mSubjectID).child("attendance").child("total").setValue(total+1);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                            Toast.makeText(getContext(), "Attendance Open", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    mRef.child("courses").child(mCourseID).child("students").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                String uid = ds.child("id").getValue(String.class);
                                try {
                                    mRef.child("users").child(uid).child("attendance_open").removeValue();
                                } catch (Exception e) {
                                    continue;
                                }
                            }
                            Toast.makeText(getContext(), "Attendance Closed", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        dismiss.setOnClickListener(this);
        TextView tv = findViewById(R.id.subject);

        tv.setText(mSubjectName);

        mRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dismiss:

            default:
                break;
        }
        dismiss();
    }
}

