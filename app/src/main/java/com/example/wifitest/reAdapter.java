package com.example.wifitest;

import static android.content.Context.CONNECTIVITY_SERVICE;

import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.core.content.ContextCompat.startActivity;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wifitest.model.Post;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class reAdapter extends FirestoreRecyclerAdapter<reuser,reAdapter.reHolder> {

    private Activity context;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public reAdapter(@NonNull FirestoreRecyclerOptions<reuser> options) {

        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull reHolder holder, int position, @NonNull reuser model) {
        holder.Rename.setText(model.getRename());
        holder.Redrink1.setText(model.getRedrink1());
        holder.Redrink2.setText(model.getRedrink2());
        holder.Redrink3.setText(model.getRedrink3());
        holder.Redrink4.setText(model.getRedrink4());
        holder.Redrink5.setText(model.getRedrink5());
        holder.Redrink6.setText(model.getRedrink6());
        holder.about.setText(model.getAbout());
        holder.Redrink1ml.setText(model.getRedrink1ml());
        holder.Redrink2ml.setText(model.getRedrink2ml());
        holder.Redrink3ml.setText(model.getRedrink3ml());
        holder.Redrink4ml.setText(model.getRedrink4ml());
        holder.Redrink5ml.setText(model.getRedrink5ml());
        holder.Redrink6ml.setText(model.getRedrink6ml());

        //holder.Image.setImageURI(model.getImage());
        Glide.with(holder.itemView.getContext()).load(model.getImage()).into(holder.Image);

        holder.cup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int drink1 = Integer.valueOf(model.getRedrink1ml());
                int drink2 = Integer.valueOf(model.getRedrink2ml());
                int drink3 = Integer.valueOf(model.getRedrink3ml());
                int drink4 = Integer.valueOf(model.getRedrink4ml());
                int drink5 = Integer.valueOf(model.getRedrink5ml());
                int drink6 = Integer.valueOf(model.getRedrink6ml());
                String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                String time = nowDate;
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("製作飲料");  //設置標題
                builder.setIcon(R.drawable.icon); //標題前面那個小圖示
                builder.setMessage("飲料1: "+drink1+"\n"+"飲料2: "+drink2+"\n"+"飲料3: "+drink3+"\n"+"飲料4: "+drink4+"\n"+"飲料5: "+drink5+"\n"+"飲料6: "+drink6);
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

                                    }
                                });
                            }
                        });
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
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
                                Intent intent1 = new Intent();
                                intent1.setClass(v.getContext(), MainActivity.class);
                                v.getContext().startActivity(intent1);
                            }
                        });
                        builder.create().show();
                    }
                });
                builder.create().show();
            }


        });
    }

    @NonNull
    @Override
    public reHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rece_item,parent,false);
        return new reHolder(v);
    }
    public void deleteitem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class reHolder extends RecyclerView.ViewHolder {

        TextView Redrink1,Redrink2,Redrink3,Redrink4,Redrink5,Redrink6,about;
        TextView Redrink1ml,Redrink2ml,Redrink3ml,Redrink4ml,Redrink5ml,Redrink6ml;
        TextView Rename;
        ImageView cup, Image;

        public reHolder(@NonNull View itemView) {
            super(itemView);
            Redrink1 = itemView.findViewById(R.id.redrink1);
            Redrink2 = itemView.findViewById(R.id.redrink2);
            Redrink3 = itemView.findViewById(R.id.redrink3);
            Redrink4 = itemView.findViewById(R.id.redrink4);
            Redrink5 = itemView.findViewById(R.id.redrink5);
            Redrink6 = itemView.findViewById(R.id.redrink6);
            about = itemView.findViewById(R.id.about);
            Redrink1ml = itemView.findViewById(R.id.redrinkml1);
            Redrink2ml = itemView.findViewById(R.id.redrinkml2);
            Redrink3ml = itemView.findViewById(R.id.redrinkml3);
            Redrink4ml = itemView.findViewById(R.id.redrinkml4);
            Redrink5ml = itemView.findViewById(R.id.redrinkml5);
            Redrink6ml = itemView.findViewById(R.id.redrinkml6);
            Rename = itemView.findViewById(R.id.redrinkname);
            Image = itemView.findViewById(R.id.image);
            cup = itemView.findViewById(R.id.cup);
        }


    }
}
