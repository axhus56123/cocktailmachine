package com.example.wifitest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class newlove extends AppCompatActivity {
    private EditText newlovename,newlove1,newlove2,newlove3,newlove1ml,newlove2ml,newlove3ml;
    private ImageButton back,save;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentuser = auth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newlove);
        back = findViewById(R.id.nLback);
        save = findViewById(R.id.newsave);


        newlovename = findViewById(R.id.newlovename);
        newlove1 = findViewById(R.id.newlove1);
        newlove2 = findViewById(R.id.newlove2);
        newlove3 = findViewById(R.id.newlove3);
        newlove1ml = findViewById(R.id.newlove1ml);
        newlove2ml = findViewById(R.id.newlove2ml);
        newlove3ml = findViewById(R.id.newlove3ml);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(newlove.this,lovedrink.class);
                startActivity(intent);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savelove();
            }
        });
    }


    private void savelove() {
        String lovename = newlovename.getText().toString();
        String lovedrink1 = newlove1.getText().toString();
        String lovedrink2 = newlove2.getText().toString();
        String lovedrink3 = newlove3.getText().toString();
        String lovedrink1ml = newlove1ml.getText().toString();
        String lovedrink2ml = newlove2ml.getText().toString();
        String lovedrink3ml = newlove3ml.getText().toString();

        CollectionReference love = FirebaseFirestore.getInstance()
                .collection("love:" + currentuser.getEmail());
        love.add(new loveuser(lovename, lovedrink1, lovedrink2, lovedrink3, lovedrink1ml, lovedrink2ml, lovedrink3ml));

        startActivity(new Intent(newlove.this,lovedrink.class));
    }
}