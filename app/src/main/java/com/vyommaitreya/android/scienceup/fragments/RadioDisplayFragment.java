package com.vyommaitreya.android.scienceup.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vyommaitreya.android.scienceup.R;
import com.vyommaitreya.android.scienceup.activities.CampusRadio;
import com.vyommaitreya.android.scienceup.adapters.RadioAdapter;
import com.vyommaitreya.android.scienceup.database.Radio;
import com.vyommaitreya.android.scienceup.dialogs.RadioInformationDialogue;

import java.util.ArrayList;
import java.util.Collections;

public class RadioDisplayFragment extends Fragment {

    private RadioAdapter mRadioAdapter;
    private ArrayList<String> mTitle, mArtist, mID, mDescription, mDate, mLink;
    private DatabaseReference mRef;
    private ListView mListView;
    private ProgressBar mProgressBar, mPlayProgress;
    private boolean mClickable;
    private MediaPlayer mPlayer;
    private int currentPlaying, totalTracks;
    private ImageView showInformation, previous, play, next;
    private TextView mPlayingTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_list_media_player, container, false);

        mTitle = new ArrayList<>();
        mArtist = new ArrayList<>();
        mID = new ArrayList<>();
        mDescription = new ArrayList<>();
        mDate = new ArrayList<>();
        mLink = new ArrayList<>();
        currentPlaying = -1;

        mRadioAdapter = new RadioAdapter(getActivity(), mTitle, mArtist, mID);
        mListView = rootView.findViewById(R.id.list);
        mProgressBar = rootView.findViewById(R.id.progress_bar);
        mPlayProgress = rootView.findViewById(R.id.play_progress);
        mPlayingTitle = rootView.findViewById(R.id.playing_title);
        mProgressBar.setVisibility(View.VISIBLE);
        mPlayProgress.setVisibility(View.GONE);
        mListView.setVisibility(View.INVISIBLE);

        mListView.setAdapter(mRadioAdapter);

        try {
            mRef = FirebaseDatabase.getInstance().getReference();
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mTitle.clear();
                    mArtist.clear();
                    mID.clear();
                    mDescription.clear();
                    mDate.clear();
                    mLink.clear();
                    totalTracks = 0;
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
                        mLink.add(entry.getUrl());
                        totalTracks++;
                    }

                    if (!mClickable) {
                        mTitle.add("N/A");
                        mArtist.add("N/A");
                        mID.add("N/A");
                        mDate.add("N/A");
                        mDescription.add("N/A");
                        mLink.add("N/A");
                        mLink.add("N/A");
                    }
                    Collections.reverse(mTitle);
                    Collections.reverse(mArtist);
                    Collections.reverse(mID);
                    Collections.reverse(mDate);
                    Collections.reverse(mDescription);
                    Collections.reverse(mLink);
                    mRadioAdapter.updateData(mTitle, mArtist, mID);
                    activateListener();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Snackbar.make(rootView, "Unable to fetch data", Snackbar.LENGTH_SHORT).show();
                }
            };
            mRef.child("radio").orderByChild("date").addValueEventListener(postListener);
        } catch (Exception e) {

        }

        //Player
        showInformation = rootView.findViewById(R.id.show_information);
        previous = rootView.findViewById(R.id.previous);
        play = rootView.findViewById(R.id.play);
        next = rootView.findViewById(R.id.next);

        showInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioInformationDialogue radioInformationDialogue = new RadioInformationDialogue(getActivity());
                radioInformationDialogue.show();
                radioInformationDialogue.setData(mTitle.get(currentPlaying), mArtist.get(currentPlaying), mDate.get(currentPlaying), mDescription.get(currentPlaying));
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (currentPlaying != 0) {
                        mListView.performItemClick(mListView.getChildAt(currentPlaying-1), currentPlaying-1, mListView.getItemIdAtPosition(currentPlaying-1));
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentPlaying == -1) mListView.performItemClick(mListView.getChildAt(0), 0, mListView.getItemIdAtPosition(0));
                else if (mPlayer.isPlaying()) {
                    mPlayer.pause();
                    play.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_play_circle_filled_black_24dp));
                } else {
                    mPlayer.start();
                    play.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_pause_circle_filled_black_24dp));
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (currentPlaying != totalTracks - 1) {
                        mListView.performItemClick(mListView.getChildAt(currentPlaying+1), currentPlaying+1, mListView.getItemIdAtPosition(currentPlaying+1));
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });

        mRadioAdapter.updateData(mTitle, mArtist, mID);

        return rootView;
    }

    void activateListener() {
        if (mClickable) {
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        if (currentPlaying != i) {
                            try { mListView.getChildAt(currentPlaying).setBackgroundColor(Color.parseColor("#ffffff")); } catch (Exception e) {;}
                            play.setVisibility(View.INVISIBLE);
                            mPlayProgress.setVisibility(View.VISIBLE);
                            play.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_pause_circle_filled_black_24dp));
                            if (mPlayer != null && mPlayer.isPlaying()) mPlayer.stop();
                            if (mPlayer != null) mPlayer.release();
                            mPlayer = new MediaPlayer();
                            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            mPlayer.setDataSource(mLink.get(i));
                            currentPlaying = i;
                            mPlayingTitle.setText(mTitle.get(currentPlaying));
                            view.setBackground(new ColorDrawable(getResources().getColor(R.color.primaryLightColorTwo)));
                            mPlayer.prepareAsync();
                            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mediaPlayer) {
                                    mPlayer.start();
                                    play.setVisibility(View.VISIBLE);
                                    mPlayProgress.setVisibility(View.GONE);
                                }
                            });
                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        // TODO: handle exception
                    }
                }
            });
        }
    }
}
