package com.example.wifitest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    Activity context = this;

    Button login ;
    TextView tv8,create;
    EditText loacc,lopass;
    String email,password;

    FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.btnlogout);
        loacc = findViewById(R.id.etRegisterEmail);
        lopass = findViewById(R.id.etRegisterpass);
        create = findViewById(R.id.textViewlogin);
        tv8 = findViewById(R.id.tv8);




        mAuth = FirebaseAuth.getInstance();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = loacc.getText().toString();
                password = lopass.getText().toString();
                if(email.isEmpty()||password.isEmpty()){
                    outNull();
                }
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent();
                            intent.setClass(Login.this,MainActivity.class);
                            startActivity(intent);
                        }else{
                            tv8.setText("登入失敗"+task.getException());
                        }
                    }
                });
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToRegister();
            }
        });

    }
    private void switchToRegister(){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
        finish();
    }
    private void outNull(){
        Toast toast = Toast.makeText(this, "請輸入資訊", Toast.LENGTH_SHORT);
        toast.show();
        return;
    }

}


