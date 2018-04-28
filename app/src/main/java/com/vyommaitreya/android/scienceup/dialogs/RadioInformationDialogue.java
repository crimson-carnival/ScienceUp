package com.vyommaitreya.android.scienceup.dialogs;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.vyommaitreya.android.scienceup.R;

public class RadioInformationDialogue extends Dialog implements
        android.view.View.OnClickListener {

    private Activity c;
    private TextView title, uploaded_by, date, comments;

    public RadioInformationDialogue(Activity a) {
        super(a);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialogue_radio_information);

        findViewById(R.id.btn_ok).setOnClickListener(this);

        title = findViewById(R.id.title);
        uploaded_by = findViewById(R.id.uploaded_by);
        date = findViewById(R.id.date);
        comments = findViewById(R.id.comments);
    }

    public void setData(String title, String uploaded_by, String date, String comments) {
        this.title.setText(title);
        this.uploaded_by.setText(uploaded_by);
        this.date.setText(date);
        this.comments.setText(comments);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}

