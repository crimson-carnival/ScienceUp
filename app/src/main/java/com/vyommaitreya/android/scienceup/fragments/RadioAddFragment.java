package com.vyommaitreya.android.scienceup.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vyommaitreya.android.scienceup.R;
import com.vyommaitreya.android.scienceup.activities.CampusRadio;
import com.vyommaitreya.android.scienceup.adapters.RadioAdapter;
import com.vyommaitreya.android.scienceup.database.Radio;
import com.vyommaitreya.android.scienceup.dialogs.RadioAddDialogue;
import com.vyommaitreya.android.scienceup.dialogs.RadioInformationDialogue;

import java.util.ArrayList;

public class RadioAddFragment extends Fragment {
    private RadioAdapter mRadioAdapter;
    private ArrayList<String> mTitle, mArtist, mID, mDescription, mDate;
    private DatabaseReference mRef;
    private ListView mListView;
    private ProgressBar mProgressBar;
    private boolean mClickable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_list_radio_add, container, false);

        mTitle = new ArrayList<>();
        mArtist = new ArrayList<>();
        mID = new ArrayList<>();
        mDescription = new ArrayList<>();
        mDate = new ArrayList<>();

        mRadioAdapter = new RadioAdapter(getActivity(), mTitle, mArtist, mID);
        mListView = rootView.findViewById(R.id.list);
        mProgressBar = rootView.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.INVISIBLE);

        mListView.setAdapter(mRadioAdapter);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioAddDialogue radioAddDialogue = new RadioAddDialogue(getActivity());
                radioAddDialogue.show();
            }
        });

        /*try {
            mRef = FirebaseDatabase.getInstance().getReference();
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mTitle.clear();
                    mArtist.clear();
                    mID.clear();
                    mDescription.clear();
                    mDate.clear();
                    mProgressBar.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Radio entry = ds.getValue(Radio.class);
                        mClickable = true;
                        mTitle.add(entry.getTitle());
                        mArtist.add(entry.getArtist());
                        mID.add(entry.getId());
                        mDate.add(entry.getDate());
                        mDescription.add(entry.getDescription());
                    }

                    if (!mClickable) {
                        mTitle.add("N/A");
                        mArtist.add("N/A");
                        mID.add("N/A");
                        mDate.add("N/A");
                        mDescription.add("N/A");
                    }
                    mRadioAdapter.updateData(mTitle, mArtist, mID);
                    activateListener();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Snackbar.make(rootView, "Unable to fetch data", Snackbar.LENGTH_SHORT).show();
                }
            };
            mRef.child("radio").addValueEventListener(postListener);
        } catch (Exception e) {

        }*/
        for (int i = 0; i < 9; i++) {

            mProgressBar.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            mTitle.add("N/A");
            mArtist.add("N/A");
            mID.add("N/A");
            mDate.add("N/A");
            mDescription.add("N/A");
        }

        mRadioAdapter.updateData(mTitle, mArtist, mID);
        return rootView;
    }

    void activateListener() {
        if (mClickable) {
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    /*TimetableOptionsDialogue cdd = new TimetableOptionsDialogue(getActivity());

                    cdd.setData(mTitle.get(i), mArtist.get(i), mID.get(i));
                    cdd.show();

                    cdd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.detach(RadioDisplayFragment.this).attach(RadioDisplayFragment.this).commit();
                        }
                    });*/
                }
            });
        }
    }
}
