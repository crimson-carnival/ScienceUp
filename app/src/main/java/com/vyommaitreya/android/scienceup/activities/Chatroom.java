package com.vyommaitreya.android.scienceup.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.vyommaitreya.android.scienceup.fragments.SubjectFragment;
import com.vyommaitreya.android.scienceup.R;

public class Chatroom extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private android.support.v4.view.ViewPager mViewPager;
    private Chatroom.SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mSectionsPagerAdapter = new Chatroom.SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);

        mViewPager.setAdapter(mSectionsPagerAdapter);

        android.support.design.widget.TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        navigationView.getMenu().getItem(0).setChecked(true);
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
            startActivity(new Intent(this, Trends.class));
            finish();
        } else if (id == R.id.nav_radio) {
            startActivity(new Intent(this, CampusRadio.class));
            finish();
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, Settings.class));
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class SectionsPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return new SubjectFragment();
                case 1:
                    return new SubjectFragment();
                case 2:
                    return new SubjectFragment();
                default:
                    return null;
            }
        }


        @Override
        public int getCount() {
            // Show 5 total pages.
            return 3;
        }
    }
}
