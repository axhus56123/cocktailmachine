package com.example.wifitest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Account extends AppCompatActivity {

    TextView tvEmail,tvname,tvid;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    Button logout,fixed;
    ImageButton back;
    EditText fixName;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        back = findViewById(R.id.managerBack);
        fixName = findViewById(R.id.fixName);
        fixed = findViewById(R.id.btnFix);
        tvname = findViewById(R.id.tvname);
        tvid = findViewById(R.id.tvid);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        db.collection("user").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        String name = documentSnapshot.get("name",String.class);
                        String id = documentSnapshot.get("id",String.class);
                        tvname.setText("Name:"+name);
                        tvid.setText("Id:"+id);
                    }
                }
            }
        });

        if(currentUser == null){
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
            return;
        }

        tvEmail = findViewById(R.id.tvEmail);
        tvEmail.setText("Email: "+ currentUser.getEmail());
        logout = findViewById(R.id.btnlogout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Account.this,bottomOption.class);
                startActivity(intent);
            }
        });
        fixed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("user").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot.exists()){
                                String name = String.valueOf(fixName.getText());
                                String id = documentSnapshot.get("id",String.class);
                                HashMap<String,Object> datauser = new HashMap<>();
                                datauser.put("name",name);
                                datauser.put("id",id);
                                db.collection("user").document(uid).set(datauser);
                                tvname.setText("Name:"+name);
                                tvid.setText("Id:"+id);
                            }
                        }
                    }
                });


            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users").child(currentUser.getUid());

    }

    private void logoutUser(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
        return;
    }
}