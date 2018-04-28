package com.vyommaitreya.android.scienceup.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.vyommaitreya.android.scienceup.R;

public class TimetableOptionsDialogue extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button delete, edit;
    String text;
    String list[];
    int index, quad;
    private String timings, subjectName, room, day, id;

    public TimetableOptionsDialogue(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialogue_timetable_options);

        edit = findViewById(R.id.edit);
        delete = findViewById(R.id.delete);

        delete.setOnClickListener(this);
        edit.setOnClickListener(this);

    }

    public void setData(String timings, String subjectName, String room, String id)
    {
        this.timings = timings;
        this.subjectName = subjectName;
        this.room = room;
        this.id = id;
    }

    void setText(String text) {
        TextView text_dia = findViewById(R.id.txt_dia);
        text_dia.setText(text);
        this.text = text;
    }

    void setArray(String[] list) {
        this.list = new String[list.length];
        for (int i = 0; i < list.length; i++) {
            this.list[i] = list[i];
        }
    }

    public void setDay(String day) {
        this.day = day;
    }

    void setQuad(int quad) {
        this.quad = quad;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit:
                TimetableAddDialogue ed = new TimetableAddDialogue(c);
                ed.show();
                ed.setDay(day);
                ed.setIsEdit(true);
                ed.setData(timings.substring(0,5),timings.substring(8,13),subjectName,room,id);
                ed.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        dismiss();
                    }
                });
                break;
            case R.id.delete:
                TimetableDeleteDialogue dd = new TimetableDeleteDialogue(c);
                dd.setDay(day);
                dd.show();
                dd.setId(id);
                dd.setSubjectName(subjectName);
                dd.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        dismiss();
                    }
                });
            default:
                break;
        }
    }

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {

        super.setOnDismissListener(listener);
    }
}