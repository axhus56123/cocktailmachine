package com.example.wifitest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class bottomOption extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Button btnuser,btnHistory,btnFavorite,btnManager,btnFriend,btnblog,btnCapacity;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
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
        btnblog = findViewById(R.id.btnBlog);
        btnCapacity = findViewById(R.id.btncapacity);
        if(currentUser == null){
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
            return;
        }
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
                if(currentUser == null){
                    notlogin();
                    Intent loginActivityIntent = new Intent(bottomOption.this, Login.class);
                    startActivity(loginActivityIntent);
                    return;
                }
                Intent intent = new Intent(bottomOption.this, Account.class);
                startActivity(intent);
            }
        });
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUser == null){
                    notlogin();
                    Intent loginActivityIntent = new Intent(bottomOption.this, Login.class);
                    startActivity(loginActivityIntent);
                    return;
                }
                Intent intent = new Intent(bottomOption.this, lovedrink.class);
                startActivity(intent);
            }
        });
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUser == null){
                    notlogin();
                    Intent loginActivityIntent = new Intent(bottomOption.this, Login.class);
                    startActivity(loginActivityIntent);
                    return;
                }
                Intent intent = new Intent(bottomOption.this, history.class);
                startActivity(intent);
            }
        });
        btnManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUser == null){
                    notlogin();
                    Intent loginActivityIntent = new Intent(bottomOption.this, Login.class);
                    startActivity(loginActivityIntent);
                    return;
                }
                Intent intent = new Intent(bottomOption.this, Manager.class);
                startActivity(intent);
            }
        });
        btnFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(networkInfo==null||!networkInfo.isConnected()){
                    notwifi();
                    return;
                }
                if(currentUser == null){
                    notlogin();
                    Intent loginActivityIntent = new Intent(bottomOption.this, Login.class);
                    startActivity(loginActivityIntent);
                    return;
                }
                Intent intent = new Intent(bottomOption.this, friendList.class);
                startActivity(intent);
            }
        });
        btnblog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(networkInfo==null||!networkInfo.isConnected()){
                    notwifi();
                    return;
                }
                if(currentUser == null){
                    notlogin();
                    Intent loginActivityIntent = new Intent(bottomOption.this, Login.class);
                    startActivity(loginActivityIntent);
                    return;
                }
                Intent intent = new Intent(bottomOption.this, Blog.class);
                startActivity(intent);
            }
        });
        btnCapacity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(networkInfo==null||!networkInfo.isConnected()){
                    notwifi();
                    return;
                }
                if(currentUser == null){
                    notlogin();
                    Intent loginActivityIntent = new Intent(bottomOption.this, Login.class);
                    startActivity(loginActivityIntent);
                    return;
                }
                Intent intent = new Intent(bottomOption.this, Capacity.class);
                startActivity(intent);
            }
        });
    }
    private void notwifi(){
        Toast toast = Toast.makeText(this, "未連接網路", Toast.LENGTH_SHORT);
        toast.show();
    }
    private void notlogin(){
        Toast toast = Toast.makeText(this, "未登入", Toast.LENGTH_SHORT);
        toast.show();
    }
}