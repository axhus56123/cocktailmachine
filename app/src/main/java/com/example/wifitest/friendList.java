package com.example.wifitest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.wifitest.model.Friend;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class friendList extends AppCompatActivity {
    FirebaseAuth mAuth;
    static FirebaseFirestore db;
    private FloatingActionButton add;
    private ImageView back;
    private String uid;
    //private EditText search;
    private RecyclerView rvFriend;
    private LinearLayoutManager mLayoutManger;
    private FirestoreRecyclerAdapter<Friend, FriendViewHolder> adapter;


    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProgressDialog pd = new ProgressDialog(friendList.this);
        setContentView(R.layout.activity_friend_list);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        add = findViewById(R.id.fabadd);
        rvFriend = findViewById(R.id.rvFriend);
        back = findViewById(R.id.friendback);
        //search = findViewById(R.id.search);

        mLayoutManger = new LinearLayoutManager(this);
        mLayoutManger.setReverseLayout(true);
        mLayoutManger.setStackFromEnd(true);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(friendList.this,bottomOption.class);
                startActivity(intent);
            }
        });


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvFriend.getContext(), mLayoutManger.getOrientation());
        rvFriend.addItemDecoration(dividerItemDecoration);
        rvFriend.setHasFixedSize(true);
        rvFriend.setLayoutManager(mLayoutManger);

        FirestoreRecyclerOptions<Friend> options = new FirestoreRecyclerOptions.Builder<Friend>()
                .setQuery(db.collection("user").document(uid).collection("friend"),Friend.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Friend, FriendViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FriendViewHolder holder, int position, @NonNull Friend model) {
                String uidFriend = getSnapshots().getSnapshot(position).getId();
                holder.setList(uidFriend);


                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goChatRoom(model.getIdChatRoom(),uidFriend);
                    }
                });

            }
            
            @NonNull
            @Override
            public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_friend, parent,false);
                return new FriendViewHolder(view);
                
            }
            public void deleteitem(int position){
                getSnapshots().getSnapshot(position).getReference().delete();
            }


        };

        /*new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteitem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(rvFriend);*/

        rvFriend.setAdapter(adapter);
        adapter.startListening();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(friendList.this);
                dialog.setTitle("Enter User UID");
                dialog.setContentView(R.layout.dialog_add);
                dialog.show();

                EditText edtid = dialog.findViewById(R.id.edtID);
                Button btnok = dialog.findViewById(R.id.btnOK);
                btnok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String iduser = edtid.getText().toString();
                        if(TextUtils.isEmpty(iduser)){
                            edtid.setError("required");
                        }else{
                            db.collection("user").whereEqualTo("id",iduser).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if(queryDocumentSnapshots.isEmpty()){
                                        edtid.setError("ID not found");
                                    }else {
                                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                            String uidfriend = documentSnapshot.getId();
                                            if(uid.equals(uidfriend)){
                                                edtid.setError("wrong ID");
                                            }else{
                                                dialog.cancel();
                                                checkFriendExist(uidfriend);
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }



    public static class FriendViewHolder extends RecyclerView.ViewHolder{
        View mView;
        ImageView imgProfile;
        TextView txtName;
        public FriendViewHolder(View itemView){
            super(itemView);
            mView = itemView;
            imgProfile = mView.findViewById(R.id.imgProfile);
            txtName = mView.findViewById(R.id.txtName);

        }
        public void setList(String uidFriend){
            db.collection("user").document(uidFriend).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if(documentSnapshot.exists()){
                            String name = documentSnapshot.get("name",String.class);
                            txtName.setText(name);
                        }
                    }
                }
            });
        }
    }

    private void checkFriendExist(String uidFriend){
        db.collection("user").document(uid).collection("friend").document().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        String idChatroom = documentSnapshot.get("idChatroom",String.class);
                        goChatRoom(idChatroom,uidFriend);
                    }else{
                        createChatRoom(uidFriend);
                    }
                }
            }
        });
    }

    private void  goChatRoom(String idChatroom,String uidFriend){
        Intent intent = new Intent(friendList.this, ChatActivity.class);
        intent.putExtra("idChatRoom",idChatroom);
        intent.putExtra("uidFriend",uidFriend);
        startActivity(intent);
    }
    private void  createChatRoom(String uidFriend){
        HashMap<String,Object> dataChatRoom = new HashMap<>();
        dataChatRoom.put("dataAdded", FieldValue.serverTimestamp());
        db.collection("chatRoom").document(uid+uidFriend).set(dataChatRoom).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //write user data
                HashMap<String,Object> dataFriend = new HashMap<>();
                dataFriend.put("idChatRoom",uid+uidFriend);
                db.collection("user").document(uid).collection("friend").document(uidFriend).set(dataFriend).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //wrire on users friend data
                        HashMap<String,Object> dataUserFriend = new HashMap<>();
                        dataUserFriend.put("idChatRoom",uid+uidFriend);
                        db.collection("user").document(uidFriend).collection("friend").document(uid).set(dataUserFriend).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                goChatRoom(uid+uidFriend,uidFriend);
                            }
                        });
                    }
                });
            }
        });
    }
}