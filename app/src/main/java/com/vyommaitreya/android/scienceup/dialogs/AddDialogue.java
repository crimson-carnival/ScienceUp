package com.vyommaitreya.android.scienceup.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vyommaitreya.android.scienceup.R;
import com.vyommaitreya.android.scienceup.activities.TestArtist;
import com.vyommaitreya.android.scienceup.database.Timetable;
import com.vyommaitreya.android.scienceup.database.TimetableContract;
import com.vyommaitreya.android.scienceup.database.TimetableDbHelper;

public class AddDialogue extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button done, cancel;
    public EditText subjectName, room, from, to;

    String list[];
    int index;
    private String day, subject, id;
    private boolean isEdit; //To check is dialog is opening as an edit dialog

    DatabaseReference mRef;

    public AddDialogue(Activity a) {
        super(a);
        this.c = a;
        isEdit = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialogue_add);

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

    public void setData(String from, String to, String subjectName, String room) {
        this.from.setText(from);
        this.to.setText(to);
        this.subjectName.setText(subjectName);
        subject = subjectName;
        this.room.setText(room);
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
                    id = mRef.push().getKey();
                    Timetable artist = new Timetable(id,day.trim(),from.getText().toString().trim() + " - " + to.getText().toString().trim(),subjectName.getText().toString().trim(),room.getText().toString().trim());
                    mRef.child("timetable").child(id).setValue(artist);
                    /*TimetableDbHelper mDbHelper = new TimetableDbHelper(getContext());
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();

                    String selection = TimetableContract.ListEntry.COLUMN_NAME_SUBJECT + " LIKE ?";*/

                    /*String day;
                    if(index==1) day="Monday";
                    else if(index==2) day="Tuesday";
                    else if(index==3) day="Wednesday";
                    else if(index==4) day="Thursday";
                    else if(index==5) day="Friday";
                    else if(index==6) day="Saturday";
                    else day="Sunday";*/
                    try {
                        /*TimetableDbHelper mDbHelper = new TimetableDbHelper(c);
                        SQLiteDatabase db = mDbHelper.getWritableDatabase();

                        if(isEdit) {
                            String selection = TimetableContract.ListEntry.COLUMN_NAME_SUBJECT + " LIKE ? AND " + TimetableContract.ListEntry.COLUMN_NAME_DAY + " LIKE ?";

                            String[] selectionArgs = {subject, day + ""};

                            int deletedRows = db.delete(TimetableContract.ListEntry.TABLE_NAME, selection, selectionArgs);
                        }

                        ContentValues values = new ContentValues();
                        values.put(TimetableContract.ListEntry.COLUMN_NAME_DAY, day.trim());
                        values.put(TimetableContract.ListEntry.COLUMN_NAME_TIME, from.getText().toString().trim() + " - " + to.getText().toString().trim());
                        values.put(TimetableContract.ListEntry.COLUMN_NAME_SUBJECT, subjectName.getText().toString().trim() + "");
                        values.put(TimetableContract.ListEntry.COLUMN_NAME_ROOM, room.getText().toString().trim() + "");

                        // Insert the new row, returning the primary key value of the new row
                        long newRowId = db.insert(TimetableContract.ListEntry.TABLE_NAME, null, values);


                        db.close();
                        mDbHelper.close();
                        */
                    } catch (Exception e) {
                        Toast.makeText(c, "Error writing: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }

            default:
                break;
        }
        dismiss();
    }

    void setArray(String[] list) {
        this.list = new String[list.length];
        for (int i = 0; i < list.length; i++) {
            this.list[i] = list[i];
        }
    }

    public void setIndex(int index) {
        this.index = index;
    }

}

