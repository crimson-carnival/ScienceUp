package com.vyommaitreya.android.scienceup.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.vyommaitreya.android.scienceup.R;

public class SubjectFragment extends ListFragment {

    public interface trial {
        void selected();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_subject, container, false);

        String[]list={"Temp data 1","Temp data 2"};
        final ListAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list);
        setListAdapter(adapter);

        if(getActivity().toString().equals("Academics")) {
            BottomNavigationView navigation = (BottomNavigationView) rootView.findViewById(R.id.navigation);
            navigation.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {

                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.navigation_assignments:
                                    break;
                                case R.id.navigation_grades:
                                    break;
                                case R.id.navigation_announcements:
                                    break;
                            }
                            return false;
                        }
                    });
        }

        SwipeRefreshLayout mSwipeRefreshLayout;
        mSwipeRefreshLayout = rootView.findViewById(R.id.linearLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(SubjectFragment.this).attach(SubjectFragment.this).commit();
                    }
                }, 100);
            }
        });

        return rootView;
    }

}