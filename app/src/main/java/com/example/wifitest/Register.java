package com.example.wifitest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    Activity context = this;
    FirebaseAuth mAuth;
    EditText etacc,etpass;
    TextView tv4,login;
    Button sure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etacc = findViewById(R.id.etRegisterEmail);
        etpass = findViewById(R.id.etRegisterpass);
        tv4 = findViewById(R.id.tv8);
        sure = findViewById(R.id.btnlogout);
        login = findViewById(R.id.textViewlogin);
        mAuth = FirebaseAuth.getInstance();

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.createUserWithEmailAndPassword(etacc.getText().toString(),etpass.getText().toString()).addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user=mAuth.getCurrentUser();
                            tv4.setText("結果:"+user.getEmail()+"註冊成功");
                        }else{
                            tv4.setText("結果:註冊失敗"+task.getException());
                        }
                    }
                });


            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToLogin();
            }
        });

    }
    private void switchToLogin(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
}