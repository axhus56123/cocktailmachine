package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

public class drink extends AppCompatActivity {


    private EditText drinkinput1;
    private EditText drinkinput2;
    private EditText drinkinput3;
    private TextView input1textView;
    private TextView input2textView;
    private TextView input3textView;
    private InputStream inputStream;
    private OutputStream outputStream;

    private Thread thread;                //執行緒
    private Socket clientSocket;        //客戶端的socket
    private BufferedWriter bw;            //取得網路輸出串流
    private BufferedReader br;            //取得網路輸入串流
    private String tmp;                    //做為接收時的緩存
    private JSONObject json_write,json_read;        //從java伺服器傳遞與接收資料的json




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);


        thread=new Thread(Connection);                //賦予執行緒工作
        thread.start();                    //讓執行緒開始執行


        drinkinput1 = findViewById(R.id.drinkinput1);
        drinkinput2 = findViewById(R.id.drinkinput2);
        drinkinput3 = findViewById(R.id.drinkinput3);
        input1textView = findViewById(R.id.input1textView);
        input2textView = findViewById(R.id.input2textView);
        input3textView = findViewById(R.id.input3textView);

    }


    //連結socket伺服器做傳送與接收
    private Runnable Connection=new Runnable(){
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
    };



    public void outputml(View view){


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


    }


}