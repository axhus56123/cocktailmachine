package com.example.wifitest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
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

public class history extends AppCompatActivity {

    private Context context = this;
    private ListView lv;
    private FirebaseFirestore db;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentuser = auth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        lv = (ListView)findViewById(R.id.historylv);
        db=FirebaseFirestore.getInstance();

        CollectionReference CR = db.collection("history:"+currentuser.getEmail());
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


                }
                SimpleAdapter SA=new SimpleAdapter(context, items,R.layout.historylist,new String[]{"id","t1","t2","t3"},new int[]{R.id.hisNo,R.id.hislist1,R.id.hislist2,R.id.hislist3});
                lv.setAdapter(SA);
            }
        });

    }
}