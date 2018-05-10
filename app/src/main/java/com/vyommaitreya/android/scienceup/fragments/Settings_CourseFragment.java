package com.vyommaitreya.android.scienceup.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vyommaitreya.android.scienceup.R;
import com.vyommaitreya.android.scienceup.adapters.FeedbackAdapter;
import com.vyommaitreya.android.scienceup.adapters.StudentsListAdapter;
import com.vyommaitreya.android.scienceup.database.Course;
import com.vyommaitreya.android.scienceup.database.Student;

import java.util.ArrayList;

public class Settings_CourseFragment extends Fragment implements View.OnClickListener {

    private Spinner mSpinner;
    private ArrayList<String> courses, courseID;
    private DatabaseReference mRef;
    String mCategory;
    TextInputEditText mCourseName;
    TextInputLayout mTextInputLayout;
    private ListView mListView;
    private ArrayList<String> mName, mID;
    private StudentsListAdapter mAdapter;
    private String curr_course;
    private ArrayAdapter<String> adapter;
    private TextInputEditText textInputEditText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_settings_course, container, false);

        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCategory = dataSnapshot.child("category").getValue(String.class);
                execute(rootView, dataSnapshot.child("course").child("course_name").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(rootView,databaseError.getMessage(),Snackbar.LENGTH_INDEFINITE).show();
            }
        });

        return rootView;
    }

    void execute(View view, final String course) {
        if (mCategory.equals("teacher")) {
            if(course == null) {
                Button add = view.findViewById(R.id.add_course_btn);
                mCourseName = view.findViewById(R.id.add_course);
                mTextInputLayout = view.findViewById(R.id.add_course_container);

                mTextInputLayout.setVisibility(View.VISIBLE);
                add.setVisibility(View.VISIBLE);
                add.setOnClickListener(this);
            }
            else {
                mListView = view.findViewById(R.id.list);
                mListView.setVisibility(View.VISIBLE);
                mName = new ArrayList<>();
                mID = new ArrayList<>();
                mAdapter = new StudentsListAdapter(getActivity(),mName,mID);
                mListView.setAdapter(mAdapter);
                mRef.child("courses").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mName.clear();
                        mID.clear();
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            if(ds.child("course_name").getValue(String.class).equals(course)) {
                                for(DataSnapshot students: ds.child("students").getChildren()) {
                                    mName.add(students.getValue(Student.class).getName());
                                    mID.add(students.getValue(Student.class).getId());
                                }
                                break;
                            }
                        }
                        mAdapter.updateData(mName,mID);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        } else {
            courses = new ArrayList<>();
            courseID = new ArrayList<>();

            mSpinner = view.findViewById(R.id.course);
            mSpinner.setVisibility(View.VISIBLE);
            view.findViewById(R.id.cc_container).setVisibility(View.VISIBLE);
            Button save_course = view.findViewById(R.id.save_course);
            save_course.setVisibility(View.VISIBLE);
            save_course.setOnClickListener(this);

            textInputEditText  = view.findViewById(R.id.cc);
            textInputEditText.setText("Will be updated automatically");

            try {
                adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, courses);
                mSpinner.setAdapter(adapter);

                curr_course = null;

                mRef.child("users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            curr_course = dataSnapshot.child("course").getValue().toString();
                            String course_id = dataSnapshot.child("course_ID").getValue().toString();
                            courses.add(curr_course);
                            courseID.add(course_id);
                            adapter.notifyDataSetChanged();
                            setCourseCoordinator(course_id);
                        } catch (NullPointerException e) {
                            selectCourse();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } catch (Exception e) { ; }
        }
    }

    private void setCourseCoordinator(String course_id) {
        mRef.child("courses").child(course_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textInputEditText.setText(dataSnapshot.child("course_coordinator").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void selectCourse() {
        mRef.child("courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                courses.clear();
                courseID.clear();
                courses.add("Select Course Name");
                courseID.add("0");
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    courses.add(ds.child("course_name").getValue(String.class));
                    courseID.add(ds.child("id").getValue(String.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.add_course_btn:
                String course_name = mCourseName.getText().toString();
                if(course_name == null || course_name.length() == 0) {
                    mCourseName.setError("Please provide a name");
                    mCourseName.requestFocus();
                }
                else {
                    String identity = mRef.child("courses").push().getKey();
                    Course course = new Course(identity, course_name, FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    mRef.child("courses").child(identity).setValue(course);
                    mRef.child("users").child(FirebaseAuth.getInstance().getUid()).child("course").setValue(course);
                    view.setVisibility(View.GONE);
                    mTextInputLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.save_course:
                int course = mSpinner.getSelectedItemPosition();
                mRef.child("users").child(FirebaseAuth.getInstance().getUid()).child("course").setValue(courses.get(course));
                mRef.child("users").child(FirebaseAuth.getInstance().getUid()).child("course_ID").setValue(courseID.get(course));
                mRef.child("courses").child(courseID.get(course)).child("students").child(FirebaseAuth.getInstance().getUid()).child("id").setValue(FirebaseAuth.getInstance().getUid());
                mRef.child("courses").child(courseID.get(course)).child("students").child(FirebaseAuth.getInstance().getUid()).child("name").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        }
    }
}
