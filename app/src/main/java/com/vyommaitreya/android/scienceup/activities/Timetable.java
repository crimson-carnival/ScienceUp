package com.vyommaitreya.android.scienceup.activities;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.vyommaitreya.android.scienceup.database.TimetableContract;
import com.vyommaitreya.android.scienceup.database.TimetableDbHelper;
import com.vyommaitreya.android.scienceup.dialogs.AddDialogue;
import com.vyommaitreya.android.scienceup.fragments.TimetableFragment;
import com.vyommaitreya.android.scienceup.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class Timetable extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager mViewPager;
    private Timetable.SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showAddItemDialog(Timetable.this);
                AddDialogue addDialogue = new AddDialogue(Timetable.this);
                int index = mViewPager.getCurrentItem() + 1;
                String day;
                if(index==1) day="Monday";
                else if(index==2) day="Tuesday";
                else if(index==3) day="Wednesday";
                else if(index==4) day="Thursday";
                else if(index==5) day="Friday";
                else if(index==6) day="Saturday";
                else day="Sunday";
                addDialogue.setDay(day);
                addDialogue.setIsEdit(false);
                addDialogue.show();

                addDialogue.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        Fragment f = (Fragment) getSupportFragmentManager().findFragmentByTag(getFragmentTag(mViewPager.getCurrentItem()));
                        f.getFragmentManager().beginTransaction().detach(f).attach(f).commit();

                    }
                });
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mSectionsPagerAdapter = new Timetable.SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);

        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        if(dayOfTheWeek.equals("Friday")) mViewPager.setCurrentItem(4,true);
        else if(dayOfTheWeek.equals("Monday")) mViewPager.setCurrentItem(0,true);
        else if(dayOfTheWeek.equals("Tuesday")) mViewPager.setCurrentItem(1,true);
        else if(dayOfTheWeek.equals("Wednesday")) mViewPager.setCurrentItem(2,true);
        else if(dayOfTheWeek.equals("Thursday")) mViewPager.setCurrentItem(3,true);
        else if(dayOfTheWeek.equals("Saturday")) mViewPager.setCurrentItem(5,true);
        else mViewPager.setCurrentItem(6,true);

        navigationView.getMenu().getItem(4).setChecked(true);
    }

    public String getFragmentTag(int pos) {
        return "android:switcher:" + R.id.container + ":" + pos;
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
        getMenuInflater().inflate(R.menu.timetable, menu);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        EditText v;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        private String convertTime(String s) {
            if(s.length()==1) return "0"+s;
            else return s;
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String s= convertTime(""+hourOfDay)+":"+convertTime(""+minute);
            v.setText(s);
        }

        public void setView(EditText view) {
            this.v = view;
        }
    }

    public void showTimePickerDialog(View v) {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.setView((EditText)v);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            TimetableFragment m;
            switch (position) {
                case 0:
                    m = new TimetableFragment(); m.setDay("Monday",1); return m;
                case 1:
                    m = new TimetableFragment(); m.setDay("Tuesday",2); return m;
                case 2:
                    m = new TimetableFragment(); m.setDay("Wednesday",3); return m;
                case 3:
                    m = new TimetableFragment(); m.setDay("Thursday",4); return m;
                case 4:
                    m = new TimetableFragment(); m.setDay("Friday",5); return m;
                case 5:
                    m = new TimetableFragment(); m.setDay("Saturday",6); return m;
                case 6:
                    m = new TimetableFragment(); m.setDay("Sunday",7); return m;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 7 total pages.
            return 7;
        }
    }
}
