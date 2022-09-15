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



    private Button button01,layout;
    private TextView userid;
    private FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentuser = auth.getCurrentUser();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button01 = findViewById(R.id.button01);

        userid = findViewById(R.id.userid);

        Intent intent = getIntent();
        String H1 =intent.getStringExtra("email");
        TextView userid =findViewById(R.id.userid);

        if(H1 != null) {
            userid.setText("User:"+H1);
        }
        else{
            userid.setText("未登入");
        }





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
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.mainlogin,menu);
        return super.onCreateOptionsMenu(menu);
    }


    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.login:
                Intent main2ActivityIntent = new Intent(MainActivity.this, Login.class);
                startActivity(main2ActivityIntent);
                break;
            case R.id.favorite:
                if(currentuser == null){
                    notlogin();
                    Intent loginActivityIntent = new Intent(MainActivity.this, Login.class);
                    startActivity(loginActivityIntent);
                    return true;
                }
                else{
                    Intent main3ActivityIntent = new Intent(MainActivity.this, lovedrink.class);
                    startActivity(main3ActivityIntent);
                    return true;

                }

            case R.id.historydrink:
                if(currentuser == null){
                    notlogin();
                    Intent loginActivityIntent = new Intent(MainActivity.this, Login.class);
                    startActivity(loginActivityIntent);
                    return true;
                }
                else{
                    Intent main4ActivityIntent = new Intent(MainActivity.this, history.class);
                    startActivity(main4ActivityIntent);
                    return true;
                }

            default:

        }
        return true;
    }




    //操控調飲



}