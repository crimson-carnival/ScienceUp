package com.vyommaitreya.android.scienceup.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vyommaitreya.android.scienceup.R;

import java.io.IOException;

public class FirstAfterLogin extends AppCompatActivity implements View.OnClickListener {

    private final static int IMAGE_CODE = 999;

    private FirebaseAuth mAuth;
    private EditText mDisplayName, mEmail;
    private FirebaseUser mUser;
    private ImageView mDisplayImage;
    private Uri mProfileImage, mDownloadUrl;
    private StorageReference mStorageRef;
    private ProgressBar mProgressBar;
    private LinearLayout mLinearLayout;
    private DatabaseReference mRef;
    private String mCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_after_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab = findViewById(R.id.fab);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference();
        mDisplayName = findViewById(R.id.display_name);
        mDisplayImage = findViewById(R.id.display_image);
        mEmail = findViewById(R.id.email);
        mProgressBar = findViewById(R.id.progress_bar);

        fab.setOnClickListener(this);
        mDisplayImage.setOnClickListener(this);

        if (mUser.getDisplayName() != null) mDisplayName.setText(mUser.getDisplayName());
        if (mUser.getPhotoUrl() != null) {
            Glide.with(this)
                    .load(mUser.getPhotoUrl())
                    .into(mDisplayImage);
        }
        if (mUser.getEmail() != null) mEmail.setText(mUser.getEmail());
        mRef.child("users").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCategory = dataSnapshot.child("category").getValue(String.class);
                if(mCategory!=null) {
                    findViewById(R.id.radio_group).setVisibility(View.GONE);
                    findViewById(R.id.text_prompt).setVisibility(View.GONE);
                }
                mProgressBar.setVisibility(View.GONE);
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
            case R.id.fab:
                if (isUsernameSet() && isCategorySet()) {
                    UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                            .setDisplayName(mDisplayName.getText().toString())
                            .build();
                    mUser.updateProfile(request);

                    mRef.child("users").child(mUser.getUid()).child("name").setValue(mDisplayName.getText().toString());
                    mRef.child("users").child(mUser.getUid()).child("category").setValue(mCategory);
                    Toast.makeText(this, "Information updated", Toast.LENGTH_SHORT).show();

                    SharedPreferences sharedPref = FirstAfterLogin.this.getSharedPreferences("com.vyommaitreya.android.scienceup", Context.MODE_PRIVATE);
                    sharedPref.edit().putString("username", mUser.getUid()).apply();

                    startActivity(new Intent(FirstAfterLogin.this, Dashboard.class));
                    finish();
                }
                break;
            case R.id.display_image:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Choose new display image."), IMAGE_CODE);
                break;
            default:
                break;
        }

    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.radio_student:
                if (checked)
                    // Pirates are the best
                    mCategory = "student";
                break;
            case R.id.radio_teacher:
                if (checked)
                    // Ninjas rule
                    mCategory = "teacher";
                break;
        }
    }

    boolean isUsernameSet() {
        if (mDisplayName.getText().toString().length() == 0) {
            mDisplayName.setError("You need to provide a display name");
            mDisplayName.requestFocus();
            return false;
        } else return true;
    }

    boolean isCategorySet() {
        if(mCategory == null) {
            Snackbar.make(findViewById(R.id.radio_student),"Please select a category",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mProfileImage = data.getData();
            uploadImage();
        }
    }

    private void uploadImage() {
        mStorageRef = FirebaseStorage.getInstance().getReference("profile_pictures/" + mUser.getUid() + ".jpg");
        mProgressBar.setVisibility(View.VISIBLE);

        if (mProfileImage != null) {
            final ProgressDialog progressDialog = new ProgressDialog(FirstAfterLogin.this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            mStorageRef.putFile(mProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            mDownloadUrl = taskSnapshot.getDownloadUrl();
                            mProgressBar.setVisibility(View.GONE);
                            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(mDownloadUrl)
                                    .build();
                            mUser.updateProfile(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Bitmap bitmap = null;
                                    try {
                                        bitmap = MediaStore.Images.Media.getBitmap(FirstAfterLogin.this.getContentResolver(), mProfileImage);
                                        mDisplayImage.setImageBitmap(bitmap);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(FirstAfterLogin.this, "Profile picture updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                            progressDialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            mProgressBar.setVisibility(View.GONE);
                            progressDialog.dismiss();
                            Toast.makeText(FirstAfterLogin.this, "Upload failed " + exception.getMessage(), Toast.LENGTH_SHORT).show();
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
    }

}
