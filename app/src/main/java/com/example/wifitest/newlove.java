package com.example.wifitest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class newlove extends AppCompatActivity {
    private EditText newlovename,newlove1,newlove2,newlove3,newlove4,newlove5;
    private SeekBar newlove1ml,newlove2ml,newlove3ml,newlove4ml,newlove5ml;
    private TextView newml1,newml2,newml3,newml4,newml5;

    private ImageButton back,save;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentuser = auth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newlove);
        back = findViewById(R.id.nLback);
        save = findViewById(R.id.newsave);


        newlovename = findViewById(R.id.newlovename);
        newlove1 = findViewById(R.id.newlove1);
        newlove2 = findViewById(R.id.newlove2);
        newlove3 = findViewById(R.id.newlove3);
        //newlove4 = findViewById(R.id.newlove4);
        //newlove5 = findViewById(R.id.newlove5);
        newlove1ml = findViewById(R.id.newlove1ml);
        newlove2ml = findViewById(R.id.newlove2ml);
        newlove3ml = findViewById(R.id.newlove3ml);
        //newlove4ml = findViewById(R.id.newlove4ml);
        //newlove5ml = findViewById(R.id.newlove5ml);
        newml1 = findViewById(R.id.newText1);
        newml2 = findViewById(R.id.newText2);
        newml3 = findViewById(R.id.newText3);
        //newml4 = findViewById(R.id.newText4);
        //newml5 = findViewById(R.id.newText5);

        newlove1ml.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress / 10;
                progress = progress * 10;
                newml1.setText("飲料1:"+String.valueOf(progress)+"ml");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        newlove2ml.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress / 10;
                progress = progress * 10;
                newml2.setText("飲料2:"+String.valueOf(progress)+"ml");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        newlove3ml.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress / 10;
                progress = progress * 10;
                newml3.setText("飲料3:"+String.valueOf(progress)+"ml");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        /*newlove4ml.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress / 10;
                progress = progress * 10;
                newml4.setText("飲料4:"+String.valueOf(progress)+"ml");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        newlove5ml.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress / 10;
                progress = progress * 10;
                newml5.setText("飲料5:"+String.valueOf(progress)+"ml");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });*/

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(newlove.this,lovedrink.class);
                startActivity(intent);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savelove();
            }
        });
    }


    private void savelove() {
        String lovename = newlovename.getText().toString();
        String lovedrink1 = newlove1.getText().toString();
        String lovedrink2 = newlove2.getText().toString();
        String lovedrink3 = newlove3.getText().toString();
        //String lovedrink4 = newlove4.getText().toString();
        //String lovedrink5 = newlove5.getText().toString();
        String lovedrink1ml = String.valueOf(newlove1ml.getProgress()/10*10);
        String lovedrink2ml = String.valueOf(newlove2ml.getProgress()/10*10);
        String lovedrink3ml = String.valueOf(newlove3ml.getProgress()/10*10);
        //String lovedrink4ml = String.valueOf(newlove4ml.getProgress()/10*10);
        //String lovedrink5ml = String.valueOf(newlove5ml.getProgress()/10*10);

        CollectionReference love = FirebaseFirestore.getInstance()
                .collection("love:" + currentuser.getEmail());
        love.add(new loveuser(lovename, lovedrink1, lovedrink2, lovedrink3,/*lovedrink4 ,lovedrink5,*/ lovedrink1ml, lovedrink2ml, lovedrink3ml/*,lovedrink4ml,lovedrink5ml*/));

        startActivity(new Intent(newlove.this,lovedrink.class));
    }
}