package com.vyommaitreya.android.scienceup.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.vyommaitreya.android.scienceup.fragments.SubjectFragment;
import com.vyommaitreya.android.scienceup.R;

public class Academics extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager mViewPager;
    private Academics.SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Academics.this.setTheme(R.style.AppThemeTwo_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mSectionsPagerAdapter = new Academics.SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);

        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        navigationView.getMenu().getItem(1).setChecked(true);



        /*navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                mViewPager.setCurrentItem(0);
                                break;
                            case R.id.navigation_dashboard:
                                mViewPager.setCurrentItem(1);
                                break;
                            case R.id.navigation_notifications:
                                mViewPager.setCurrentItem(2);
                                break;
                        }
                        return false;
                    }
                });*/

        /*Button logoutButton = findViewById(R.id.action_logout);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = Academics.this.getSharedPreferences("com.vyommaitreya.android.scienceup", Context.MODE_PRIVATE);
                sharedPref.edit().putString("username", null).commit();
                sharedPref.edit().putString("password", null).commit();

                startActivity(new Intent(Academics.this, Login.class));
                finish();
            }
        });*/
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
        getMenuInflater().inflate(R.menu.grades, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {

            SharedPreferences sharedPref = Academics.this.getSharedPreferences("com.vyommaitreya.android.scienceup", Context.MODE_PRIVATE);
            sharedPref.edit().putString("username", null).commit();
            sharedPref.edit().putString("password", null).commit();

            startActivity(new Intent(Academics.this, Login.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_chat) {
            startActivity(new Intent(this, Chatroom.class));
            finish();
        } else if (id == R.id.nav_academics) {

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

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return new SubjectFragment();
                case 1:
                    return new SubjectFragment();
                case 2:
                    return new SubjectFragment();
                case 3:
                    return new SubjectFragment();
                case 4:
                    return new SubjectFragment();
                default:
                    return null;
            }
        }


        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }
    }
}
