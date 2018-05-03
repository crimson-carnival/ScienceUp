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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vyommaitreya.android.scienceup.R;
import com.vyommaitreya.android.scienceup.adapters.TrendsAdapter;
import com.vyommaitreya.android.scienceup.dialogs.TrendsAddDialogue;

import java.util.ArrayList;

public class Trends extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView mListView;
    private ArrayList<String> mLinks;
    private DatabaseReference mRef;
    private FloatingActionButton mFab;
    private TrendsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(6).setChecked(true);

        mListView = findViewById(R.id.list);
        mRef = FirebaseDatabase.getInstance().getReference();
        mLinks = new ArrayList<>();
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mAdapter = new TrendsAdapter(Trends.this,mLinks);
        mListView.setAdapter(mAdapter);
        mRef.child("users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("category").getValue(String.class).equals("teacher")) mFab.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrendsAddDialogue trendsAddDialogue = new TrendsAddDialogue(Trends.this);
                trendsAddDialogue.show();
            }
        });

        mRef.child("trends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mLinks.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()) mLinks.add(ds.getValue(String.class));
                mAdapter.updateData(mLinks);
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

        if (id == R.id.nav_chat) {
            startActivity(new Intent(this, Chatroom.class));
            finish();
        } else if (id == R.id.nav_academics) {
            startActivity(new Intent(this, Academics.class));
            finish();
        } else if (id == R.id.nav_content) {
            startActivity(new Intent(this, StudyMaterial.class));
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

        } else if (id == R.id.nav_radio) {
            startActivity(new Intent(this, CampusRadio.class));
            finish();
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, Settings.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
