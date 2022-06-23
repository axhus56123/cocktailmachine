package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class drink extends AppCompatActivity {


    private Button send ,connect;
    private EditText drinkinput1;
    private EditText drinkinput2;
    private EditText drinkinput3;
    private EditText ip;
    private TextView input1textView;
    private TextView input2textView;
    private TextView input3textView;
    private InputStream inputStream;
    private OutputStream outputStream;

    private Thread thread;                //執行緒
    private Socket socket = null;
    private BufferedWriter bw;            //取得網路輸出串流
    private BufferedReader br;            //取得網路輸入串流
    private String tmp;                    //做為接收時的緩存
    private JSONObject json_write,json_read;        //從java伺服器傳遞與接收資料的json




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);


        /*thread=new Thread(Connection);                //賦予執行緒工作
        thread.start();*/                    //讓執行緒開始執行

        send = findViewById(R.id.send);
        connect = findViewById(R.id.connect);
        drinkinput1 = findViewById(R.id.drinkinput1);
        drinkinput2 = findViewById(R.id.drinkinput2);
        drinkinput3 = findViewById(R.id.drinkinput3);
        ip = findViewById(R.id.ip);
        input1textView = findViewById(R.id.input1textView);
        input2textView = findViewById(R.id.input2textView);
        input3textView = findViewById(R.id.input3textView);

        findViewById(R.id.connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect();
            }
        });
        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });


    }


    public void connect()  {
        AsyncTask <Void ,String ,Void> read = new AsyncTask<Void, String, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    socket = new Socket(ip.getText().toString(),1234);
                    bw = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
                    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch(UnknownHostException e){
                    Toast.makeText(drink.this,"無法連接",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }catch (IOException e) {
                    Toast.makeText(drink.this,"無法連接",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


                try{
                    String line;
                    while((line = br.readLine())!=null){
                        publishProgress(line);
                    }

                }catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                Toast.makeText(drink.this,"連接成功",Toast.LENGTH_SHORT).show();

                super.onProgressUpdate(values);
            }

        };
        read.execute();


    }
    public void send() {
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
                e.printStackTrace();
            }




    }



    //連結socket伺服器做傳送與接收
    /*private Runnable Connection=new Runnable(){
        @Override
        public void run() {
            // TODO Auto-generated method stub
            try{
                // IP為Server端
                InetAddress serverIp = InetAddress.getByName("192.168.0.1");
                int serverPort = 5050;
                clientSocket = new Socket(serverIp, serverPort);
                inputStream = clientSocket.getInputStream();
                outputStream = clientSocket.getOutputStream();
                //取得網路輸出串流
                bw = new BufferedWriter( new OutputStreamWriter(clientSocket.getOutputStream()));
                // 取得網路輸入串流
                br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // 當連線後
                while (clientSocket.isConnected()) {
                    // 取得網路訊息
                    tmp = br.readLine();    //宣告一個緩衝,從br串流讀取值
                    // 如果不是空訊息
                    if(tmp!=null){
                        //將取到的String抓取{}範圍資料
                        tmp=tmp.substring(tmp.indexOf("{"), tmp.lastIndexOf("}") + 1);
                        json_read=new JSONObject(tmp);
                        //從java伺服器取得值後做拆解,可使用switch做不同動作的處理
                    }
                }
            }catch(Exception e){
                //當斷線時會跳到catch,可以在這裡寫上斷開連線後的處理
                e.printStackTrace();
                Log.e("text","Socket連線="+e.toString());
                finish();    //當斷線時自動關閉房間
            }
        }
    };*/



    /*public void send(){


        try{
            outputStream.write(drinkinput1.getText().toString().getBytes());

            outputStream.write(drinkinput2.getText().toString().getBytes());

            outputStream.write(drinkinput3.getText().toString().getBytes());

            outputStream.flush();



            input1textView.setText("飲料1設定為 "+drinkinput1.getText()+" ml");
            input2textView.setText("飲料2設定為 "+drinkinput2.getText()+" ml");
            input3textView.setText("飲料3設定為 "+drinkinput3.getText()+" ml");



        } catch (Exception e) {
            e.printStackTrace();
        }


    }*/


}