package com.vyommaitreya.android.scienceup;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.vyommaitreya.android.scienceup.activities.Dashboard;
import com.vyommaitreya.android.scienceup.activities.Login;

public class Splash extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash);

        Window window = Splash.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Splash.this.getResources().getColor(R.color.primaryColorTwo));

        Thread timer = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    updateUI(currentUser);
                }
            }
        };
        timer.start();

        mAuth = FirebaseAuth.getInstance();

        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception e) {
            ;
        }
        currentUser = mAuth.getCurrentUser();

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) startActivity(new Intent(Splash.this, Dashboard.class));
        else startActivity(new Intent(Splash.this, Login.class));
        finish();
    }
}
