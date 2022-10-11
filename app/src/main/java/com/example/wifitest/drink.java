package com.example.wifitest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class drink extends AppCompatActivity {

    Thread Thread1 = null;
    private Context context = this;
    private ImageButton back;
    private ListView lv;
    private Button send ,btnconnect,disconnect;
    private SeekBar drinkinput1,drinkinput2,drinkinput3,drinkinput4,drinkinput5,drinkinput6;
    private TextView ml1,ml2,ml3,tvMessages,ml4,ml5,ml6,ML1,ML2,ML3,ML4,ML5,ML6;
    private EditText etip,etport;
    private PrintWriter output;
    private BufferedReader input;
    public Socket socket;
    private FirebaseFirestore db;
    private String x_last="0";
    private String x_select="0";
    String SERVER_IP;
    int SERVER_PORT;
    long fireBaseCounter,endcounter;
    String a;
    private Thread thread;                //執行緒
    private BufferedWriter bw;            //取得網路輸出串流
    private BufferedReader br;            //取得網路輸入串流
    private String tmp;   //做為接收時的緩存
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentuser = auth.getCurrentUser();
    private FirebaseDatabase Db = FirebaseDatabase.getInstance();
    private FirebaseDatabase ESP32 = FirebaseDatabase.getInstance();








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_drink);
        setTitle("調飲");
        send = findViewById(R.id.send);

        //btnconnect = findViewById(R.id.btnconnect);
        //disconnect = findViewById(R.id.btndisconnect);
        drinkinput1 = findViewById(R.id.drinkinput1);
        drinkinput2 = findViewById(R.id.drinkinput2);
        drinkinput3 = findViewById(R.id.drinkinput3);
        drinkinput4 = findViewById(R.id.drinkinput4);
        drinkinput5 = findViewById(R.id.drinkinput5);
        drinkinput6 = findViewById(R.id.drinkinput6);
        //tvMessages = findViewById(R.id.tvMessages);
        ml1 = findViewById(R.id.drinkml1);
        ml2 = findViewById(R.id.drinkml2);
        ml3 = findViewById(R.id.drinkml3);
        ml4 = findViewById(R.id.drinkml4);
        ml5 = findViewById(R.id.drinkml5);
        ml6 = findViewById(R.id.drinkml6);
        ML1 = findViewById(R.id.ML1);
        ML2 = findViewById(R.id.ML2);
        ML3 = findViewById(R.id.ML3);
        ML4 = findViewById(R.id.ML4);
        ML5 = findViewById(R.id.ML5);
        ML6 = findViewById(R.id.ML6);
        //etip = findViewById(R.id.ip);
        //etport = findViewById(R.id.port);
        back = findViewById(R.id.drinkBack);

        db=FirebaseFirestore.getInstance();
        Manager();


        drinkinput1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress / 10;
                progress = progress * 10;
                ML1.setText(String.valueOf(progress)+"ml");
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
                ML2.setText(String.valueOf(progress)+"ml");
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
                ML3.setText(String.valueOf(progress)+"ml");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        drinkinput4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress / 10;
                progress = progress * 10;
                ML4.setText(String.valueOf(progress)+"ml");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        drinkinput5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress / 10;
                progress = progress * 10;
                ML5.setText(String.valueOf(progress)+"ml");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        drinkinput6.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress / 10;
                progress = progress * 10;
                ML6.setText(String.valueOf(progress)+"ml");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        /*btnconnect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                    tvMessages.setText("");
                    SERVER_IP = etip.getText().toString().trim();
                    SERVER_PORT = Integer.parseInt(etport.getText().toString().trim());
                    Thread1 = new Thread(new Thread1());
                    Thread1.start();
                }

        });*/

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(output==null) {
                    notFound();
                    return;
                }*/
                if(drinkinput1.getProgress()==0&&drinkinput2.getProgress()==0&&drinkinput3.getProgress()==0&&drinkinput4.getProgress()==0&&drinkinput5.getProgress()==0&&drinkinput6.getProgress()==0){
                    notinput();
                    return;
                }
                Sure();


            }
        });

        /*disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disConnect();
            }
        });*/

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(drink.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    /*private void disConnect()  {
        if (socket == null) return;
        try{
            socket.close();
            socket = null;
            bw = null;
            br = null;
        } catch (IOException e){
            e.printStackTrace();
        }

    }*/
    private  void actSendOrderToFirebse() {
        DatabaseReference count = Db.getReference("count/end");
        DatabaseReference root = Db.getReference("test");
        String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String Userid = currentuser.getEmail();
        Boolean state = false;
        int drink1 = drinkinput1.getProgress()/10*10;
        int drink2 = drinkinput2.getProgress()/10*10;
        int drink3 = drinkinput3.getProgress()/10*10;
        int drink4 = drinkinput4.getProgress()/10*10;
        int drink5 = drinkinput5.getProgress()/10*10;
        int drink6 = drinkinput6.getProgress()/10*10;
        String time = nowDate;


        HashMap<String,Object> order = new HashMap<>();
        order.put("Userid",Userid);
        order.put("state",state);
        order.put("drink1",drink1);
        order.put("drink2",drink2);
        order.put("drink3",drink3);
        order.put("drink4",drink4);
        order.put("drink5",drink5);
        order.put("drink6",drink6);
        order.put("time",time);
        count.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult();
                long endcounter = (long) dataSnapshot.getValue();
                endcounter+=1;
                count.setValue(endcounter);
                fireBaseCounter = (long) dataSnapshot.getValue()+1;
                root.child("schedule_"+fireBaseCounter).setValue(order);
            }

        });

        //count.setValue(endcounter);



    }

    private  void actSendHistryToFirebse(){


        String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        int ml1 =(drinkinput1.getProgress()/10*10);
        int ml2 =(drinkinput2.getProgress()/10*10);
        int ml3 =(drinkinput3.getProgress()/10*10);
        int ml4 =(drinkinput4.getProgress()/10*10);
        int ml5 =(drinkinput5.getProgress()/10*10);
        int ml6 =(drinkinput6.getProgress()/10*10);

        Map<String,Object> history_data= new HashMap<>();
        history_data.put("hisdrink1",ml1);
        history_data.put("hisdrink2",ml2);
        history_data.put("hisdrink3",ml3);
        history_data.put("hisdrink4",ml4);
        history_data.put("hisdrink5",ml5);
        history_data.put("hisdrink6",ml6);
        history_data.put("time",nowDate);

        db.collection("history:"+currentuser.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int hiscounter = task.getResult().size();
                db.collection("history:"+currentuser.getEmail()).document(String.valueOf(hiscounter)).set(history_data);
            }
        });


        /*db.collection("history:"+currentuser.getEmail()).document(String.valueOf(x_id)).set(history_data)
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
                });*/

    }
    /*private  void notFound(){
        Toast toast = Toast.makeText(this, "Not device found", Toast.LENGTH_SHORT);
        toast.show();
    }*/


    /*class Thread1 implements Runnable {
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
    }*/
   /* class Thread2 implements Runnable {
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
    }*/
    /*class Thread3 implements Runnable {
        private String ml1,ml2,ml3,ml4,ml5;

        Thread3(String ml1) {
            this.ml1 = ml1;
            this.ml2 = ml2;
            this.ml3 = ml3;
            this.ml4 = ml4;
            this.ml5 = ml5;
        }
        @Override
        public void run() {
            output.write(ml1);
            output.write('\n');
            //output.write(ml2);
            //output.write('\n');
            //output.write(ml3);
            //output.write('\n');
            //output.write(ml4);
            //output.write('\n');
            //output.write(ml5);

            output.flush();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvMessages.append("client: " + ml1 + "\n");
                    //tvMessages.append("client: " + ml2 + "\n");
                    //tvMessages.append("client: " + ml3 + "\n");
                    //tvMessages.append("client: " + ml4 + "\n");
                    //tvMessages.append("client: " + ml5 + "\n");
                }
            });
        }
    }*/

        private void Sure() {
        int sml1 =(drinkinput1.getProgress()/10*10);
        int sml2 =(drinkinput2.getProgress()/10*10);
        int sml3 =(drinkinput3.getProgress()/10*10);
        int sml4 =(drinkinput4.getProgress()/10*10);
        int sml5 =(drinkinput5.getProgress()/10*10);
        int sml6 =(drinkinput6.getProgress()/10*10);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("進行調飲?");  //設置標題
        builder.setIcon(R.drawable.icon); //標題前面那個小圖示
        builder.setMessage("飲料1: "+sml1+"\n"+"飲料2: "+sml2+"\n"+"飲料3: "+sml3+"\n"+"飲料4: "+sml4+"\n"+"飲料5: "+sml5+"\n"+"飲料6: "+sml6);


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

                //readData();
                actSendOrderToFirebse();
                actSendHistryToFirebse();

                int dml1 =(drinkinput1.getProgress()/10*10);
                int dml2 =(drinkinput2.getProgress()/10*10);
                int dml3 =(drinkinput3.getProgress()/10*10);
                int dml4 =(drinkinput4.getProgress()/10*10);
                int dml5 =(drinkinput5.getProgress()/10*10);
                int dml6 =(drinkinput6.getProgress()/10*10);

                String ml1 = String.valueOf(dml1);
                String ml2 = String.valueOf(dml2);
                String ml3 = String.valueOf(dml3);
                String ml4 = String.valueOf(dml4);
                String ml5 = String.valueOf(dml5);
                String ml6 = String.valueOf(dml6);
                /*if (!ml1.isEmpty()||!ml2.isEmpty()||!ml3.isEmpty()||!ml4.isEmpty()||!ml5.isEmpty()) {
                    new Thread(new Thread3("1")).start();
                }*/
                Sure2();
            }
        });
        builder.create().show();

    }
    private void Sure2(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("上傳成功");  //設置標題
        builder.setIcon(R.drawable.icon); //標題前面那個小圖示
        builder.setMessage("是否前往首頁");
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
                Intent intent = new Intent();
                intent.setClass(drink.this,MainActivity.class);
                startActivity(intent);
            }
        });
        builder.create().show();
    }
    private void notinput(){
        Toast toast = Toast.makeText(this, "未輸入飲料ml", Toast.LENGTH_SHORT);

        toast.show();

    }
    private void Manager(){
        DatabaseReference ESP32 = Db.getReference("ESP32");

        ESP32.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult();
                int drinkcounter = (int) dataSnapshot.getChildrenCount();

                switch (drinkcounter){
                    case 1:
                        drinkinput1.setVisibility(View.VISIBLE);
                        drinkinput2.setVisibility(View.GONE);
                        drinkinput3.setVisibility(View.GONE);
                        drinkinput4.setVisibility(View.GONE);
                        drinkinput5.setVisibility(View.GONE);
                        drinkinput6.setVisibility(View.GONE);
                        ml1.setVisibility(View.VISIBLE);
                        ml2.setVisibility(View.GONE);
                        ml3.setVisibility(View.GONE);
                        ml4.setVisibility(View.GONE);
                        ml5.setVisibility(View.GONE);
                        ml6.setVisibility(View.GONE);
                        ML1.setVisibility(View.VISIBLE);
                        ML2.setVisibility(View.GONE);
                        ML3.setVisibility(View.GONE);
                        ML4.setVisibility(View.GONE);
                        ML5.setVisibility(View.GONE);
                        ML6.setVisibility(View.GONE);
                        ml1.setText((String) dataSnapshot.child("drink_1").getValue());
                        break;
                    case 2:
                        drinkinput1.setVisibility(View.VISIBLE);
                        drinkinput2.setVisibility(View.VISIBLE);
                        drinkinput3.setVisibility(View.GONE);
                        drinkinput4.setVisibility(View.GONE);
                        drinkinput5.setVisibility(View.GONE);
                        drinkinput6.setVisibility(View.GONE);
                        ml1.setVisibility(View.VISIBLE);
                        ml2.setVisibility(View.VISIBLE);
                        ml3.setVisibility(View.GONE);
                        ml4.setVisibility(View.GONE);
                        ml5.setVisibility(View.GONE);
                        ml6.setVisibility(View.GONE);
                        ML1.setVisibility(View.VISIBLE);
                        ML2.setVisibility(View.VISIBLE);
                        ML3.setVisibility(View.GONE);
                        ML4.setVisibility(View.GONE);
                        ML5.setVisibility(View.GONE);
                        ML6.setVisibility(View.GONE);
                        ml1.setText((String) dataSnapshot.child("drink_1").getValue());
                        ml2.setText((String) dataSnapshot.child("drink_2").getValue());
                        break;
                    case 3:
                        drinkinput1.setVisibility(View.VISIBLE);
                        drinkinput2.setVisibility(View.VISIBLE);
                        drinkinput3.setVisibility(View.VISIBLE);
                        drinkinput4.setVisibility(View.GONE);
                        drinkinput5.setVisibility(View.GONE);
                        drinkinput6.setVisibility(View.GONE);
                        ml1.setVisibility(View.VISIBLE);
                        ml2.setVisibility(View.VISIBLE);
                        ml3.setVisibility(View.VISIBLE);
                        ml4.setVisibility(View.GONE);
                        ml5.setVisibility(View.GONE);
                        ml6.setVisibility(View.GONE);
                        ML1.setVisibility(View.VISIBLE);
                        ML2.setVisibility(View.VISIBLE);
                        ML3.setVisibility(View.VISIBLE);
                        ML4.setVisibility(View.GONE);
                        ML5.setVisibility(View.GONE);
                        ML6.setVisibility(View.GONE);
                        ml1.setText((String) dataSnapshot.child("drink_1").getValue());
                        ml2.setText((String) dataSnapshot.child("drink_2").getValue());
                        ml3.setText((String) dataSnapshot.child("drink_3").getValue());
                        break;
                    case 4:
                        drinkinput1.setVisibility(View.VISIBLE);
                        drinkinput2.setVisibility(View.VISIBLE);
                        drinkinput3.setVisibility(View.VISIBLE);
                        drinkinput4.setVisibility(View.VISIBLE);
                        drinkinput5.setVisibility(View.GONE);
                        drinkinput6.setVisibility(View.GONE);
                        ml1.setVisibility(View.VISIBLE);
                        ml2.setVisibility(View.VISIBLE);
                        ml3.setVisibility(View.VISIBLE);
                        ml4.setVisibility(View.VISIBLE);
                        ml5.setVisibility(View.GONE);
                        ml6.setVisibility(View.GONE);
                        ML1.setVisibility(View.VISIBLE);
                        ML2.setVisibility(View.VISIBLE);
                        ML3.setVisibility(View.VISIBLE);
                        ML4.setVisibility(View.VISIBLE);
                        ML5.setVisibility(View.GONE);
                        ML6.setVisibility(View.GONE);
                        ml1.setText((String) dataSnapshot.child("drink_1").getValue());
                        ml2.setText((String) dataSnapshot.child("drink_2").getValue());
                        ml3.setText((String) dataSnapshot.child("drink_3").getValue());
                        ml4.setText((String) dataSnapshot.child("drink_4").getValue());
                        break;
                    case 5:
                        drinkinput1.setVisibility(View.VISIBLE);
                        drinkinput2.setVisibility(View.VISIBLE);
                        drinkinput3.setVisibility(View.VISIBLE);
                        drinkinput4.setVisibility(View.VISIBLE);
                        drinkinput5.setVisibility(View.VISIBLE);
                        drinkinput6.setVisibility(View.GONE);
                        ml1.setVisibility(View.VISIBLE);
                        ml2.setVisibility(View.VISIBLE);
                        ml3.setVisibility(View.VISIBLE);
                        ml4.setVisibility(View.VISIBLE);
                        ml5.setVisibility(View.VISIBLE);
                        ml6.setVisibility(View.GONE);
                        ML1.setVisibility(View.VISIBLE);
                        ML2.setVisibility(View.VISIBLE);
                        ML3.setVisibility(View.VISIBLE);
                        ML4.setVisibility(View.VISIBLE);
                        ML5.setVisibility(View.VISIBLE);
                        ML6.setVisibility(View.GONE);
                        ml1.setText((String) dataSnapshot.child("drink_1").getValue());
                        ml2.setText((String) dataSnapshot.child("drink_2").getValue());
                        ml3.setText((String) dataSnapshot.child("drink_3").getValue());
                        ml4.setText((String) dataSnapshot.child("drink_4").getValue());
                        ml5.setText((String) dataSnapshot.child("drink_5").getValue());
                        break;
                    case 6:
                        drinkinput1.setVisibility(View.VISIBLE);
                        drinkinput2.setVisibility(View.VISIBLE);
                        drinkinput3.setVisibility(View.VISIBLE);
                        drinkinput4.setVisibility(View.VISIBLE);
                        drinkinput5.setVisibility(View.VISIBLE);
                        drinkinput6.setVisibility(View.VISIBLE);
                        ml1.setVisibility(View.VISIBLE);
                        ml2.setVisibility(View.VISIBLE);
                        ml3.setVisibility(View.VISIBLE);
                        ml4.setVisibility(View.VISIBLE);
                        ml5.setVisibility(View.VISIBLE);
                        ml6.setVisibility(View.VISIBLE);
                        ML1.setVisibility(View.VISIBLE);
                        ML2.setVisibility(View.VISIBLE);
                        ML3.setVisibility(View.VISIBLE);
                        ML4.setVisibility(View.VISIBLE);
                        ML5.setVisibility(View.VISIBLE);
                        ML6.setVisibility(View.VISIBLE);
                        ml1.setText((String) dataSnapshot.child("drink_1").getValue());
                        ml2.setText((String) dataSnapshot.child("drink_2").getValue());
                        ml3.setText((String) dataSnapshot.child("drink_3").getValue());
                        ml4.setText((String) dataSnapshot.child("drink_4").getValue());
                        ml5.setText((String) dataSnapshot.child("drink_5").getValue());
                        ml6.setText((String) dataSnapshot.child("drink_6").getValue());
                        break;
                }
            }
        });
    }



}