package com.example.wifitest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Random;

public class Register extends AppCompatActivity {

    Activity context = this;
    FirebaseAuth auth;
    FirebaseFirestore db;
    EditText etacc,etpass,etname;
    TextView tv8,login;
    Button sure;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etacc = findViewById(R.id.etRegisterEmail);
        etpass = findViewById(R.id.etRegisterpass);
        tv8 = findViewById(R.id.tv8);
        sure = findViewById(R.id.btnlogout);
        login = findViewById(R.id.textViewlogin);
        auth = FirebaseAuth.getInstance();
        etname = findViewById(R.id.etRegistername);

        db = FirebaseFirestore.getInstance();



        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etacc.getText().toString().trim();
                String password = etpass.getText().toString().trim();
                String name = etname.getText().toString().trim();
                if( TextUtils.isEmpty(email)||TextUtils.isEmpty(password)||TextUtils.isEmpty(name)){
                    tv8.setText("請輸入數值");
                    return;
                }

                auth.createUserWithEmailAndPassword(etacc.getText().toString(),etpass.getText().toString()).addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user=auth.getCurrentUser();
                            String uid = user.getUid();
                            reference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                            HashMap<String,String> hashMap = new HashMap<>();
                            hashMap.put("id",uid);
                            hashMap.put("username",name);
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });

                            HashMap<String,Object> datauser = new HashMap<>();
                            datauser.put("name",name);
                            datauser.put("id",getRandom(6));

                            db.collection("user").document(uid).set(datauser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });


                            tv8.setText("結果:"+user.getEmail()+"註冊成功");
                        }else{
                            tv8.setText("結果:註冊失敗"+task.getException());
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
    private String getRandom(int i){
        String characters = "abcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder result = new StringBuilder();
        while (i>0){
            Random random = new Random();
            result.append(characters.charAt(random.nextInt(characters.length())));
            i--;
        }
        return  result.toString();
    }
}