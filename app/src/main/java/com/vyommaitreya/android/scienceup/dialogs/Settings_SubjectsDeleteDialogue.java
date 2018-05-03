package com.vyommaitreya.android.scienceup.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vyommaitreya.android.scienceup.R;

public class Settings_SubjectsDeleteDialogue extends Dialog implements
        android.view.View.OnClickListener {

    private Activity c;
    private String url;
    private String id;

    private DatabaseReference mRef;

    Settings_SubjectsDeleteDialogue(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialogue_delete);

        Button yes, no;
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
                    mRef.child("users").child(FirebaseAuth.getInstance().getUid()).child("subjects").child(id).removeValue();
                } catch (Exception e) {
                }

            default:
                break;
        }
        dismiss();
    }

    public void setId(String id) {
        this.id = id;
    }
}


