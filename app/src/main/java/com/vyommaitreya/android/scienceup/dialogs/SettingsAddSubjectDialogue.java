package com.vyommaitreya.android.scienceup.dialogs;

import android.app.Activity;
import android.app.Dialog;

import java.text.DateFormat;
import java.util.Date;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vyommaitreya.android.scienceup.R;
import com.vyommaitreya.android.scienceup.database.Feedback;
import com.vyommaitreya.android.scienceup.database.Subject;
import com.vyommaitreya.android.scienceup.database.UserAccount;


public class SettingsAddSubjectDialogue extends Dialog implements
        android.view.View.OnClickListener {

    private Activity c;
    private EditText mSubjectName, mTeacherName;

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

        done = findViewById(R.id.done);
        cancel = findViewById(R.id.cancel);
        mSubjectName = findViewById(R.id.subject_name);
        mTeacherName = findViewById(R.id.teacher_name);

        done.setOnClickListener(this);
        cancel.setOnClickListener(this);

        mRef = FirebaseDatabase.getInstance().getReference("users/"+userId+"/subjects");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done:
                try {
                    String subjectName = mSubjectName.getText().toString();
                    String teacherName = mTeacherName.getText().toString();
                    if(subjectName == null) {
                        mSubjectName.setError("Subject Name needed.");
                        mSubjectName.requestFocus();
                    }
                    else if(teacherName == null) {
                        mTeacherName.setError("Teacher Name needed (Can be edited later).");
                        mTeacherName.requestFocus();
                    }
                    else {
                        id = mRef.push().getKey();
                        Subject subject = new Subject(subjectName, teacherName, id);
                        mRef.child(id).setValue(subject);
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


