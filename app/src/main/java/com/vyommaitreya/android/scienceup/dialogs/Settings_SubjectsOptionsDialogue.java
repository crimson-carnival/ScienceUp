package com.vyommaitreya.android.scienceup.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.vyommaitreya.android.scienceup.R;

public class Settings_SubjectsOptionsDialogue extends Dialog implements
        android.view.View.OnClickListener {

    private Activity c;
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public Settings_SubjectsOptionsDialogue(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialogue_options);

        Button delete, edit;

        edit = findViewById(R.id.edit);
        delete = findViewById(R.id.delete);

        delete.setOnClickListener(this);
        edit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit:

                break;
            case R.id.delete:
                Settings_SubjectsDeleteDialogue rdd = new Settings_SubjectsDeleteDialogue(c);
                rdd.setId(id);
                rdd.show();

                rdd.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        dismiss();
                    }
                });
            default:
                break;
        }
    }
}
