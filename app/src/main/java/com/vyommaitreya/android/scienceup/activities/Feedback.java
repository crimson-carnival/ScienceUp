package com.vyommaitreya.android.scienceup.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vyommaitreya.android.scienceup.R;
import com.vyommaitreya.android.scienceup.adapters.FeedbackAdapter;
import com.vyommaitreya.android.scienceup.dialogs.FeedbackAddDialogue;

import java.util.ArrayList;

public class Feedback extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView mListView;
    private ProgressBar mProgressBar;
    private FeedbackAdapter mFeedbackAdapter;
    private ArrayList<String> mDate, mFeedback, mID;
    private DatabaseReference mRef;
    private String userID, new_id;
    private boolean mClickable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRef = FirebaseDatabase.getInstance().getReference();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_id = mRef.push().getKey();
                FeedbackAddDialogue fad = new FeedbackAddDialogue(Feedback.this, new_id, userID);
                fad.show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(5).setChecked(true);

        mListView = findViewById(R.id.list);
        mProgressBar = findViewById(R.id.progress_bar);

        mProgressBar.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.INVISIBLE);

        mDate = new ArrayList<>();
        mFeedback = new ArrayList<>();
        mID = new ArrayList<>();

        mFeedbackAdapter = new FeedbackAdapter(this, mDate, mFeedback, mID, userID);

        mListView.setDivider(null);
        mListView.setAdapter(mFeedbackAdapter);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDate.clear();
                mFeedback.clear();
                mID.clear();
                mProgressBar.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    com.vyommaitreya.android.scienceup.database.Feedback entry = ds.getValue(com.vyommaitreya.android.scienceup.database.Feedback.class);
                    mClickable = true;
                    mDate.add(entry.getDate());
                    mFeedback.add(entry.getFeedback());
                    mID.add(entry.getId());
                }

                if (!mClickable) {
                    mDate.add("N/A");
                    mFeedback.add("N/A");
                    mID.add("N/A");
                }
                mFeedbackAdapter.updateData(mDate, mFeedback, mID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(mListView, "Unable to fetch data", Snackbar.LENGTH_SHORT).show();
            }
        };
        mRef.child("users").child(userID).child("feedbacks").addValueEventListener(postListener);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.feedback, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_academics) {
            startActivity(new Intent(this, Academics.class));
            finish();
        } else if (id == R.id.nav_attendance) {
            startActivity(new Intent(this, Attendance.class));
            finish();
        } else if (id == R.id.nav_timetable) {
            startActivity(new Intent(this, Timetable.class));
            finish();
        } else if (id == R.id.nav_feedback) {

        } else if (id == R.id.nav_trends) {
            startActivity(new Intent(this, Trends.class));
            finish();
        } else if (id == R.id.nav_radio) {
            startActivity(new Intent(this, CampusRadio.class));
            finish();
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, Settings.class));
            finish();
        } else if (id == R.id.nav_sos) {
            startActivity(new Intent(this, SOS.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
