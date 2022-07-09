package com.example.wifitest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

public class Login extends AppCompatActivity {
    Activity context = this;

    Button sure ,cancel ,login ;
    TextView tv4,tv8;
    EditText etacc, etpass,loacc,lopass;
    String email;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sure = findViewById(R.id.sure);
        cancel = findViewById(R.id.cancel);
        login = findViewById(R.id.btnlogin);


        etacc = findViewById(R.id.etacc);
        etpass = findViewById(R.id.etpass);
        loacc = findViewById(R.id.logacc);
        lopass = findViewById(R.id.logpass);


        tv4 = findViewById(R.id.tv4);
        tv8 = findViewById(R.id.tv8);

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

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etacc.setText(null);
                etpass.setText(null);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signInWithEmailAndPassword(loacc.getText().toString(),lopass.getText().toString()).addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user= mAuth.getCurrentUser();
                            tv8.setText(user.getEmail()+"登入成功");
                            email = user.getEmail();
                        }else{
                            tv8.setText("登入失敗"+task.getException());
                        }
                    }
                });
            }
        });

    }
}