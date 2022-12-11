package com.example.wifitest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Capacity extends AppCompatActivity {

    ProgressBar capacity1,capacity2,capacity3,capacity4,capacity5,capacity6;
    TextView ml1,ml2,ml3,ml4,ml5,ml6;
    EditText ed1,ed2,ed3,ed4,ed5,ed6;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentuser = auth.getCurrentUser();
    ImageView back;
    Button finish;
    private FirebaseDatabase Db = FirebaseDatabase.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capacity);

        back = findViewById(R.id.capacityBack);
        capacity1 = findViewById(R.id.capacity1);
        capacity2 = findViewById(R.id.capacity2);
        capacity3 = findViewById(R.id.capacity3);
        capacity4 = findViewById(R.id.capacity4);
        capacity5 = findViewById(R.id.capacity5);
        capacity6 = findViewById(R.id.capacity6);
        ml1 = findViewById(R.id.ml1);
        ml2 = findViewById(R.id.ml2);
        ml3 = findViewById(R.id.ml3);
        ml4 = findViewById(R.id.ml4);
        ml5 = findViewById(R.id.ml5);
        ml6 = findViewById(R.id.ml6);
        ed1 = findViewById(R.id.Replenish1);
        ed2 = findViewById(R.id.Replenish2);
        ed3 = findViewById(R.id.Replenish3);
        ed4 = findViewById(R.id.Replenish4);
        ed5 = findViewById(R.id.Replenish5);
        ed6 = findViewById(R.id.Replenish6);
        finish = findViewById(R.id.ReplenishOk);
        finish.setVisibility(View.GONE);
        ed1.setVisibility(View.GONE);
        ed2.setVisibility(View.GONE);
        ed3.setVisibility(View.GONE);
        ed4.setVisibility(View.GONE);
        ed5.setVisibility(View.GONE);
        ed6.setVisibility(View.GONE);

        String email = currentuser.getEmail();

        if(email.equals("system@gmail.com")){
            finish.setVisibility(View.VISIBLE);
            ed1.setVisibility(View.VISIBLE);
            ed2.setVisibility(View.VISIBLE);
            ed3.setVisibility(View.VISIBLE);
            ed4.setVisibility(View.VISIBLE);
            ed5.setVisibility(View.VISIBLE);
            ed6.setVisibility(View.VISIBLE);
        }

        capacity1.setMax(750);
        capacity2.setMax(750);
        capacity3.setMax(750);
        capacity4.setMax(750);
        capacity5.setMax(750);
        capacity6.setMax(750);
        countml();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Capacity.this,bottomOption.class);
                startActivity(intent);
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference capacity = Db.getReference("capacity");
                capacity.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        DataSnapshot dataSnapshot = task.getResult();
                        String ml1 = ed1.getText().toString();
                        String ml2 = ed2.getText().toString();
                        String ml3 = ed3.getText().toString();
                        String ml4 = ed4.getText().toString();
                        String ml5 = ed5.getText().toString();
                        String ml6 = ed6.getText().toString();
                        HashMap<String,Object> ml = new HashMap<>();
                        if(!ed1.getText().toString().matches("")){
                            int intml1 = Integer.valueOf(ml1);
                            ml.put("drink1",intml1);
                        }
                        else{
                            long notfix1 = (long) dataSnapshot.child("drink1").getValue();
                            ml.put("drink1",notfix1);
                        }
                        if(!ed2.getText().toString().matches("")){
                            int intml2 = Integer.valueOf(ml2);
                            ml.put("drink2",intml2);
                        }
                        else{
                            long notfix2 = (long) dataSnapshot.child("drink2").getValue();
                            ml.put("drink2",notfix2);
                        }
                        if(!ed3.getText().toString().matches("")){
                            int intml3 = Integer.valueOf(ml3);
                            ml.put("drink3",intml3);
                        }
                        else{
                            long notfix3 = (long) dataSnapshot.child("drink3").getValue();
                            ml.put("drink3",notfix3);
                        }
                        if(!ed4.getText().toString().matches("")){
                            int intml4 = Integer.valueOf(ml4);
                            ml.put("drink4",intml4);
                        }
                        else{
                            long notfix4 = (long) dataSnapshot.child("drink4").getValue();
                            ml.put("drink4",notfix4);
                        }
                        if(!ed5.getText().toString().matches("")){
                            int intml5 = Integer.valueOf(ml5);
                            ml.put("drink5",intml5);
                        }
                        else{
                            long notfix5 = (long) dataSnapshot.child("drink5").getValue();
                            ml.put("drink5",notfix5);
                        }
                        if(!ed6.getText().toString().matches("")){
                            int intml6 = Integer.valueOf(ml6);
                            ml.put("drink6",intml6);
                        }
                        else{
                            long notfix6 = (long) dataSnapshot.child("drink6").getValue();
                            ml.put("drink6",notfix6);
                        }
                        capacity.setValue(ml);
                        countml();
                    }
                });



            }
        });

    }
    private void countml(){
        DatabaseReference capacity = Db.getReference("capacity");
        String Useruid = currentuser.getUid();
        capacity.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult();
                long usedml1 = (long) dataSnapshot.child("drink1").getValue();
                long usedml2 = (long) dataSnapshot.child("drink2").getValue();
                long usedml3 = (long) dataSnapshot.child("drink3").getValue();
                long usedml4 = (long) dataSnapshot.child("drink4").getValue();
                long usedml5 = (long) dataSnapshot.child("drink5").getValue();
                long usedml6 = (long) dataSnapshot.child("drink6").getValue();
                capacity1.setProgress((int) usedml1);
                capacity2.setProgress((int) usedml2);
                capacity3.setProgress((int) usedml3);
                capacity4.setProgress((int) usedml4);
                capacity5.setProgress((int) usedml5);
                capacity6.setProgress((int) usedml6);
                ml1.setText(Integer.toString((int) usedml1));
                ml2.setText(Integer.toString((int) usedml2));
                ml3.setText(Integer.toString((int) usedml3));
                ml4.setText(Integer.toString((int) usedml4));
                ml5.setText(Integer.toString((int) usedml5));
                ml6.setText(Integer.toString((int) usedml6));
            }
        });

    }
}