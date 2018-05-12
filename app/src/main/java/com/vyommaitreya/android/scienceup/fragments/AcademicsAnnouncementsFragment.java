package com.vyommaitreya.android.scienceup.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vyommaitreya.android.scienceup.R;
import com.vyommaitreya.android.scienceup.adapters.TrendsAdapter;
import com.vyommaitreya.android.scienceup.dialogs.AcademicsAnnouncementAddDialogue;

import java.util.ArrayList;

public class AcademicsAnnouncementsFragment extends Fragment {

    private ListView mListView;
    private ArrayList<String> mLinks;
    private DatabaseReference mRef;
    private FloatingActionButton mFab;
    private TrendsAdapter mAdapter;
    private String mCourseID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_academics_announcements, container, false);

        mListView = rootView.findViewById(R.id.list);
        mRef = FirebaseDatabase.getInstance().getReference();
        mLinks = new ArrayList<>();
        mFab = rootView.findViewById(R.id.fab);
        mAdapter = new TrendsAdapter(getContext(), mLinks);
        mListView.setAdapter(mAdapter);
        mListView.setDivider(null);
        mRef.child("users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("category").getValue(String.class).equals("teacher")) {
                    mFab.setVisibility(View.VISIBLE);
                    mCourseID = dataSnapshot.child("course").child("id").getValue(String.class);
                } else
                    mCourseID = dataSnapshot.child("course_ID").getValue(String.class);
                proceed();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

    private void proceed() {
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AcademicsAnnouncementAddDialogue academicsAnnouncementAddDialogue = new AcademicsAnnouncementAddDialogue(getActivity(), mCourseID);
                academicsAnnouncementAddDialogue.show();
            }
        });

        mRef.child("academics").child("announcements").child(mCourseID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mLinks.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                    mLinks.add(ds.getValue(String.class));
                mAdapter.updateData(mLinks);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
