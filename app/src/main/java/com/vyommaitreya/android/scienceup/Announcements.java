package com.vyommaitreya.android.scienceup;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Announcements extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        File directory = new File(Environment.getExternalStorageDirectory() + "/Science Up");
        if (!directory.exists()) directory.mkdirs();

        populateListView();

        int category;
        Intent i = getIntent();
        try {
            category = i.getExtras().getInt("category", 0);
        }
        catch (Exception e) {
            category = 0;
        }


        if(category==1) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Only teachers can add announcements", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
        else {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAddItemDialog(Announcements.this);
                }
            });
        }

        navigationView.getMenu().getItem(0).setChecked(true);
    }

    private void showAddItemDialog(Context c) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Add a new task")
                .setMessage("What do you want to do next?")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        try {
                            FileWriter fw = new FileWriter(Environment.getExternalStorageDirectory() + File.separator + "Science Up" + File.separator + "Announcements.txt", true);
                            BufferedWriter bw = new BufferedWriter(fw);
                            PrintWriter pw = new PrintWriter(bw);
                            pw.println(task);
                            pw.close();
                            bw.close();
                            fw.close();
                        }
                        catch(IOException e) {
                            Toast.makeText(Announcements.this, "Error writing: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        populateListView();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    void populateListView() {

        String s="";
        try {
            FileReader fr=new FileReader(Environment.getExternalStorageDirectory()+ File.separator+"Science Up" + File.separator + "Announcements.txt");
            BufferedReader br = new BufferedReader(fr);
            String t;
            int c=0;
            while((t = br.readLine()) != null)
            {
                s=s+"\n"+t;
                c++;
            }
            StringTokenizer st = new StringTokenizer(s,"\n");
            String[]list=new String[c];
            for(int i=0;i<c;i++) list[i]=st.nextToken();
            br.close();
            fr.close();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this,
                    R.layout.card_view,
                    list
            );
            ListView list_view = findViewById(R.id.list);
            list_view.setAdapter(adapter);
        }
        catch(IOException e) {
            Toast.makeText(this, "Error reading "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
        getMenuInflater().inflate(R.menu.announcements, menu);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } /*else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
