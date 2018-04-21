package com.vyommaitreya.android.scienceup.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.vyommaitreya.android.scienceup.R;

public class EditDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button cancel, done;

    String list[];
    int index, quad;

    public EditDialog(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialogue_edit);
        TextView heading = findViewById(R.id.heading);
        heading.setText("Enter a new task");

        cancel = findViewById(R.id.cancel);
        done = findViewById(R.id.done);

        cancel.setOnClickListener(this);
        done.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done:
                try {

                    /*EditText edit_text = findViewById(R.id.edit_text);

                    TimetableDbHelper mDbHelper = new TimetableDbHelper(getContext());
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();

                    String title = edit_text.getText().toString();
                    if (title.indexOf("\n") == -1) {
                        ContentValues values = new ContentValues();
                        values.put(TimetableContract.ListEntry.COLUMN_NAME_TASK, title);

                        String selection = TimetableContract.ListEntry.COLUMN_NAME_TASK + " LIKE ?";
                        String[] selectionArgs = {list[index]};

                        int count = db.update(
                                TimetableContract.ListEntry.TABLE_NAME,
                                values,
                                selection,
                                selectionArgs);
                    }
                    db.close();
                    mDbHelper.close();*/
                } catch (Exception e) {
                }
            default:
                break;
        }
        dismiss();
    }


    void setText(String text) {
        EditText text_dia = findViewById(R.id.edit_text);
        text_dia.setText(text);
    }

    void setArray(String[] list) {
        this.list = new String[list.length];
        for (int i = 0; i < list.length; i++) {
            this.list[i] = list[i];
        }
    }

    void setIndex(int index) {
        this.index = index;
    }

    void setQuad(int quad) {
        this.quad = quad;
    }
}