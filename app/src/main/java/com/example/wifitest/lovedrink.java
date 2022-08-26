package com.example.wifitest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class lovedrink extends AppCompatActivity {

    private RecyclerView loverecyclerView;



    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentuser = auth.getCurrentUser();

    private FirebaseFirestore db;

    private FirestoreRecyclerAdapter adapter;

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

        db = FirebaseFirestore.getInstance();
        loverecyclerView = findViewById(R.id.loverecycle);

        Query query = db.collection("love:"+currentuser.getEmail());
        FirestoreRecyclerOptions<loveuser> options = new FirestoreRecyclerOptions.Builder<loveuser>()
                .setQuery(query, loveuser.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<loveuser, ProductsViewHolder>(options) {
            @NonNull
            @Override
            public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.love_item,parent,false);
                return new ProductsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProductsViewHolder holder, int position, @NonNull loveuser model) {
                holder.lovename.setText(model.getLovename());
                holder.lovedrink1.setText(model.getLovedrink1()+":");
                holder.lovedrink2.setText(model.getLovedrink2()+":");
                holder.lovedrink3.setText(model.getLovedrink3()+":");
                holder.lovedrink1ml.setText(model.getLovedrink1ml()+"ml");
                holder.lovedrink2ml.setText(model.getLovedrink2ml()+"ml");
                holder.lovedrink3ml.setText(model.getLovedrink3ml()+"ml");
            }
        };

        loverecyclerView.setHasFixedSize(true);
        loverecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loverecyclerView.setAdapter(adapter);


    }



    private class ProductsViewHolder extends  RecyclerView.ViewHolder{

        private TextView lovename,lovedrink1,lovedrink2,lovedrink3,lovedrink1ml,lovedrink2ml,lovedrink3ml;

        public ProductsViewHolder(@NonNull View itemView){
            super(itemView);
            lovename = itemView.findViewById(R.id.lovename);
            lovedrink1ml = itemView.findViewById(R.id.lovedrink1ml);
            lovedrink2ml = itemView.findViewById(R.id.lovedrink2ml);
            lovedrink3ml = itemView.findViewById(R.id.lovedrink3ml);
            lovedrink1 = itemView.findViewById(R.id.lovedrink1);
            lovedrink2 = itemView.findViewById(R.id.lovedrink2);
            lovedrink3 = itemView.findViewById(R.id.lovedrink3);
        }
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