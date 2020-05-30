package com.example.lasttry;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore firestoredb;
    EditText emailtextview;
    EditText profilenameedittext;
    EditText descedittext;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        firestoredb = FirebaseFirestore.getInstance();
        emailtextview = (EditText) findViewById(R.id.emailtextview);
        profilenameedittext = (EditText)findViewById(R.id.profilenameedittext);
        descedittext = (EditText)findViewById(R.id.desceditText);

        String id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        firestoredb.collection("users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name = documentSnapshot.get("Name").toString();
                String email = documentSnapshot.get("Email").toString();
                String about = documentSnapshot.get("About").toString();
                profilenameedittext.setText(name);
                profilenameedittext.setEnabled(false);
                emailtextview.setText(email);
                emailtextview.setEnabled(false);
                descedittext.setText(about);
                descedittext.setEnabled(false);

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
    }
}
