package com.vyommaitreya.android.scienceup.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vyommaitreya.android.scienceup.R;
import com.vyommaitreya.android.scienceup.database.Radio;

import java.io.File;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class RadioAdd extends AppCompatActivity implements android.view.View.OnClickListener {

    private static final int PICK_AUDIO_REQUEST = 234;

    private EditText title, description;

    private String id;
    private boolean isEdit; //To check is dialog is opening as an edit dialog

    private DatabaseReference mRef;
    private StorageReference mStorageRef;
    private Uri mFilePath, mDownloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button done, upload;

        done = findViewById(R.id.done);
        upload = findViewById(R.id.upload_btn);
        title = findViewById(R.id.edit_text_title);
        description = findViewById(R.id.edit_text_description);

        done.setOnClickListener(this);
        upload.setOnClickListener(this);

        mRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.done:
                try {
                    if (!isEdit) id = mRef.push().getKey();
                    upload();
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.upload_btn:
                Intent intent = new Intent();
                intent.setType("audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Audio"), PICK_AUDIO_REQUEST);
            default:
                break;
        }
    }

    private void upload() {
        //Uri file = Uri.fromFile(new File(mFilePath.toString()));
        StorageReference riversRef = mStorageRef.child("radio/" + DateFormat.getDateTimeInstance().format(new Date()));

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        riversRef.putFile(mFilePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        mDownloadUrl = taskSnapshot.getDownloadUrl();
                        progressDialog.dismiss();
                        Toast.makeText(RadioAdd.this, "Upload successful", Toast.LENGTH_SHORT).show();
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        Calendar now = Calendar.getInstance();
                        String year = Integer.toString(now.get(Calendar.YEAR));
                        String month = Integer.toString(now.get(Calendar.MONTH));
                        if (month.length() == 1) month = 0 + month;
                        String day = Integer.toString(now.get(Calendar.DAY_OF_MONTH));
                        if (day.length() == 1) day = 0 + day;
                        String hour = Integer.toString(now.get(Calendar.HOUR_OF_DAY));
                        if (hour.length() == 1) hour = 0 + hour;
                        String minute = Integer.toString(now.get(Calendar.MINUTE));
                        if (minute.length() == 1) minute = 0 + minute;
                        String second = Integer.toString(now.get(Calendar.SECOND));
                        if (second.length() == 1) second = 0 + second;
                        String millis = Integer.toString(now.get(Calendar.MILLISECOND));
                        if (second.length() == 1) millis = 0 + millis;
                        if (second.length() == 2) millis = 0 + millis;
                        String timestamp = year + "." + month + "." + day + "." + " " + hour + ":" + minute + ":" + second+"."+millis;
                        Radio radio = new Radio(id, title.getText().toString(), currentUser.getDisplayName(), description.getText().toString(), timestamp, mDownloadUrl.toString());
                        mRef.child("radio").child(id).setValue(radio);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        progressDialog.dismiss();
                        Toast.makeText(RadioAdd.this, "Upload failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //calculating progress percentage
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        //displaying percentage in progress dialog
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_AUDIO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mFilePath = data.getData();
        }
    }
}
