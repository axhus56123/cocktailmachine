package com.example.wifitest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class drink extends AppCompatActivity {

    Thread Thread1 = null;
    private Context context = this;
    private ImageButton back;
    private ListView lv;
    private Button send ,btnconnect,disconnect;
    private SeekBar drinkinput1,drinkinput2,drinkinput3;
    private TextView ml1,ml2,ml3,tvMessages;
    private EditText etip,etport;
    private PrintWriter output;
    private BufferedReader input;
    public Socket socket;
    private FirebaseFirestore db;
    private String x_last="0";
    private String x_select="0";
    String SERVER_IP;
    int SERVER_PORT;
    long fireBaseCounter=0,a;
    private Thread thread;                //執行緒
    private BufferedWriter bw;            //取得網路輸出串流
    private BufferedReader br;            //取得網路輸入串流
    private String tmp;   //做為接收時的緩存
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentuser = auth.getCurrentUser();
    private FirebaseDatabase Db = FirebaseDatabase.getInstance();
    DatabaseReference root = Db.getReference("test");
    DatabaseReference count = Db.getReference("count");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        setTitle("調飲");

        send = findViewById(R.id.send);

        btnconnect = findViewById(R.id.btnconnect);
        disconnect = findViewById(R.id.btndisconnect);
        drinkinput1 = findViewById(R.id.drinkinput1);
        drinkinput2 = findViewById(R.id.drinkinput2);
        drinkinput3 = findViewById(R.id.drinkinput3);
        tvMessages = findViewById(R.id.tvMessages);
        ml1 = findViewById(R.id.drinkml1);
        ml2 = findViewById(R.id.drinkml2);
        ml3 = findViewById(R.id.drinkml3);
        etip = findViewById(R.id.ip);
        etport = findViewById(R.id.port);
        back = findViewById(R.id.drinkBack);

        db=FirebaseFirestore.getInstance();


        drinkinput1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress / 10;
                progress = progress * 10;
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
                progress = progress / 10;
                progress = progress * 10;
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
                progress = progress / 10;
                progress = progress * 10;
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
                    tvMessages.setText("");
                    SERVER_IP = etip.getText().toString().trim();
                    SERVER_PORT = Integer.parseInt(etport.getText().toString().trim());
                    Thread1 = new Thread(new Thread1());
                    Thread1.start();
                }

        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(output==null) {
                    notFound();
                    return;
                }
                Sure();


            }
        });

        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disConnect();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(drink.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void disConnect()  {
        if (socket == null) return;
        try{
            socket.close();
            socket = null;
            bw = null;
            br = null;
        } catch (IOException e){
            e.printStackTrace();
        }

    }
    private  void actSendOrderToFirebse() {




        String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String Userid = currentuser.getEmail();
        Boolean state = false;
        int drink1 = drinkinput1.getProgress()/10*10;
        int drink2 = drinkinput2.getProgress()/10*10;
        int drink3 = drinkinput3.getProgress()/10*10;
        String time = nowDate;


        HashMap<String,Object> order = new HashMap<>();
        order.put("Userid",Userid);
        order.put("state",state);
        order.put("drink1",drink1);
        order.put("drink2",drink2);
        order.put("drink3",drink3);
        order.put("time",time);
        root.child("schedule_"+fireBaseCounter).setValue(order);


        /*root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fireBaseCounter =snapshot.getChildrenCount();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/


    }

    private  void actSendHistryToFirebse(){


        String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        int ml1 =(drinkinput1.getProgress()/10*10);
        int ml2 =(drinkinput2.getProgress()/10*10);
        int ml3 =(drinkinput3.getProgress()/10*10);

        Map<String,Object> history_data= new HashMap<>();
        history_data.put("hisdrink1",ml1);
        history_data.put("hisdrink2",ml2);
        history_data.put("hisdrink3",ml3);
        history_data.put("time",nowDate);

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
    private  void notFound(){
        Toast toast = Toast.makeText(this, "Not device found", Toast.LENGTH_SHORT);
        toast.show();
    }


    class Thread1 implements Runnable {
        public void run() {
            Socket socket;
            try {
                socket = new Socket(SERVER_IP, SERVER_PORT);
                output = new PrintWriter(socket.getOutputStream());
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvMessages.setText("Connected\n");
                    }
                });
                new Thread(new Thread2()).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    class Thread2 implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    final String message = input.readLine();
                    if (message != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvMessages.append("server: " + message + "\n");
                            }
                        });
                    } else {
                        Thread1 = new Thread(new Thread1());
                        Thread1.start();
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    class Thread3 implements Runnable {
        private String ml1,ml2,ml3;

        Thread3(String ml1) {
            this.ml1 = ml1;
            this.ml2 = ml2;
            this.ml3 = ml3;
        }
        @Override
        public void run() {
            output.write(ml1);
            output.write('\n');
           // output.write(ml2);
            //output.write('\n');
            //output.write(ml3);

            output.flush();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvMessages.append("client: " + ml1 + "\n");
                    //tvMessages.append("client: " + ml2 + "\n");
                    //tvMessages.append("client: " + ml3 + "\n");
                }
            });
        }
    }

        private void Sure() {
        int sml1 =(drinkinput1.getProgress()/10*10);
        int sml2 =(drinkinput2.getProgress()/10*10);
        int sml3 =(drinkinput3.getProgress()/10*10);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("進行調飲?");  //設置標題
        builder.setIcon(R.mipmap.ic_launcher_round); //標題前面那個小圖示
        builder.setMessage("飲料1: "+sml1+"\n"+"飲料2: "+sml2+"\n"+"飲料3: "+sml3);


        //確定 取消
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                actSendOrderToFirebse();
                actSendHistryToFirebse();

                int dml1 =(drinkinput1.getProgress()/10*10);
                int dml2 =(drinkinput2.getProgress()/10*10);
                int dml3 =(drinkinput3.getProgress()/10*10);

                String ml1 = String.valueOf(dml1);
                String ml2 = String.valueOf(dml2);
                String ml3 = String.valueOf(dml3);
                if (!ml1.isEmpty()||!ml2.isEmpty()||!ml3.isEmpty()) {
                    new Thread(new Thread3("1")).start();
                }
            }
        });
        builder.create().show();
    }

    /*private void firebase_select(DatabaseReference db){
        final List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int x_sum = (int) snapshot.getChildrenCount();

                for(DataSnapshot ds:snapshot.getChildren()){
                    TextString
                    fireBaseCounter=Integer.parseInt(ds.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/











}