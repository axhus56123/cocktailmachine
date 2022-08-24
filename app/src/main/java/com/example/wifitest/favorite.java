package com.example.wifitest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class favorite extends AppCompatActivity {

    private Context context = this;
    private Button add ,del ,fix;
    private EditText t1,t2,t3;
    private ListView lv;
    private FirebaseFirestore db;
    private String x_last="0";
    private String x_select="0";
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentuser = auth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        add = (Button)findViewById(R.id.btnadd);
        fix = (Button)findViewById(R.id.btnfix);
        del = (Button)findViewById(R.id.btndel);
        lv = (ListView)findViewById(R.id.lv);
        t1 = (EditText)findViewById(R.id.fadrinkinput1);
        t2 = (EditText)findViewById(R.id.fadrinkinput2);
        t3 = (EditText)findViewById(R.id.fadrinkinput3);
        db=FirebaseFirestore.getInstance();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> text_data= new HashMap<>();
                text_data.put("t1",t1.getText().toString());
                text_data.put("t2",t2.getText().toString());
                text_data.put("t3",t3.getText().toString());

                Integer x_id=Integer.valueOf(x_last)+1;
                db.collection("favorite:"+currentuser.getEmail()).document(String.valueOf(x_id)).set(text_data)
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
                data_select();
            }
        });

        fix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> text_data= new HashMap<>();
                text_data.put("t1",t1.getText().toString());
                text_data.put("t2",t2.getText().toString());
                text_data.put("t3",t3.getText().toString());

                db.collection("favorite:"+currentuser.getEmail()).document(x_select).update(text_data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("結果","更新成功");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("結果","更新失敗");
                            }
                        });
                data_select();

            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("favorite:"+currentuser.getEmail()).document(x_select).delete();
                data_select();
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView t1_s =(TextView)view.findViewById(R.id.faNo);
                x_select=t1_s.getText().toString();
                TextView t2_s =(TextView)view.findViewById(R.id.drinklist1);
                t1.setText(t2_s.getText().toString());
                TextView t3_s =(TextView)view.findViewById(R.id.drinklist2);
                t2.setText(t3_s.getText().toString());
                TextView t4_s =(TextView)view.findViewById(R.id.drinklist3);
                t3.setText(t4_s.getText().toString());
            }
        });
        data_select();


    }
    private void data_select(){
        CollectionReference CR = db.collection("favorite:"+currentuser.getEmail());
        final List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
        CR.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot document: task.getResult()){
                    Map<String,Object> item=new HashMap<String,Object>();
                    item.put("id",document.getId());
                    item.put("t1",document.get("t1"));
                    item.put("t2",document.get("t2"));
                    item.put("t3",document.get("t3"));
                    items.add(item);
                    x_last=document.getId();

                }
                SimpleAdapter SA=new SimpleAdapter(context, items,R.layout.favoritelist,new String[]{"id","t1","t2","t3"},new int[]{R.id.faNo,R.id.drinklist1,R.id.drinklist2,R.id.drinklist3});
                lv.setAdapter(SA);
            }
        });

    }
}