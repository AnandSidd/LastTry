package com.example.lasttry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    private FirebaseFirestore firestoredb;
    private RecyclerView recyclerView;
    private PostAdapter mAdapter;
    ArrayList<Postitems> postlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        firestoredb = FirebaseFirestore.getInstance();

        firestoredb.collection("Posts").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for(DocumentSnapshot d : list){

                                Postitems postitems = d.toObject(Postitems.class);
                                Postitems post = new Postitems();
                                post.setPostUrl(postitems.getPostUrl());
                                post.setEmail(postitems.getEmail());
                                post.setCaption(postitems.getCaption());
                                postlist.add(post);
                                mAdapter.notifyDataSetChanged();

                            }
                        }
                    }
                });

        recyclerView = (RecyclerView)findViewById(R.id.Recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PostAdapter(postlist);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.create){
            startActivity(new Intent(HomeActivity.this, CreateActivity.class));
        }else if(item.getItemId()==R.id.profile){
            startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
        }else if(item.getItemId()==R.id.logout){
            mAuth.signOut();
            finish();
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
