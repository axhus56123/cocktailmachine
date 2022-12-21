package com.example.wifitest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class addRecommend extends AppCompatActivity {
    private EditText newrename,newre1,newre2,newre3,newre4,newre5,newre6,about;
    private SeekBar newre1ml,newre2ml,newre3ml,newre4ml,newre5ml,newre6ml;
    private TextView newml1,newml2,newml3,newml4,newml5,newml6;
    private ImageView mPostImage;
    private StorageReference storageReference;
    private FirebaseFirestore firestore;
    private ImageView mReImage;
    private Uri postImageUri = null;

    private ImageButton back,save;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentuser = auth.getCurrentUser();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recommend);

        int storge;
        storge= (int)(Math.random()*6000)+1;
        String storgeCount = String.valueOf(storge);

        back = findViewById(R.id.addreBack);
        save = findViewById(R.id.newsave);
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        newrename = findViewById(R.id.newrename);
        newre1 = findViewById(R.id.newre1);
        newre2 = findViewById(R.id.newre2);
        newre3 = findViewById(R.id.newre3);
        newre4 = findViewById(R.id.newre4);
        newre5 = findViewById(R.id.newre5);
        newre6 = findViewById(R.id.newre6);
        about = findViewById(R.id.about);
        newre1ml = findViewById(R.id.newre1ml);
        newre2ml = findViewById(R.id.newre2ml);
        newre3ml = findViewById(R.id.newre3ml);
        newre4ml = findViewById(R.id.newre4ml);
        newre5ml = findViewById(R.id.newre5ml);
        newre6ml = findViewById(R.id.newre6ml);
        newml1 = findViewById(R.id.newText1);
        newml2 = findViewById(R.id.newText2);
        newml3 = findViewById(R.id.newText3);
        newml4 = findViewById(R.id.newText4);
        newml5 = findViewById(R.id.newText5);
        newml6 = findViewById(R.id.newText6);

        mPostImage = findViewById(R.id.post_image);


        newre1ml.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

        newre2ml.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

        newre3ml.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        newre4ml.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        newre5ml.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        });
        newre6ml.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress / 10;
                progress = progress * 10;
                newml6.setText("飲料6:"+String.valueOf(progress)+"ml");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(addRecommend.this,recommend.class);
                startActivity(intent);
            }
        });

        mPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(3,2)
                        .setMinCropResultSize(512,512)
                        .start(addRecommend.this);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lovename = newrename.getText().toString();
                String lovedrink1 = newre1.getText().toString();
                String lovedrink2 = newre2.getText().toString();
                String lovedrink3 = newre3.getText().toString();
                String lovedrink4 = newre4.getText().toString();
                String lovedrink5 = newre5.getText().toString();
                String lovedrink6 = newre6.getText().toString();
                String reabout = about.getText().toString();
                String lovedrink1ml = String.valueOf(newre1ml.getProgress()/10*10);
                String lovedrink2ml = String.valueOf(newre2ml.getProgress()/10*10);
                String lovedrink3ml = String.valueOf(newre3ml.getProgress()/10*10);
                String lovedrink4ml = String.valueOf(newre4ml.getProgress()/10*10);
                String lovedrink5ml = String.valueOf(newre5ml.getProgress()/10*10);
                String lovedrink6ml = String.valueOf(newre6ml.getProgress()/10*10);
                HashMap<String, Object> reMap = new HashMap<>();

                reMap.put("Rename", lovename);
                reMap.put("About",reabout);
                reMap.put("Redrink1", lovedrink1);
                reMap.put("Redrink1ml", lovedrink1ml);
                reMap.put("Redrink2", lovedrink2);
                reMap.put("Redrink2ml", lovedrink2ml);
                reMap.put("Redrink3", lovedrink3);
                reMap.put("Redrink3ml", lovedrink3ml);
                reMap.put("Redrink4", lovedrink4);
                reMap.put("Redrink4ml", lovedrink4ml);
                reMap.put("Redrink5", lovedrink5);
                reMap.put("Redrink5ml", lovedrink5ml);
                reMap.put("Redrink6", lovedrink6);
                reMap.put("Redrink6ml", lovedrink6ml);

                StorageReference postRef = storageReference.child(storgeCount).child(FieldValue.serverTimestamp().toString() + ".jpg");
                postRef.putFile(postImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            postRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    reMap.put("Image", uri.toString());
                                    reMap.put("time", FieldValue.serverTimestamp());

                                    firestore.collection("Recommend").add(reMap);
                                    startActivity(new Intent(addRecommend.this,recommend.class));
                                }
                            });

                        }
                    }
                });


                //firestore.collection("Recommend").add(reMap);
                //startActivity(new Intent(addRecommend.this,recommend.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){

                postImageUri = result.getUri();
                mPostImage.setImageURI(postImageUri);
            }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Toast.makeText(this, result.getError().toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}