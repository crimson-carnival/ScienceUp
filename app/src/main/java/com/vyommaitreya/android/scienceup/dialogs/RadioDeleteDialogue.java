package com.vyommaitreya.android.scienceup.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vyommaitreya.android.scienceup.R;

public class RadioDeleteDialogue extends Dialog implements
        android.view.View.OnClickListener {

    private Activity c;
    private String url;
    private String id;

    private DatabaseReference mRef;
    private StorageReference mStorageRef;

    public void setUrl(String url) {
        this.url = url;
    }

    public RadioDeleteDialogue(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialogue_delete);

        Button yes, no;
        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);

        yes.setOnClickListener(this);
        no.setOnClickListener(this);

        mRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yes:
                try {
                    mRef.child("radio").child(id).removeValue();
                    FirebaseStorage.getInstance().getReferenceFromUrl(url).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // File deleted successfully
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Uh-oh, an error occurred!
                        }
                    });
                } catch (Exception e) {
                }

            default:
                break;
        }
        dismiss();
    }

    public void setId(String id) {
        this.id = id;
    }
}

