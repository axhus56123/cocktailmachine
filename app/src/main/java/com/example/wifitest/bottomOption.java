package com.example.wifitest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class bottomOption extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Button btnuser,btnHistory,btnFavorite,btnManager,btnFriend;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_option);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.bottomOptions);
        btnuser = findViewById(R.id.btnuser);
        btnFavorite = findViewById(R.id.btnlove);
        btnHistory = findViewById(R.id.btnhistory);
        btnManager = findViewById(R.id.manager);
        btnFriend = findViewById(R.id.btnfriend);
        btnManager.setVisibility(View.GONE);
        String email = currentUser.getEmail();

        if(email.equals("system@gmail.com")){
            btnManager.setVisibility(View.VISIBLE);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.bottomHome:
                        startActivity(new Intent(new Intent(getApplicationContext(),MainActivity.class)));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.bottomOptions:
                        return true;
                }

                return false;
            }
        });

        btnuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(bottomOption.this, Account.class);
                startActivity(intent);
            }
        });
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(bottomOption.this, lovedrink.class);
                startActivity(intent);
            }
        });
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(bottomOption.this, history.class);
                startActivity(intent);
            }
        });
        btnManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(bottomOption.this, Manager.class);
                startActivity(intent);
            }
        });
        btnFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(bottomOption.this, friendList.class);
                startActivity(intent);
            }
        });
    }
}