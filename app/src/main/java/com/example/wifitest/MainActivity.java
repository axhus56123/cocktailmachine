package com.example.wifitest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.io.IOException;

public class MainActivity<override> extends AppCompatActivity {

    private Button button01;

    private FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentuser = auth.getCurrentUser();
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.bottomHome);
        button01 = findViewById(R.id.button01);

        if(currentuser == null){
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
            return;
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.bottomHome:
                        return true;
                    case R.id.bottomOptions:
                        startActivity(new Intent(new Intent(getApplicationContext(),bottomOption.class)));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });

        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentuser == null){
                    notlogin();
                    Intent loginActivityIntent = new Intent(MainActivity.this, Login.class);
                    startActivity(loginActivityIntent);
                    return;
                }
                Intent main2ActivityIntent = new Intent(MainActivity.this, drink.class);
                startActivity(main2ActivityIntent);
            }
        });
    }

    private void logoutuser(){
        FirebaseAuth.getInstance().signOut();
        Toast toast = Toast.makeText(this, "登出成功", Toast.LENGTH_SHORT);
        toast.show();
        finish();
    }
    private void notlogin(){
        Toast toast = Toast.makeText(this, "未登入", Toast.LENGTH_SHORT);
        toast.show();
    }


    //右上選單








    //操控調飲



}