package com.example.lasttry;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore firestoredb;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    String imagename= UUID.randomUUID().toString() + ".jpg";

    ImageButton button1;
    ImageButton button2;
    ImageButton button3;
    ImageButton button4;
    ImageButton button5;

    EditText imageedittext, captionedittext;
    Button submitbutton;
    ImageView imageView;
    FrameLayout frameLayout;
    int REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        button1 = (ImageButton) findViewById(R.id.imageButton1);
        button2 = (ImageButton) findViewById(R.id.imageButton2);
        button3 = (ImageButton) findViewById(R.id.imageButton3);
        button4 = (ImageButton) findViewById(R.id.imageButton4);
        button5 = (ImageButton) findViewById(R.id.imageButton5);
        imageedittext = (EditText) findViewById(R.id.imageeditText);
        captionedittext = (EditText) findViewById(R.id.captioneditText);
        submitbutton = (Button) findViewById(R.id.Submitbutton);
        imageView = (ImageView) findViewById(R.id.imageView);
        frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        firestoredb = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage(button1);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage(button2);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage(button3);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage(button4);
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalleryforImage();
            }
        });
        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitClicked();
            }
        });
        imageedittext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                drag(event, v);
                return false;
            }
        });
    }

    public void SelectImage(ImageButton button) {
        Bitmap bitmap = ((BitmapDrawable) button.getDrawable()).getBitmap();

        imageView.setImageBitmap(bitmap);
    }

    public void openGalleryforImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            assert data != null;
            imageView.setImageURI(data.getData());
        }
    }

    public Bitmap viewtoBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public void SubmitClicked() {
        Bitmap bitmap = viewtoBitmap(frameLayout);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        final String uid = mAuth.getCurrentUser().getUid();
        final String email = mAuth.getCurrentUser().getEmail();
        final String caption = captionedittext.getText().toString();

        UploadTask uploadTask = storage.getReference().child("Images").child(imagename).putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(getApplicationContext(), "Upload Failed!", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                final String[] url = {""};
                final Task<Uri> downloadUri = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                downloadUri.addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        url[0] = downloadUri.getResult().toString();
                        Log.i("URL", url[0]);

                        Postitems post = new Postitems(url[0],email, caption);

                        firestoredb.collection("Posts").add(post);
                        Toast.makeText(CreateActivity.this, "Post Added", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CreateActivity.this, HomeActivity.class));
                    }
                });
            }
        });
    }

    public void drag(MotionEvent event, View view){
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)view.getLayoutParams();
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                params.topMargin = (int) (event.getRawY()- (view.getHeight()+480));
                params.leftMargin = (int) (event.getRawX()- (view.getWidth()-180));
                view.setLayoutParams(params);
            case MotionEvent.ACTION_DOWN:
                params.topMargin = (int) (event.getRawY()- (view.getHeight()+480));
                params.leftMargin = (int) (event.getRawX()- (view.getWidth()-180));
                view.setLayoutParams(params);
            case MotionEvent.ACTION_UP:
                params.topMargin = (int) (event.getRawY()- (view.getHeight()+480));
                params.leftMargin = (int) (event.getRawX()- (view.getWidth()-180));
                view.setLayoutParams(params);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CreateActivity.this, HomeActivity.class));
    }
}