package com.example.wifitest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class drink extends AppCompatActivity {



    private Context context = this;
    private ListView lv;
    private Button send ,btnconnect;
    private SeekBar drinkinput1,drinkinput2,drinkinput3;
    private TextView ml1,ml2,ml3;
    private EditText ip;
    private InputStream inputStream;
    private OutputStream outputStream;
    public Socket socket;
    private FirebaseFirestore db;
    private String x_last="0";
    private String x_select="0";
    private Thread thread;                //執行緒

    private BufferedWriter bw;            //取得網路輸出串流
    private BufferedReader br;            //取得網路輸入串流
    private String tmp;   //做為接收時的緩存

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentuser = auth.getCurrentUser();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        send = findViewById(R.id.send);
        btnconnect = findViewById(R.id.btnconnect);
        drinkinput1 = findViewById(R.id.drinkinput1);
        drinkinput2 = findViewById(R.id.drinkinput2);
        drinkinput3 = findViewById(R.id.drinkinput3);
        ml1 = findViewById(R.id.drinkml1);
        ml2 = findViewById(R.id.drinkml2);
        ml3 = findViewById(R.id.drinkml3);
        ip = findViewById(R.id.ip);


        db=FirebaseFirestore.getInstance();

        drinkinput1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ml1.setText("飲料1:"+String.valueOf(progress)+"ml");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        drinkinput2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ml2.setText("飲料2:"+String.valueOf(progress)+"ml");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        drinkinput3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ml3.setText("飲料3:"+String.valueOf(progress)+"ml");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



       btnconnect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                actconnect();
            }
       });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actsend();
                actsend2();
            }
        });


    }

    private void actconnect() {

        Thread mThread = new Thread(connect);
        mThread.start();
    }

    private Runnable connect = new Runnable ()
    {

        public void run (){
            try {
                socket = new Socket("192.168.2.187" ,1234);
                bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            }catch (IOException e){
                e.printStackTrace();
            }
        }



    } ;

    private  void actsend() {


        Map<String,Object> order_data= new HashMap<>();
        order_data.put("Userid",currentuser.getEmail());
        order_data.put("fin","0");
        order_data.put("t1",drinkinput1.getProgress());
        order_data.put("t2",drinkinput2.getProgress());
        order_data.put("t3",drinkinput3.getProgress());





        db.collection("Order").document(currentuser.getEmail()).set(order_data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("結果: ","新增成功");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("結果: ","新增失敗");
                    }
                });







        /*if (outputStream == null) return;
        Thread mThread = new Thread(trans);
        mThread.start();*/

    }
    private  void actsend2(){
        Map<String,Object> history_data= new HashMap<>();
        history_data.put("t1",drinkinput1.getProgress());
        history_data.put("t2",drinkinput2.getProgress());
        history_data.put("t3",drinkinput3.getProgress());

        Integer x_id=Integer.valueOf(x_last)+1;

        if(x_id>5){
            x_id=1;
        }

        db.collection("history:"+currentuser.getEmail()).document(String.valueOf(x_id)).set(history_data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("結果: ","新增成功");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("結果: ","新增失敗");
                    }
                });
        x_last=x_id.toString();
    }



   /* private Runnable trans = new Runnable (){

        public void run (){
            try {




                bw.write(drinkinput1.getText().toString());
                bw.flush();
                bw.write(drinkinput2.getText().toString());
                bw.flush();
                bw.write(drinkinput3.getText().toString());
                bw.flush();


                input1textView.setText("飲料1設定為 "+drinkinput1.getText()+" ml");
                input2textView.setText("飲料2設定為 "+drinkinput2.getText()+" ml");
                input3textView.setText("飲料3設定為 "+drinkinput3.getText()+" ml");

                drinkinput1.setText("");
                drinkinput2.setText("");
                drinkinput3.setText("");

            }catch (IOException e){
                e.printStackTrace();
            }
        }



    } ;*/




//Toast.makeText(drink.this,"連接成功",Toast.LENGTH_SHORT).show();








   /* public void send() {
        try{


            bw.write(drinkinput1.getText().toString()+"\n");
            bw.flush();
            bw.write(drinkinput2.getText().toString());
            bw.flush();
            bw.write(drinkinput3.getText().toString());
            bw.flush();


            input1textView.setText("飲料1設定為 "+drinkinput1.getText()+" ml");
            input2textView.setText("飲料2設定為 "+drinkinput2.getText()+" ml");
            input3textView.setText("飲料3設定為 "+drinkinput3.getText()+" ml");

            drinkinput1.setText("");
            drinkinput2.setText("");
            drinkinput3.setText("");
        }catch (IOException e){
            Toast.makeText(drink.this,"傳送失敗",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }




    }*/





}