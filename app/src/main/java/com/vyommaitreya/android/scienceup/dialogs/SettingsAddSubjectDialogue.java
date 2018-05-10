package com.vyommaitreya.android.scienceup.dialogs;

import android.app.Activity;
import android.app.Dialog;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.os.Bundle;
import android.util.ArraySet;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vyommaitreya.android.scienceup.R;
import com.vyommaitreya.android.scienceup.database.Attendance;
import com.vyommaitreya.android.scienceup.database.Feedback;
import com.vyommaitreya.android.scienceup.database.Subject;
import com.vyommaitreya.android.scienceup.database.UserAccount;


public class SettingsAddSubjectDialogue extends Dialog implements
        android.view.View.OnClickListener {

    private Activity c;
    private EditText mSubjectName;
    private Spinner mCourseName;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> mArrayList, mArrayList2;
    private boolean mFlag;
    private Subject subject;

    private String id, userId;

    private DatabaseReference mRef;

    public SettingsAddSubjectDialogue(Activity a, String userId) {
        super(a);
        c = a;
        this.id = id;
        this.userId = userId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialogue_settings_subject_add);

        Button done, cancel;

        mFlag = false;

        mRef = FirebaseDatabase.getInstance().getReference();
        mArrayList = new ArrayList<>();
        mArrayList2 = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, mArrayList);

        done = findViewById(R.id.done);
        cancel = findViewById(R.id.cancel);
        mSubjectName = findViewById(R.id.subject_name);
        mCourseName = findViewById(R.id.course_name);
        mCourseName.setAdapter(adapter);

        mRef.child("courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mArrayList.clear();
                mArrayList2.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    mArrayList.add(ds.child("course_name").getValue().toString());
                    mArrayList2.add(ds.child("id").getValue().toString());
                }
                mFlag = true;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        done.setOnClickListener(this);
        cancel.setOnClickListener(this);

        mRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done:
                try {
                    String subjectName = mSubjectName.getText().toString();
                    String courseName = mArrayList.get(mCourseName.getSelectedItemPosition());//mCourseName.getText().toString();
                    if(subjectName == null) {
                        mSubjectName.setError("Subject Name needed.");
                        mSubjectName.requestFocus();
                    }
                    else if(!mFlag) {
                        Toast.makeText(c, "Please add courses for selection", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        id = mRef.push().getKey();
                        subject = new Subject(subjectName, mArrayList2.get(mCourseName.getSelectedItemPosition()), id);
                        mRef.child("users").child(userId).child("subjects").child(id).setValue(subject);

                        mRef.child("courses").child(mArrayList2.get(mCourseName.getSelectedItemPosition())).child("students").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                    subject.setTeacher(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                                    mRef.child("courses").child(mArrayList2.get(mCourseName.getSelectedItemPosition())).child("subjects").child(id).setValue(subject);
                                    mRef.child("users").child(ds.child("id").getValue().toString()).child("subjects").child(id).setValue(subject);
                                    Attendance attendance = new Attendance(0,0);
                                    mRef.child("users").child(ds.child("id").getValue().toString()).child("subjects").child(id).child("attendance").setValue(attendance);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                } catch (Exception e) {
                    Toast.makeText(c, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            default:
                break;
        }
        dismiss();
    }
}


