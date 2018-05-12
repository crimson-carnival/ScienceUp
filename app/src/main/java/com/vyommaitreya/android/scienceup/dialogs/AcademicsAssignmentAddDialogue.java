package com.vyommaitreya.android.scienceup.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vyommaitreya.android.scienceup.R;

public class AcademicsAssignmentAddDialogue extends Dialog implements
        View.OnClickListener {

    private Activity c;
    private EditText mFeedback;

    private String id, courseID;

    private DatabaseReference mRef;

    public AcademicsAssignmentAddDialogue(Activity a, String courseID) {
        super(a);
        c = a;
        this.courseID = courseID;
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

        TextView header = findViewById(R.id.header);
        header.setText("Add an assignment:");
        mFeedback.setHint("New Assignment");

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
                    mRef.child("academics").child("assignments").child(courseID).child(id).setValue(mFeedback.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(c, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            default:
                break;
        }
        dismiss();
    }
}
