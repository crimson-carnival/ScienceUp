package com.vyommaitreya.android.scienceup.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vyommaitreya.android.scienceup.R;
import com.vyommaitreya.android.scienceup.adapters.AttendanceStudentAdapter;
import com.vyommaitreya.android.scienceup.adapters.AttendanceTeacherAdapter;
import com.vyommaitreya.android.scienceup.database.Subject;
import com.vyommaitreya.android.scienceup.dialogs.AttendanceStudentDialogue;
import com.vyommaitreya.android.scienceup.dialogs.AttendanceTeacherDialogue;

import java.util.ArrayList;

public class Attendance extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference mRef;
    private FirebaseUser mUser;
    private String mCategory;
    private ListView mListView;
    private FloatingActionButton mFab;
    private AttendanceStudentAdapter mStudentAdapter;
    private AttendanceTeacherAdapter mTeacherAdapter;
    private ArrayList<String> mSubjectName, mSubjectID, mCourseID;
    private ArrayList<Integer> mTotal, mPresent;
    private boolean mClickable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(3).setChecked(true);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference();
        mFab = findViewById(R.id.fab);
        mListView = findViewById(R.id.list);
        mFab.setVisibility(View.GONE);
        mClickable = false;

        mRef.child("users").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCategory = dataSnapshot.child("category").getValue(String.class);
                if (mCategory.equals("teacher")) initiateTeacher();
                else initiateStudent();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initiateStudent() {

        mSubjectName = new ArrayList<>();
        mPresent = new ArrayList<>();
        mTotal = new ArrayList<>();
        mStudentAdapter = new AttendanceStudentAdapter(Attendance.this, mSubjectName, mPresent, mTotal, mUser.getUid());
        mListView.setDivider(null);
        mListView.setAdapter(mStudentAdapter);
        findViewById(R.id.attendance_layout).setBackgroundColor(Color.parseColor("#e0e0e0"));

        mRef.child("users").child(mUser.getUid()).child("subjects").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mSubjectName.clear();
                mPresent.clear();
                mTotal.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    mSubjectName.add(ds.child("name").getValue(String.class));
                    mPresent.add(ds.child("attendance").child("attended").getValue(Integer.class));
                    mTotal.add(ds.child("attendance").child("total").getValue(Integer.class));
                }

                mStudentAdapter.updateData(mSubjectName, mPresent, mTotal);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mRef.child("users").child(mUser.getUid()).child("attendance_open").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status = dataSnapshot.child("status").getValue(String.class);
                final String subject = dataSnapshot.child("subject_id").getValue(String.class);
                if (status != null && status.equals("open")) {
                    mFab.setVisibility(View.VISIBLE);
                    /*mRef.child("users").child(mUser.getUid()).child("subjects").child(subject).child("attendance").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int total = dataSnapshot.child("total").getValue(Integer.class);
                            mRef.child("users").child(mUser.getUid()).child("subjects").child(subject).child("attendance").child("total").setValue(total + 1);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });*/
                    mFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AttendanceStudentDialogue attendanceStudentDialogue = new AttendanceStudentDialogue(Attendance.this, mUser.getUid(), subject);
                            attendanceStudentDialogue.show();
                        }
                    });
                } else mFab.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initiateTeacher() {
        mSubjectName = new ArrayList<>();
        mSubjectID = new ArrayList<>();
        mCourseID = new ArrayList<>();

        mTeacherAdapter = new AttendanceTeacherAdapter(Attendance.this, mSubjectName, mSubjectID, mCourseID);

        mListView.setAdapter(mTeacherAdapter);

        mRef.child("users").child(mUser.getUid()).child("subjects").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mSubjectName.clear();
                mSubjectID.clear();
                mCourseID.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    mClickable = true;
                    Subject subject = ds.getValue(Subject.class);
                    mSubjectID.add(subject.getId());
                    mCourseID.add(subject.getTeacher());
                    mSubjectName.add(subject.getName());
                }
                activateListener();
                mTeacherAdapter.updateData(mSubjectName, mSubjectID, mCourseID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
            startActivity(new Intent(this, SOS.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void activateListener() {
        if (mClickable) {
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    AttendanceTeacherDialogue attendanceTeacherDialogue = new AttendanceTeacherDialogue(Attendance.this, mSubjectName.get(i), mSubjectID.get(i), mCourseID.get(i));
                    attendanceTeacherDialogue.show();
                }
            });
        }
    }
}
