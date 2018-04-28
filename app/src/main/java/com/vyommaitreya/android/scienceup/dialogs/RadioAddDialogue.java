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
import com.vyommaitreya.android.scienceup.database.Radio;


public class RadioAddDialogue extends Dialog implements
        android.view.View.OnClickListener {

    private Activity c;
    private EditText title, description;

    private String id;
    private boolean isEdit; //To check is dialog is opening as an edit dialog

    private DatabaseReference mRef;

    public RadioAddDialogue(Activity a) {
        super(a);
        c = a;
        isEdit = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialogue_radio_add);

        Button done, cancel;

        done = findViewById(R.id.done);
        cancel = findViewById(R.id.cancel);
        title = findViewById(R.id.edit_text_title);
        description = findViewById(R.id.edit_text_description);

        done.setOnClickListener(this);
        cancel.setOnClickListener(this);


        mRef = FirebaseDatabase.getInstance().getReference();
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done:
                try {
                    if (!isEdit) id = mRef.push().getKey();
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    Radio radio = new Radio(id,title.getText().toString(),currentUser.getEmail(),description.getText().toString(), DateFormat.getDateTimeInstance().format(new Date()))   ;
                    mRef.child("radio").child(id).setValue(radio);
                } catch (Exception e) {
                    Toast.makeText(c, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            default:
                break;
        }
        dismiss();
    }
}
