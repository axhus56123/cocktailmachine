package com.example.wifitest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class history extends AppCompatActivity {

    private RecyclerView hrecyclerView;
    private ImageButton back;


    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentuser = auth.getCurrentUser();
    ProgressDialog progressDialog;
    private FirebaseFirestore db;

    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        db = FirebaseFirestore.getInstance();
        hrecyclerView = findViewById(R.id.hisrecycle);
        back = findViewById(R.id.hisBack);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(history.this,bottomOption.class);
                startActivity(intent);
            }
        });

        Query query = db.collection("history:"+currentuser.getEmail());
        FirestoreRecyclerOptions<hisuser> options = new FirestoreRecyclerOptions.Builder<hisuser>()
                .setQuery(query, hisuser.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<hisuser, ProductsViewHolder>(options) {
            @NonNull
            @Override
            public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
                return new ProductsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProductsViewHolder holder, int position, @NonNull hisuser model) {
                holder.hisdrink1.setText(model.getHisdrink1()+"");
                holder.hisdrink2.setText(model.getHisdrink2()+"");
                holder.hisdrink3.setText(model.getHisdrink3()+"");
                holder.hisdrink4.setText(model.getHisdrink4()+"");
                holder.hisdrink5.setText(model.getHisdrink5()+"");
                holder.time.setText(model.getTime()+"");
            }
        };

        hrecyclerView.setHasFixedSize(true);
        hrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        hrecyclerView.setAdapter(adapter);


    }
    static class ProductsViewHolder extends  RecyclerView.ViewHolder{

        private TextView hisdrink1,hisdrink2,hisdrink3,hisdrink4,hisdrink5,time;

        public ProductsViewHolder(@NonNull View itemView){
            super(itemView);
            hisdrink1 = itemView.findViewById(R.id.hisdrink1);
            hisdrink2 = itemView.findViewById(R.id.hisdrink2);
            hisdrink3 = itemView.findViewById(R.id.hisdrink3);
            hisdrink4 = itemView.findViewById(R.id.hisdrink4);
            hisdrink5 = itemView.findViewById(R.id.hisdrink5);
            time = itemView.findViewById(R.id.histime);
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