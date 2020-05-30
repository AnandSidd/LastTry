package com.example.lasttry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText emaileditText, passeditText;
    Button btnlogin, btnsignup;
    TextView forgetPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        emaileditText = (EditText)findViewById(R.id.emailET);
        passeditText = (EditText)findViewById(R.id.passET);
        btnlogin = (Button)findViewById(R.id.loginbtn);
        btnsignup = (Button)findViewById(R.id.signupbtn);
        forgetPass = (TextView)findViewById(R.id.forget);

        if(mAuth.getCurrentUser()!= null){
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
        }

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignupActivity.class));
            }
        });
        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emaileditText.getText().toString();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(MainActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                }else {
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Reset Link sent!", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(MainActivity.this, "Unable to send Reset Link", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    public void login(){
        String email = emaileditText.getText().toString();
        String password = passeditText.getText().toString();

        if (email.isEmpty()) {
            Toast.makeText(MainActivity.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();

        }
        else if (password.isEmpty()) {
            Toast.makeText(MainActivity.this, "Please Enter Your password", Toast.LENGTH_SHORT).show();

        }
        else{
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
