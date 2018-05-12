package com.vyommaitreya.android.scienceup.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vyommaitreya.android.scienceup.R;
import com.vyommaitreya.android.scienceup.adapters.SOSAdapter;

import java.util.ArrayList;

public class SOS extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference myRef;
    private FirebaseUser mUser;
    private FloatingActionButton mFab;
    private Spinner mSpinner;
    private ListView mListView;
    private ArrayAdapter<String> mSubjectsAdapter;
    private ArrayList<String> mTeacherID, mTeacherName, mCourseNames;
    private ArrayList<Integer> mCount;
    private String mCourseID, mCourseName;
    private boolean mFlag;
    private SOSAdapter mSOSAdapter;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(1).setChecked(true);

        mFab = findViewById(R.id.fab);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference();
        mFlag = false;
        mProgressBar = findViewById(R.id.progress_bar);

        mTeacherID = new ArrayList<>();
        mTeacherName = new ArrayList<>();

        myRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTeacherID.clear();
                mTeacherName.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String s = ds.child("category").getValue(String.class);
                    if (s.equals("teacher")) {
                        mTeacherID.add(ds.getKey());
                        mTeacherName.add(ds.child("name").getValue(String.class));
                    }
                }
                if (!mFlag) proceed();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void proceed() {
        mFlag = true;
        myRef.child("users").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProgressBar.setVisibility(View.GONE);
                if (dataSnapshot.child("category").getValue(String.class).equals("student")) {
                    mCourseID = dataSnapshot.child("course_ID").getValue(String.class);
                    mCourseName = dataSnapshot.child("course").getValue(String.class);
                    intialiseStudent();
                } else intialiseTeacher();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void intialiseTeacher() {
        mListView = findViewById(R.id.list);
        mListView.setVisibility(View.VISIBLE);
        mCourseNames = new ArrayList<>();
        mCount = new ArrayList<>();
        mSOSAdapter = new SOSAdapter(SOS.this, mCourseNames, mCount);
        mListView.setAdapter(mSOSAdapter);
        mListView.setDivider(null);

        myRef.child("sos").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCourseNames.clear();
                mCount.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    mCourseNames.add(ds.child("course_name").getValue(String.class));
                    mCount.add((int) ds.getChildrenCount() - 1);
                }
                mSOSAdapter.updateData(mCourseNames, mCount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void intialiseStudent() {
        findViewById(R.id.student_container).setVisibility(View.VISIBLE);
        mSpinner = findViewById(R.id.subjects);
        mSubjectsAdapter = new ArrayAdapter<>(SOS.this, R.layout.support_simple_spinner_dropdown_item, mTeacherName);
        mSpinner.setAdapter(mSubjectsAdapter);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = mSpinner.getSelectedItemPosition();
                myRef.child("sos").child(mTeacherID.get(pos)).child(mCourseID).child("course_name").setValue(mCourseName);
                myRef.child("sos").child(mTeacherID.get(pos)).child(mCourseID).child(mUser.getUid()).setValue("yes");
                Snackbar.make(view,"SOS request added",Snackbar.LENGTH_SHORT).show();
            }
        });
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
            startActivity(new Intent(this, Feedback.class));
            finish();
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
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
