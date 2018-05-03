package com.vyommaitreya.android.scienceup.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vyommaitreya.android.scienceup.R;

public class TrendsAddDialogue extends Dialog implements
        android.view.View.OnClickListener {

    private Activity c;
    private EditText mFeedback;

    private String id, userId;

    private DatabaseReference mRef;

    public TrendsAddDialogue(Activity a) {
        super(a);
        c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialogue_trends_add);

        Button done, cancel;

        done = findViewById(R.id.done);
        cancel = findViewById(R.id.cancel);
        mFeedback = findViewById(R.id.edit_text_link);

        done.setOnClickListener(this);
        cancel.setOnClickListener(this);

        mRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done:
                try {
                    id = mRef.push().getKey();
                    mRef.child("trends").child(id).setValue(mFeedback.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(c, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            default:
                break;
        }
        dismiss();
    }
}
