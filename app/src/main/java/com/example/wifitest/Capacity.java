package com.example.wifitest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class Capacity extends AppCompatActivity {

    ProgressBar capacity1,capacity2,capacity3,capacity4,capacity5,capacity6;
    TextView ml1,ml2,ml3,ml4,ml5,ml6;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentuser = auth.getCurrentUser();
    private FirebaseDatabase Db = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capacity);

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

        capacity1.setMax(750);
        capacity2.setMax(750);
        capacity3.setMax(750);
        capacity4.setMax(750);
        capacity5.setMax(750);
        capacity6.setMax(750);
        countml();

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