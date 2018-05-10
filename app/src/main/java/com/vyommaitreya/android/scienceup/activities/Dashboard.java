package com.vyommaitreya.android.scienceup.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.vyommaitreya.android.scienceup.R;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences mSharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        mSharedPref = Dashboard.this.getSharedPreferences("com.vyommaitreya.android.scienceup", Context.MODE_PRIVATE);
        if(mSharedPref.getString("username",null) == null) {
            startActivity(new Intent(Dashboard.this, Settings.class));
        }

        findViewById(R.id.fab).setOnClickListener(this);
        findViewById(R.id.academics_btn).setOnClickListener(this);
        findViewById(R.id.attendance_btn).setOnClickListener(this);
        findViewById(R.id.timetable_btn).setOnClickListener(this);
        findViewById(R.id.feedback_btn).setOnClickListener(this);
        findViewById(R.id.trends_btn).setOnClickListener(this);
        findViewById(R.id.campus_radio_btn).setOnClickListener(this);
        findViewById(R.id.settings_btn).setOnClickListener(this);
        findViewById(R.id.sos_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.academics_btn:
                startActivity(new Intent(Dashboard.this, Academics.class));
                break;
            case R.id.attendance_btn:
                startActivity(new Intent(Dashboard.this, Attendance.class));
                break;
            case R.id.timetable_btn:
                startActivity(new Intent(Dashboard.this, Timetable.class));
                break;
            case R.id.feedback_btn:
                startActivity(new Intent(Dashboard.this, Feedback.class));
                break;
            case R.id.trends_btn:
                startActivity(new Intent(Dashboard.this, Trends.class));
                break;
            case R.id.campus_radio_btn:
                startActivity(new Intent(Dashboard.this, CampusRadio.class));
                break;
            case R.id.settings_btn:
                startActivity(new Intent(Dashboard.this, Settings.class));
                break;
            case R.id.sos_btn:
                startActivity(new Intent(Dashboard.this, SOS.class));
                break;
            case R.id.fab:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked

                                mSharedPref.edit().putString("username", null).apply();

                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(Dashboard.this, Login.class));
                                finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
                builder.setMessage("Do you want to logout?")
                        .setNegativeButton("No", dialogClickListener)
                        .setPositiveButton("Yes", dialogClickListener)
                        .show();
                break;
            default:
                break;
        }
    }
}
