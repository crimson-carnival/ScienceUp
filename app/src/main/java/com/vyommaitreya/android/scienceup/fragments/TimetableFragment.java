package com.vyommaitreya.android.scienceup.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vyommaitreya.android.scienceup.CustomAdapter;
import com.vyommaitreya.android.scienceup.database.Timetable;
import com.vyommaitreya.android.scienceup.dialogs.CustomDialogClass;
import com.vyommaitreya.android.scienceup.R;

import java.util.ArrayList;

public class TimetableFragment extends Fragment {

    DatabaseReference mRef;
    String day;
    int d;

    boolean mClickable;
    CustomAdapter mCustomAdapter;
    ListView mListView;
    ProgressBar mProgressBar;
    ArrayList<String> mTiming, mSubject, mRoom, mId;

    public void setDay(String day, int d) {
        this.day = day;
        this.d = d;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_timetable, container, false);

        mTiming = new ArrayList<>();
        mSubject = new ArrayList<>();
        mRoom = new ArrayList<>();
        mId = new ArrayList<>();

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
                    mTiming.clear();
                    mSubject.clear();
                    mRoom.clear();
                    mId.clear();
                    mProgressBar.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Timetable entry = ds.getValue(Timetable.class);
                        mClickable = true;
                        mTiming.add(entry.getTime());
                        mSubject.add(entry.getSubject());
                        mRoom.add(entry.getRoom());
                        mId.add(entry.getId());
                    }

                    if (!mClickable) {
                        mTiming.add("N/A");
                        mSubject.add("N/A");
                        mRoom.add("N/A");
                        mId.add("N/A");
                    }
                    mCustomAdapter.updateData(mTiming, mSubject, mRoom);
                    activateListener();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Snackbar.make(rootView, "Unable to fetch data", Snackbar.LENGTH_SHORT).show();
                }
            };
            mRef.child("timetable").child(day).addValueEventListener(postListener);
        } catch (Exception e) {

        }
        return rootView;
    }

    void activateListener() {
        if (mClickable) {
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    CustomDialogClass cdd = new CustomDialogClass(getActivity());

                    cdd.setDay(day);
                    cdd.setData(mTiming.get(i), mSubject.get(i), mRoom.get(i), mId.get(i));
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
    }
}
