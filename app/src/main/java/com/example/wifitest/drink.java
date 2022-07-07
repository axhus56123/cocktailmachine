package com.example.wifitest;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;


public class drink extends AppCompatActivity {




    private Button send ,btnconnect;
    private EditText drinkinput1;
    private EditText drinkinput2;
    private EditText drinkinput3;
    private EditText ip;
    private TextView input1textView;
    private TextView input2textView;
    private TextView input3textView;
    private InputStream inputStream;
    private OutputStream outputStream;
    public Socket socket;
    private Thread thread;                //執行緒

    private BufferedWriter bw;            //取得網路輸出串流
    private BufferedReader br;            //取得網路輸入串流
    private String tmp;                    //做為接收時的緩存





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        send = findViewById(R.id.send);
        btnconnect = findViewById(R.id.btnconnect);
        drinkinput1 = findViewById(R.id.drinkinput1);
        drinkinput2 = findViewById(R.id.drinkinput2);
        drinkinput3 = findViewById(R.id.drinkinput3);
        ip = findViewById(R.id.ip);
        input1textView = findViewById(R.id.input1textView);
        input2textView = findViewById(R.id.input2textView);
        input3textView = findViewById(R.id.input3textView);





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
        Thread mThread = new Thread(trans);
        mThread.start();

    }

    private Runnable trans = new Runnable ()
    {
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



    } ;




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