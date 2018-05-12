package com.vyommaitreya.android.scienceup.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vyommaitreya.android.scienceup.R;
import com.vyommaitreya.android.scienceup.adapters.RadioAdapter;
import com.vyommaitreya.android.scienceup.database.Subject;
import com.vyommaitreya.android.scienceup.dialogs.SettingsAddSubjectDialogue;
import com.vyommaitreya.android.scienceup.dialogs.Settings_SubjectsOptionsDialogue;

import java.util.ArrayList;

public class Settings_SubjectsFragment extends Fragment implements View.OnClickListener {
    private ArrayList<String> mSubject, mTeacher, mID;
    private ListView mListView;
    private RadioAdapter mRadioAdapter;
    private DatabaseReference mRef;
    private boolean mClickable;
    private FirebaseUser mUser;
    private ProgressBar mProgressBar;
    private FloatingActionButton mFab;
    private String mCourseId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings_subject, container, false);

        mSubject = new ArrayList<>();
        mTeacher = new ArrayList<>();
        mID = new ArrayList<>();
        mClickable = false;

        mRef = FirebaseDatabase.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mListView = rootView.findViewById(R.id.list);
        mProgressBar = rootView.findViewById(R.id.progress_bar);
        mRadioAdapter = new RadioAdapter(getContext(), mSubject, mTeacher, mID);
        mFab = rootView.findViewById(R.id.fab);
        mFab.setVisibility(View.GONE);

        mRef.child("users").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.child("category").getValue().toString().equals("teacher")) {
                        mFab.setOnClickListener(Settings_SubjectsFragment.this);
                        mFab.setVisibility(View.VISIBLE);
                        inflateForTeacher();
                    } else {
                        mCourseId = dataSnapshot.child("course_ID").getValue().toString();
                        inflateForStudent();
                    }
                } catch (Exception e) {;}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mListView.setAdapter(mRadioAdapter);

        /*try {
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mSubject.clear();
                    mTeacher.clear();
                    mID.clear();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Subject entry = ds.getValue(Subject.class);
                        mClickable = true;
                        mSubject.add(entry.getName());
                        mTeacher.add(entry.getTeacher());
                        mID.add(entry.getId());
                    }

                    if (!mClickable) {
                        mSubject.add("N/A");
                        mTeacher.add("N/A");
                        mID.add("N/A");
                    }
                    mProgressBar.setVisibility(View.GONE);

                    mRadioAdapter.updateData(mSubject, mTeacher, mID);
                    activateListener();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getActivity(), "Unable to fetch data.", Toast.LENGTH_SHORT).show();
                }
            };
            mRef.child("users").child(mUser.getUid()).child("subjects").addValueEventListener(postListener);
        } catch (Exception e) {

        }*/

        return rootView;
    }

    private void inflateForStudent() {
        mRef.child("courses").child(mCourseId).child("subjects").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mSubject.clear();
                mTeacher.clear();
                mID.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Subject entry = ds.getValue(Subject.class);
                    mClickable = true;
                    mSubject.add(entry.getName());
                    mTeacher.add(entry.getTeacher());
                    mID.add(entry.getId());
                }

                if (!mClickable) {
                    mSubject.add("N/A");
                    mTeacher.add("N/A");
                    mID.add("N/A");
                }
                mProgressBar.setVisibility(View.GONE);

                mRadioAdapter.updateData(mSubject, mTeacher, mID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Unable to fetch data.", Toast.LENGTH_SHORT).show();
            }
        });
        return;
    }

    private void inflateForTeacher() {
        try {
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mSubject.clear();
                    mTeacher.clear();
                    mID.clear();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Subject entry = ds.getValue(Subject.class);
                        mClickable = true;
                        mSubject.add(entry.getName());
                        mTeacher.add("");
                        mID.add(entry.getId());
                    }

                    if (!mClickable) {
                        mSubject.add("N/A");
                        mTeacher.add("N/A");
                        mID.add("N/A");
                    }
                    mProgressBar.setVisibility(View.GONE);

                    mRadioAdapter.updateData(mSubject, mTeacher, mID);
                    activateListener();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getActivity(), "Unable to fetch data.", Toast.LENGTH_SHORT).show();
                }
            };
            mRef.child("users").child(mUser.getUid()).child("subjects").addValueEventListener(postListener);
        } catch (Exception e) {

        }
        return;
    }


    void activateListener() {
        if (mClickable) {
            /*mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Settings_SubjectsOptionsDialogue settingsSubjectsOptionsDialogue = new Settings_SubjectsOptionsDialogue(getActivity());
                    settingsSubjectsOptionsDialogue.setId(mID.get(i));
                    settingsSubjectsOptionsDialogue.show();
                }
            });*/
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.fab:
                SettingsAddSubjectDialogue settingsAddSubjectDialogue = new SettingsAddSubjectDialogue(getActivity(), mUser.getUid());
                settingsAddSubjectDialogue.show();
                break;
        }
    }
}