package com.example.lasttry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    EditText emailedittext, nameedittext, passedittext, aboutedittext;
    Button signupbutton;
    FirebaseAuth mAuth;
    FirebaseFirestore firestoredb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        firestoredb = FirebaseFirestore.getInstance();
        emailedittext = (EditText)findViewById(R.id.emailedittext);
        passedittext = (EditText)findViewById(R.id.passeditText);
        nameedittext = (EditText)findViewById(R.id.NameET);
        aboutedittext = (EditText)findViewById(R.id.abouteditText);
        signupbutton = (Button)findViewById(R.id.buttonsignup);

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });
    }

    public void Register(){
        String email = emailedittext.getText().toString();
        String password = passedittext.getText().toString();
        if(email.isEmpty()){
            Toast.makeText(SignupActivity.this,"Email cannot be empty", Toast.LENGTH_SHORT).show();
        }else if(password.isEmpty()){
            Toast.makeText(SignupActivity.this,"Password cannot be empty", Toast.LENGTH_SHORT).show();
        }else{
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String id = mAuth.getCurrentUser().getUid();
                        String name = nameedittext.getText().toString();
                        String about = aboutedittext.getText().toString();
                        String email = emailedittext.getText().toString();
                        Map<String, String> user = new HashMap<>();
                        user.put("Name", name);
                        user.put("Email", email);
                        user.put("About", about);

                        firestoredb.collection("users").document(id).set(user);
                        startActivity(new Intent(SignupActivity.this, HomeActivity.class));
                    } else {
                        Toast.makeText(SignupActivity.this, "Registration Failed!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SignupActivity.this, MainActivity.class));
    }
}
