package com.example.wifitest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class loveadapter extends FirestoreRecyclerAdapter<loveuser,loveadapter.loveholder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public loveadapter(@NonNull FirestoreRecyclerOptions<loveuser> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull loveholder holder, int position, @NonNull loveuser model) {
        holder.lovename.setText(model.getLovename());
        holder.lovedrink1.setText(model.getLovedrink1()+"");
        holder.lovedrink2.setText(model.getLovedrink2()+"");
        holder.lovedrink3.setText(model.getLovedrink3()+"");
        holder.lovedrink4.setText(model.getLovedrink4()+"");
        holder.lovedrink5.setText(model.getLovedrink5()+"");
        holder.lovedrink6.setText(model.getLovedrink6()+"");
        holder.lovedrink1ml.setText(model.getLovedrink1ml()+"ml");
        holder.lovedrink2ml.setText(model.getLovedrink2ml()+"ml");
        holder.lovedrink3ml.setText(model.getLovedrink3ml()+"ml");
        holder.lovedrink4ml.setText(model.getLovedrink4ml()+"ml");
        holder.lovedrink5ml.setText(model.getLovedrink5ml()+"ml");
        holder.lovedrink6ml.setText(model.getLovedrink6ml()+"ml");
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

                int drink1 = Integer.valueOf(model.getLovedrink1ml());
                int drink2 = Integer.valueOf(model.getLovedrink2ml());
                int drink3 = Integer.valueOf(model.getLovedrink3ml());
                int drink4 = Integer.valueOf(model.getLovedrink4ml());
                int drink5 = Integer.valueOf(model.getLovedrink5ml());
                int drink6 = Integer.valueOf(model.getLovedrink6ml());
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
                        capacity.setValue(ml);
                    }
                });

            }
        });
    }

    @NonNull
    @Override
    public loveholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.love_item,parent,false);
        return new loveholder(view);
    }
    public void deleteitem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class loveholder extends  RecyclerView.ViewHolder{

        TextView lovename,lovedrink1,lovedrink2,lovedrink3,lovedrink4,lovedrink5,lovedrink6,lovedrink1ml,lovedrink2ml,lovedrink3ml,lovedrink4ml,lovedrink5ml,lovedrink6ml;
        ImageView cup;
        public loveholder(@NonNull View itemView) {
            super(itemView);
            lovename = itemView.findViewById(R.id.lovename);
            lovedrink1ml = itemView.findViewById(R.id.lovedrink1ml);
            lovedrink2ml = itemView.findViewById(R.id.lovedrink2ml);
            lovedrink3ml = itemView.findViewById(R.id.lovedrink3ml);
            lovedrink4ml = itemView.findViewById(R.id.lovedrink4ml);
            lovedrink5ml = itemView.findViewById(R.id.lovedrink5ml);
            lovedrink6ml = itemView.findViewById(R.id.lovedrink6ml);
            lovedrink1 = itemView.findViewById(R.id.lovedrink1);
            lovedrink2 = itemView.findViewById(R.id.lovedrink2);
            lovedrink3 = itemView.findViewById(R.id.lovedrink3);
            lovedrink4 = itemView.findViewById(R.id.lovedrink4);
            lovedrink5 = itemView.findViewById(R.id.lovedrink5);
            lovedrink6 = itemView.findViewById(R.id.lovedrink6);
            cup = itemView.findViewById(R.id.cup);
        }

    }
}
