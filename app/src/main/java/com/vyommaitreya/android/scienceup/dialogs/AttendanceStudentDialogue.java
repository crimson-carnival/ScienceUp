package com.vyommaitreya.android.scienceup.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vyommaitreya.android.scienceup.R;

public class AttendanceStudentDialogue extends Dialog implements
        android.view.View.OnClickListener {

    private String userId, subjectId;
    private DatabaseReference mRef;
    private int total, attended;

    public AttendanceStudentDialogue(@NonNull Context c, String userId, String subjectId) {
        super(c);
        this.userId = userId;
        this.subjectId = subjectId;
        total = 0;
        attended = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialogue_attendance_student_add);

        Button done, cancel;

        done = findViewById(R.id.done);
        cancel = findViewById(R.id.cancel);

        done.setOnClickListener(this);
        cancel.setOnClickListener(this);

        mRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done:
                try {
                    mRef.child("users").child(userId).child("subjects").child(subjectId).child("attendance").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            attended = dataSnapshot.child("attended").getValue(Integer.class);
                            mRef.child("users").child(userId).child("subjects").child(subjectId).child("attendance").child("attended").setValue(attended+1);
                            mRef.child("users").child(userId).child("attendance_open").removeValue();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            default:
                break;
        }
        dismiss();
    }
}

