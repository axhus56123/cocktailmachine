package com.example.wifitest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class recommend extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentuser = auth.getCurrentUser();
    private ImageView back;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference reRef = db.collection("Recommend");
    private reAdapter adapter;
    FloatingActionButton btnadd;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        back = findViewById(R.id.Back);
        btnadd = findViewById(R.id.REAdd);
        btnadd.setVisibility(View.GONE);
        String email = currentuser.getEmail();
        if(email.equals("system@gmail.com")){
            btnadd.setVisibility(View.VISIBLE);
        }

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(recommend.this,addRecommend.class));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(recommend.this,MainActivity.class);
                startActivity(intent);
            }
        });
        setuprecycleview();
    }
    private void setuprecycleview(){
        String email = currentuser.getEmail();
        Query query = reRef.orderBy("Rename",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<reuser> options = new FirestoreRecyclerOptions.Builder<reuser>()
                .setQuery(query,reuser.class)
                .build();
        adapter = new reAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recyview);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        if(email.equals("system@gmail.com")){
            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    adapter.deleteitem(viewHolder.getAdapterPosition());
                }
            }).attachToRecyclerView(recyclerView);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}