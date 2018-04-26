package com.vyommaitreya.android.scienceup.fragments;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vyommaitreya.android.scienceup.CustomAdapter;
import com.vyommaitreya.android.scienceup.activities.SOS;
import com.vyommaitreya.android.scienceup.database.Timetable;
import com.vyommaitreya.android.scienceup.dialogs.CustomDialogClass;
import com.vyommaitreya.android.scienceup.R;
import com.vyommaitreya.android.scienceup.database.TimetableContract;
import com.vyommaitreya.android.scienceup.database.TimetableDbHelper;

import java.util.StringTokenizer;

public class TimetableFragment extends Fragment {

    DatabaseReference mRef;
    String day;
    int d;
    boolean mClickable;
    CustomAdapter mCustomAdapter;
    ListView mListView;
    ProgressBar mProgressBar;
    String[] mTiming = {"N/A"};
    String[] mSubject = {"N/A"};
    String[] mRoom = {"N/A"};

    public void setDay(String day, int d) {
        this.day = day;
        this.d = d;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_timetable, container, false);

        int c = 0;
        String s = "", s2 = "", s3 = "";

        mClickable = false;

        mCustomAdapter = new CustomAdapter(getActivity(), mTiming, mSubject, mRoom);
        mListView = rootView.findViewById(R.id.list);
        mListView.setDivider(null);
        mListView.setAdapter(mCustomAdapter);

        mProgressBar = rootView.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.INVISIBLE);


        try {
            mRef = FirebaseDatabase.getInstance().getReference();
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    int c = 0;
                    int i = 0;
                    mProgressBar.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Timetable entry = ds.getValue(Timetable.class);
                        if (entry.getDay().equals(day)) c++;
                    }

                    if (c > 0) {
                        mClickable = true;
                        mTiming = new String[c];
                        mSubject = new String[c];
                        mRoom = new String[c];

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Timetable entry = ds.getValue(Timetable.class);
                            if (entry.getDay().equals(day)) {
                                mTiming[i] = entry.getTime();
                                mSubject[i] = entry.getSubject();
                                mRoom[i++] = entry.getRoom();
                            }
                        }
                    }
                    mCustomAdapter.updateData(mTiming,mSubject,mRoom);
                    /*mCustomAdapter = new CustomAdapter(getActivity(), mTiming, mSubject, mRoom);
                    mListView.setAdapter(mCustomAdapter);*/
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    // ...
                }
            };
            mRef.child("timetable").addValueEventListener(postListener);
        } catch (Exception e) {

        }

        try {

           /* TimetableDbHelper mDbHelper = new TimetableDbHelper(getContext());
            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            String[] projection = {
                    TimetableContract.ListEntry.COLUMN_NAME_TIME,
                    TimetableContract.ListEntry.COLUMN_NAME_SUBJECT,
                    TimetableContract.ListEntry.COLUMN_NAME_ROOM
            };

            String selection = TimetableContract.ListEntry.COLUMN_NAME_DAY + " = ?";
            String[] selectionArgs = {day};

            String sortOrder =
                    TimetableContract.ListEntry.COLUMN_NAME_TIME;

            Cursor cursor = db.query(
                    TimetableContract.ListEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            );
            String t, t2, t3;
            c = 0;
            while (cursor.moveToNext()) {
                t = cursor.getString(0);
                t2 = cursor.getString(1);
                t3 = cursor.getString(2);
                s = s + "\n" + t;
                s2 = s2 + "\n" + t2;
                s3 = s3 + "\n" + t3;
                c++;
            }
            cursor.close();

            StringTokenizer st = new StringTokenizer(s, "\n");
            StringTokenizer st2 = new StringTokenizer(s2, "\n");
            StringTokenizer st3 = new StringTokenizer(s3, "\n");
            if (c > 0) {
                clickable = true;
                list = new String[c];
                list2 = new String[c];
                list3 = new String[c];
            }

            final String timings[] = list;
            final String subjects[] = list2;
            final String rooms[] = list3;

            for (int i = 0; i < c; i++) {
                list[i] = st.nextToken();
                list2[i] = st2.nextToken();
                list3[i] = st3.nextToken();
            }
            db.close();
            mDbHelper.close();*/

            SwipeRefreshLayout mSwipeRefreshLayout;
            mSwipeRefreshLayout = rootView.findViewById(R.id.linearLayout);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.detach(TimetableFragment.this).attach(TimetableFragment.this).commit();
                        }
                    }, 100);
                }
            });

            if(mClickable) {
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        CustomDialogClass cdd = new CustomDialogClass(getActivity());

                        cdd.setDay(day);
                        cdd.setData(mTiming[i], mSubject[i], mRoom[i]);
                        cdd.show();

                        cdd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.detach(TimetableFragment.this).attach(TimetableFragment.this).commit();
                            }
                        });
                    }
                });
            }

        } catch (Exception e) {
        }
        return rootView;
    }
}
