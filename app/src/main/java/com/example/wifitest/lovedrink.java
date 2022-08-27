package com.example.wifitest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class lovedrink extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentuser = auth.getCurrentUser();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference loveref = db.collection("love:"+currentuser.getEmail());
    private loveadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lovedrink);
        FloatingActionButton btnadd = findViewById(R.id.buttonaddlove);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(lovedrink.this,newlove.class));
            }
        });


        setuprecycleview();
    }
    private void setuprecycleview(){
        Query query = loveref.orderBy("lovename",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<loveuser> options = new FirestoreRecyclerOptions.Builder<loveuser>()
                .setQuery(query,loveuser.class)
                .build();
        adapter = new loveadapter(options);

        RecyclerView loverecyclerView = findViewById(R.id.loverecycle);

        loverecyclerView.setHasFixedSize(true);
        loverecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loverecyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteitem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(loverecyclerView);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}