package com.vyommaitreya.android.scienceup.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vyommaitreya.android.scienceup.R;
import com.vyommaitreya.android.scienceup.database.Timetable;

public class TimetableAddDialogue extends Dialog implements
        android.view.View.OnClickListener {

    private Activity c;
    private EditText subjectName, room, from, to;

    private String day, id;
    private boolean isEdit; //To check is dialog is opening as an edit dialog

    private DatabaseReference mRef;

    public TimetableAddDialogue(Activity a) {
        super(a);
        c = a;
        isEdit = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialogue_timetable_add);

        Button done, cancel;

        done = findViewById(R.id.done);
        cancel = findViewById(R.id.cancel);

        done.setOnClickListener(this);
        cancel.setOnClickListener(this);

        from = findViewById(R.id.edit_text_from);
        to = findViewById(R.id.edit_text_to);
        subjectName = findViewById(R.id.edit_text_subject_name);
        room = findViewById(R.id.edit_text_room);

        mRef = FirebaseDatabase.getInstance().getReference();
    }

    public void setData(String from, String to, String subjectName, String room, String id) {
        this.from.setText(from);
        this.to.setText(to);
        this.subjectName.setText(subjectName);
        this.room.setText(room);
        this.id = id;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done:
                try {
                    if(!isEdit) id = mRef.push().getKey();
                    Timetable artist = new Timetable(id,day.trim(),from.getText().toString().trim() + " - " + to.getText().toString().trim(),subjectName.getText().toString().trim(),room.getText().toString().trim());
                    mRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("timetable").child(day).child(id).setValue(artist);
                } catch (Exception e) {
                    Toast.makeText(c, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            default:
                break;
        }
        dismiss();
    }
}

