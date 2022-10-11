package com.example.wifitest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Manager extends AppCompatActivity {

    NumberPicker drinkPicker;
    EditText drinkname1,drinkname2,drinkname3,drinkname4,drinkname5,drinkname6;
    Button managerSend,managerSet;
    private FirebaseDatabase Db = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        drinkPicker = findViewById(R.id.drinkpicker);
        drinkPicker.setMaxValue(6);
        drinkPicker.setMinValue(1);
        drinkname1 = findViewById(R.id.managerDrinkName1);
        drinkname2 = findViewById(R.id.managerDrinkName2);
        drinkname3 = findViewById(R.id.managerDrinkName3);
        drinkname4 = findViewById(R.id.managerDrinkName4);
        drinkname5 = findViewById(R.id.managerDrinkName5);
        drinkname6 = findViewById(R.id.managerDrinkName6);
        drinkname1.setVisibility(View.GONE);
        drinkname2.setVisibility(View.GONE);
        drinkname3.setVisibility(View.GONE);
        drinkname4.setVisibility(View.GONE);
        drinkname5.setVisibility(View.GONE);
        drinkname6.setVisibility(View.GONE);
        managerSend = findViewById(R.id.managerSend);
        managerSet = findViewById(R.id.managerSet);

        managerSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int drinkCounter = drinkPicker.getValue();
                switch (drinkCounter){
                    case 1:
                        drinkname1.setVisibility(View.VISIBLE);
                        drinkname2.setVisibility(View.GONE);
                        drinkname3.setVisibility(View.GONE);
                        drinkname4.setVisibility(View.GONE);
                        drinkname5.setVisibility(View.GONE);
                        drinkname6.setVisibility(View.GONE);
                        break;
                    case 2:
                        drinkname1.setVisibility(View.VISIBLE);
                        drinkname2.setVisibility(View.VISIBLE);
                        drinkname3.setVisibility(View.GONE);
                        drinkname4.setVisibility(View.GONE);
                        drinkname5.setVisibility(View.GONE);
                        drinkname6.setVisibility(View.GONE);
                        break;
                    case 3:
                        drinkname1.setVisibility(View.VISIBLE);
                        drinkname2.setVisibility(View.VISIBLE);
                        drinkname3.setVisibility(View.VISIBLE);
                        drinkname4.setVisibility(View.GONE);
                        drinkname5.setVisibility(View.GONE);
                        drinkname6.setVisibility(View.GONE);
                        break;
                    case 4:
                        drinkname1.setVisibility(View.VISIBLE);
                        drinkname2.setVisibility(View.VISIBLE);
                        drinkname3.setVisibility(View.VISIBLE);
                        drinkname4.setVisibility(View.VISIBLE);
                        drinkname5.setVisibility(View.GONE);
                        drinkname6.setVisibility(View.GONE);
                        break;
                    case 5:
                        drinkname1.setVisibility(View.VISIBLE);
                        drinkname2.setVisibility(View.VISIBLE);
                        drinkname3.setVisibility(View.VISIBLE);
                        drinkname4.setVisibility(View.VISIBLE);
                        drinkname5.setVisibility(View.VISIBLE);
                        drinkname6.setVisibility(View.GONE);
                        break;
                    case 6:
                        drinkname1.setVisibility(View.VISIBLE);
                        drinkname2.setVisibility(View.VISIBLE);
                        drinkname3.setVisibility(View.VISIBLE);
                        drinkname4.setVisibility(View.VISIBLE);
                        drinkname5.setVisibility(View.VISIBLE);
                        drinkname6.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        managerSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference firebaseManager = Db.getReference("ESP32");
                int drinkCounter = drinkPicker.getValue();

                switch (drinkCounter){
                    case 1:
                        firebaseManager.removeValue();
                        HashMap<String,Object> manager1 = new HashMap<>();
                        manager1.put("drink_1",drinkname1.getText().toString());

                        firebaseManager.setValue(manager1);
                        break;
                    case 2:
                        firebaseManager.removeValue();
                        HashMap<String,Object> manager2 = new HashMap<>();
                        manager2.put("drink_1",drinkname1.getText().toString());
                        manager2.put("drink_2",drinkname2.getText().toString());

                        firebaseManager.setValue(manager2);
                        break;
                    case 3:
                        firebaseManager.removeValue();
                        HashMap<String,Object> manager3 = new HashMap<>();
                        manager3.put("drink_1",drinkname1.getText().toString());
                        manager3.put("drink_2",drinkname2.getText().toString());
                        manager3.put("drink_3",drinkname3.getText().toString());

                        firebaseManager.setValue(manager3);
                        break;
                    case 4:
                        firebaseManager.removeValue();
                        HashMap<String,Object> manager4 = new HashMap<>();
                        manager4.put("drink_1",drinkname1.getText().toString());
                        manager4.put("drink_2",drinkname2.getText().toString());
                        manager4.put("drink_3",drinkname3.getText().toString());
                        manager4.put("drink_4",drinkname4.getText().toString());

                        firebaseManager.setValue(manager4);
                        break;
                    case 5:
                        firebaseManager.removeValue();
                        HashMap<String,Object> manager5 = new HashMap<>();
                        manager5.put("drink_1",drinkname1.getText().toString());
                        manager5.put("drink_2",drinkname2.getText().toString());
                        manager5.put("drink_3",drinkname3.getText().toString());
                        manager5.put("drink_4",drinkname4.getText().toString());
                        manager5.put("drink_5",drinkname5.getText().toString());

                        firebaseManager.setValue(manager5);
                        break;
                    case 6:
                        firebaseManager.removeValue();
                        HashMap<String,Object> manager6 = new HashMap<>();
                        manager6.put("drink_1",drinkname1.getText().toString());
                        manager6.put("drink_2",drinkname2.getText().toString());
                        manager6.put("drink_3",drinkname3.getText().toString());
                        manager6.put("drink_4",drinkname4.getText().toString());
                        manager6.put("drink_5",drinkname5.getText().toString());
                        manager6.put("drink_6",drinkname6.getText().toString());

                        firebaseManager.setValue(manager6);
                        break;
                }
            }
        });


    }
}