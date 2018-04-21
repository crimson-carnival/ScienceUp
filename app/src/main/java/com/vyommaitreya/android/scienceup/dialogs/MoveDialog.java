package com.vyommaitreya.android.scienceup.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;

import com.vyommaitreya.android.scienceup.R;
import com.vyommaitreya.android.scienceup.database.TimetableContract;
import com.vyommaitreya.android.scienceup.database.TimetableDbHelper;

public class MoveDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button shift, cancel_move;
    public RadioButton radio_q1, radio_q2, radio_q3, radio_q4, radio_q5, radio_q6, radio_q7;

    String list[];
    int index, quad, moveTo;

    public MoveDialog(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialogue_copy);

        shift = findViewById(R.id.shift);
        cancel_move = findViewById(R.id.cancel_move);
        radio_q1 = findViewById(R.id.radio_q1);
        radio_q2 = findViewById(R.id.radio_q2);
        radio_q3 = findViewById(R.id.radio_q3);
        radio_q4 = findViewById(R.id.radio_q4);
        radio_q5 = findViewById(R.id.radio_q5);
        radio_q6 = findViewById(R.id.radio_q6);
        radio_q7 = findViewById(R.id.radio_q7);

        shift.setOnClickListener(this);
        cancel_move.setOnClickListener(this);
        radio_q1.setOnClickListener(this);
        radio_q2.setOnClickListener(this);
        radio_q3.setOnClickListener(this);
        radio_q4.setOnClickListener(this);
        radio_q5.setOnClickListener(this);
        radio_q6.setOnClickListener(this);
        radio_q7.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.radio_q1:
                moveTo = 1;
                radio_q1.setChecked(true);
                radio_q2.setChecked(false);
                radio_q3.setChecked(false);
                radio_q4.setChecked(false);
                radio_q5.setChecked(false);
                radio_q6.setChecked(false);
                radio_q7.setChecked(false);
                break;
            case R.id.radio_q2:
                moveTo = 2;
                radio_q1.setChecked(false);
                radio_q2.setChecked(true);
                radio_q3.setChecked(false);
                radio_q4.setChecked(false);
                radio_q5.setChecked(false);
                radio_q6.setChecked(false);
                radio_q7.setChecked(false);
                break;
            case R.id.radio_q3:
                moveTo = 3;
                radio_q1.setChecked(false);
                radio_q2.setChecked(false);
                radio_q3.setChecked(true);
                radio_q4.setChecked(false);
                radio_q5.setChecked(false);
                radio_q6.setChecked(false);
                radio_q7.setChecked(false);
                break;
            case R.id.radio_q4:
                moveTo = 4;
                radio_q1.setChecked(false);
                radio_q2.setChecked(false);
                radio_q3.setChecked(false);
                radio_q4.setChecked(true);
                radio_q5.setChecked(true);
                radio_q6.setChecked(true);
                radio_q7.setChecked(true);
                break;
            case R.id.radio_q5:
                moveTo = 5;
                radio_q1.setChecked(false);
                radio_q2.setChecked(false);
                radio_q3.setChecked(false);
                radio_q4.setChecked(false);
                radio_q5.setChecked(true);
                radio_q6.setChecked(false);
                radio_q7.setChecked(false);
                break;
            case R.id.radio_q6:
                moveTo = 6;
                radio_q1.setChecked(false);
                radio_q2.setChecked(false);
                radio_q3.setChecked(false);
                radio_q4.setChecked(false);
                radio_q5.setChecked(false);
                radio_q6.setChecked(true);
                radio_q7.setChecked(false);
                break;
            case R.id.radio_q7:
                moveTo = 7;
                radio_q1.setChecked(false);
                radio_q2.setChecked(false);
                radio_q3.setChecked(false);
                radio_q4.setChecked(false);
                radio_q5.setChecked(false);
                radio_q6.setChecked(false);
                radio_q7.setChecked(true);
                break;
            case R.id.shift:
                if (moveTo != 0) {
                    try {
                        TimetableDbHelper mDbHelper = new TimetableDbHelper(getContext());
                        SQLiteDatabase db = mDbHelper.getWritableDatabase();

                        int item=moveTo;
                        String day;
                        if(item==1) day="Monday";
                        else if(item==2) day="Tuesday";
                        else if(item==3) day="Wednesday";
                        else if(item==4) day="Thursday";
                        else if(item==5) day="Friday";
                        else if(item==6) day="Saturday";
                        else day="Sunday";
                        ContentValues values = new ContentValues();
                        values.put(TimetableContract.ListEntry.COLUMN_NAME_DAY, day);

                        String selection = TimetableContract.ListEntry.COLUMN_NAME_SUBJECT + " LIKE ?";
                        String[] selectionArgs = {list[index]};

                        int count = db.update(
                                TimetableContract.ListEntry.TABLE_NAME,
                                values,
                                selection,
                                selectionArgs);
                    } catch (Exception e) {
                    }
                    dismiss();
                }
                break;
            case R.id.cancel_move:
            default:
                dismiss();
                break;
        }
    }

    public void setArray(String[] list) {
        this.list = new String[list.length];
        for (int i = 0; i < list.length; i++) {
            this.list[i] = list[i];
        }
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setQuad(int quad) {
        this.quad = quad;
    }
}
