package com.vyommaitreya.android.scienceup.fragments;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vyommaitreya.android.scienceup.R;
import com.vyommaitreya.android.scienceup.activities.CampusRadio;
import com.vyommaitreya.android.scienceup.activities.RadioAdd;
import com.vyommaitreya.android.scienceup.adapters.RadioAdapter;
import com.vyommaitreya.android.scienceup.database.Radio;
import com.vyommaitreya.android.scienceup.dialogs.RadioInformationDialogue;
import com.vyommaitreya.android.scienceup.dialogs.RadioOptionsDialogue;

import java.util.ArrayList;

public class RadioAddFragment extends Fragment {
    private RadioAdapter mRadioAdapter;
    private ArrayList<String> mTitle, mArtist, mID, mDescription, mDate, mLink;
    private DatabaseReference mRef;
    private StorageReference mStorageRef;
    private ListView mListView;
    private ProgressBar mProgressBar;
    private boolean mClickable;
    private MediaPlayer mPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_list_radio_add, container, false);

        mTitle = new ArrayList<>();
        mArtist = new ArrayList<>();
        mID = new ArrayList<>();
        mDescription = new ArrayList<>();
        mDate = new ArrayList<>();
        mLink = new ArrayList<>();

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
                startActivity(new Intent(getActivity(), RadioAdd.class));
            }
        });

        try {
            mRef = FirebaseDatabase.getInstance().getReference();
            mStorageRef = FirebaseStorage.getInstance().getReference();
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mTitle.clear();
                    mArtist.clear();
                    mID.clear();
                    mDescription.clear();
                    mDate.clear();
                    mLink.clear();
                    mProgressBar.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Radio entry = ds.getValue(Radio.class);
                        if(entry.getArtist().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                            mClickable = true;
                            mTitle.add(entry.getTitle());
                            mArtist.add(entry.getArtist());
                            mID.add(entry.getId());
                            mDate.add(entry.getDate());
                            mDescription.add(entry.getDescription());
                            mLink.add(entry.getUrl());

                            /*final long ONE_MEGABYTE = 1024 * 1024;
                            mStorageRef.child(entry.getUrl()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    // Data for "images/island.jpg" is returns, use this as needed
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });*/
                        }
                    }

                    if (!mClickable) {
                        mTitle.add("N/A");
                        mArtist.add("N/A");
                        mID.add("N/A");
                        mDate.add("N/A");
                        mDescription.add("N/A");
                        mLink.add("N/A");
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

        }

        mRadioAdapter.updateData(mTitle, mArtist, mID);

        //MediaPlayer

        return rootView;
    }

    void activateListener() {
        if (mClickable) {
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        RadioOptionsDialogue radioOptionsDialogue = new RadioOptionsDialogue(getActivity());
                        radioOptionsDialogue.setId(mID.get(i));
                        radioOptionsDialogue.setUrl(mLink.get(i));
                        radioOptionsDialogue.show();
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
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
