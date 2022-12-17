package com.example.wifitest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class history extends AppCompatActivity {

    private RecyclerView hrecyclerView;
    private ImageButton back;


    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentuser = auth.getCurrentUser();
    ProgressDialog progressDialog;
    private FirebaseFirestore db;

    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        db = FirebaseFirestore.getInstance();
        hrecyclerView = findViewById(R.id.hisrecycle);
        back = findViewById(R.id.hisBack);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(history.this,bottomOption.class);
                startActivity(intent);
            }
        });

        Query query = db.collection("history:"+currentuser.getEmail());
        FirestoreRecyclerOptions<hisuser> options = new FirestoreRecyclerOptions.Builder<hisuser>()
                .setQuery(query, hisuser.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<hisuser, ProductsViewHolder>(options) {
            @NonNull
            @Override
            public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
                return new ProductsViewHolder(view);
            }

            public void deleteitem(int position){
                getSnapshots().getSnapshot(position).getReference().delete();
            }

            @Override
            protected void onBindViewHolder(@NonNull ProductsViewHolder holder, int position, @NonNull hisuser model) {
                holder.hisdrink1.setText(model.getHisdrink1()+"");
                holder.hisdrink2.setText(model.getHisdrink2()+"");
                holder.hisdrink3.setText(model.getHisdrink3()+"");
                holder.hisdrink4.setText(model.getHisdrink4()+"");
                holder.hisdrink5.setText(model.getHisdrink5()+"");
                holder.hisdrink6.setText(model.getHisdrink6()+"");
                holder.time.setText(model.getTime()+"");

                holder.cup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        FirebaseFirestore db;
                        FirebaseDatabase Db = FirebaseDatabase.getInstance();
                        DatabaseReference count = Db.getReference("count/end");
                        DatabaseReference root = Db.getReference("test");
                        DatabaseReference capacity = Db.getReference("capacity");
                        Boolean state = false;
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        FirebaseUser currentuser = auth.getCurrentUser();
                        String Userid = currentuser.getEmail();
                        db=FirebaseFirestore.getInstance();

                        int drink1 = Integer.valueOf((int) model.getHisdrink1());
                        int drink2 = Integer.valueOf((int) model.getHisdrink2());
                        int drink3 = Integer.valueOf((int) model.getHisdrink3());
                        int drink4 = Integer.valueOf((int) model.getHisdrink4());
                        int drink5 = Integer.valueOf((int) model.getHisdrink5());
                        int drink6 = Integer.valueOf((int) model.getHisdrink6());
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
                                long fireBaseCounter;
                                long endcounter = (long) dataSnapshot.getValue();
                                endcounter+=1;
                                count.setValue(endcounter);
                                fireBaseCounter = (long) dataSnapshot.getValue()+1;
                                root.child("schedule_"+fireBaseCounter).setValue(order);
                            }

                        });


                        Map<String,Object> history_data= new HashMap<>();
                        history_data.put("hisdrink1",drink1);
                        history_data.put("hisdrink2",drink2);
                        history_data.put("hisdrink3",drink3);
                        history_data.put("hisdrink4",drink4);
                        history_data.put("hisdrink5",drink5);
                        history_data.put("hisdrink6",drink6);
                        history_data.put("time",nowDate);

                        db.collection("history:"+currentuser.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                int hiscounter = task.getResult().size();
                                db.collection("history:"+currentuser.getEmail()).document(String.valueOf(hiscounter)).set(history_data);
                            }
                        });
                        capacity.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                DataSnapshot dataSnapshot = task.getResult();
                                long getdataml1 = (long) dataSnapshot.child("drink1").getValue();
                                long getdataml2 = (long) dataSnapshot.child("drink2").getValue();
                                long getdataml3 = (long) dataSnapshot.child("drink3").getValue();
                                long getdataml4 = (long) dataSnapshot.child("drink4").getValue();
                                long getdataml5 = (long) dataSnapshot.child("drink5").getValue();
                                long getdataml6 = (long) dataSnapshot.child("drink6").getValue();
                                int setdataml1 = (int) (getdataml1-drink1);
                                int setdataml2 = (int) (getdataml2-drink2);
                                int setdataml3 = (int) (getdataml3-drink3);
                                int setdataml4 = (int) (getdataml4-drink4);
                                int setdataml5 = (int) (getdataml5-drink5);
                                int setdataml6 = (int) (getdataml6-drink6);

                                HashMap<String,Object> ml = new HashMap<>();
                                ml.put("drink1",setdataml1);
                                ml.put("drink2",setdataml2);
                                ml.put("drink3",setdataml3);
                                ml.put("drink4",setdataml4);
                                ml.put("drink5",setdataml5);
                                ml.put("drink6",setdataml6);
                                capacity.setValue(ml).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast toast = Toast.makeText(history.this, "輸入成功", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                });
                            }
                        });
                    }
                });

            }

        };
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(hrecyclerView.VERTICAL);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        hrecyclerView.setLayoutManager(layoutManager);
        hrecyclerView.setHasFixedSize(true);
        hrecyclerView.setAdapter(adapter);


    }

    static class ProductsViewHolder extends  RecyclerView.ViewHolder{
        private TextView hisdrink1,hisdrink2,hisdrink3,hisdrink4,hisdrink5,hisdrink6,time;
        private ImageView cup;

        public ProductsViewHolder(@NonNull View itemView){

            super(itemView);
            hisdrink1 = itemView.findViewById(R.id.hisdrink1);
            hisdrink2 = itemView.findViewById(R.id.hisdrink2);
            hisdrink3 = itemView.findViewById(R.id.hisdrink3);
            hisdrink4 = itemView.findViewById(R.id.hisdrink4);
            hisdrink5 = itemView.findViewById(R.id.hisdrink5);
            hisdrink6 = itemView.findViewById(R.id.hisdrink6);
            cup = itemView.findViewById(R.id.cup);
            time = itemView.findViewById(R.id.histime);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

}