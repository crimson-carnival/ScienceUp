package com.vyommaitreya.android.scienceup.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vyommaitreya.android.scienceup.R;

public class TimetableDeleteDialogue extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;

    private String subjectName, day, id;
    int index, quad;

    DatabaseReference mRef;

    public TimetableDeleteDialogue(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialogue_timetable_delete);

        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);

        yes.setOnClickListener(this);
        no.setOnClickListener(this);

        mRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yes:
                try {
                    mRef.child("timetable").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(day).child(id).removeValue();
                } catch (Exception e) {
                }

            default:
                break;
        }
        dismiss();
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setQuad(int quad) {
        this.quad = quad;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setId(String id) {
        this.id = id;
    }
}
