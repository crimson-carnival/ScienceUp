package com.vyommaitreya.android.scienceup.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.vyommaitreya.android.scienceup.R;
import com.vyommaitreya.android.scienceup.database.TimetableContract;
import com.vyommaitreya.android.scienceup.database.TimetableDbHelper;

public class DeleteDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;

    private String subjectName, day;
    int index, quad;

    public DeleteDialog(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialogue_delete);

        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);

        yes.setOnClickListener(this);
        no.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yes:
                try {
                    TimetableDbHelper mDbHelper = new TimetableDbHelper(getContext());
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();

                    String selection = TimetableContract.ListEntry.COLUMN_NAME_SUBJECT + " LIKE ? AND " + TimetableContract.ListEntry.COLUMN_NAME_DAY + " LIKE ?";

                    String[] selectionArgs = {subjectName + "", day + ""};

                    int deletedRows = db.delete(TimetableContract.ListEntry.TABLE_NAME, selection, selectionArgs);
                    db.close();
                    mDbHelper.close();
                } catch (Exception e) {
                }

            default:
                break;
        }
        dismiss();
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setQuad(int quad) {
        this.quad = quad;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
