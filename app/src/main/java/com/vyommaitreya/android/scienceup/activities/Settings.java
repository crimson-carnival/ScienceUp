package com.vyommaitreya.android.scienceup.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.vyommaitreya.android.scienceup.R;
import com.vyommaitreya.android.scienceup.fragments.Settings_CourseFragment;
import com.vyommaitreya.android.scienceup.fragments.Settings_ProfileFragment;
import com.vyommaitreya.android.scienceup.fragments.Settings_SubjectsFragment;

public class Settings extends AppCompatActivity
        {

    ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;
    private Settings.SectionsPagerAdapter mSectionsPagerAdapter;
    private MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSectionsPagerAdapter = new Settings.SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mBottomNavigationView = findViewById(R.id.navigation);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mBottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_profile:
                                mViewPager.setCurrentItem(0);
                                break;
                            case R.id.navigation_subjects:
                                mViewPager.setCurrentItem(1);
                                break;
                            case R.id.navigation_course:
                                mViewPager.setCurrentItem(2);
                                break;
                        }
                        return false;
                    }
                });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    mBottomNavigationView.getMenu().getItem(0).setChecked(false);
                }

                mBottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = mBottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
                    return new Settings_ProfileFragment();
                case 1:
                    return new Settings_SubjectsFragment();
                case 2:
                    return new Settings_CourseFragment();
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
