package com.example.wifitest.Adpter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wifitest.CommentsActivity;
import com.example.wifitest.R;
import com.example.wifitest.model.Post;
import com.example.wifitest.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostAdpter extends RecyclerView.Adapter<PostAdpter.PostViewHolder> {

    private List<Post> mList;
    private List<Users> usersList;
    private Activity context;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    public PostAdpter(Activity context, List<Post> mList) {
        this.mList = mList;
        this.context = context;
        this.usersList = usersList;
    }


    @NonNull
    @Override
    public PostAdpter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.each_post , parent , false);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdpter.PostViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Post post = mList.get(position);
        holder.setPostCaption(post.getCaption());
        holder.setPostPic(post.getImage());
        long milliseconds = post.getTime().getTime();
        String date  = DateFormat.format("yyyy/MM/dd" , new Date(milliseconds)).toString();
        holder.setPostDate(date);

        //String username = usersList.get(position).getName();
        //String image = usersList.get(position).getImage();
        String userID = post.getUser();
        firestore.collection("user").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String username = task.getResult().getString("name");

                    holder.setPostUsername(username);
                }else{
                    Toast.makeText(context,task.getException().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        String postId = post.PostId;
        String currentUserId = auth.getCurrentUser().getUid();
        holder.likePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("Posts/" + postId + "/Likes").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.getResult().exists()){
                            Map<String , Object> likesMap = new HashMap<>();
                            likesMap.put("timestamp" , FieldValue.serverTimestamp());
                            firestore.collection("Posts/" + postId + "/Likes").document(currentUserId).set(likesMap);
                        }else{
                            firestore.collection("Posts/" + postId + "/Likes").document(currentUserId).delete();
                        }
                    }
                });
            }
        });

        //like color change
        firestore.collection("Posts/" + postId + "/Likes").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null){
                    if (value.exists()){
                        holder.likePic.setImageDrawable(context.getDrawable(R.drawable.after_liked));
                    }else{
                        holder.likePic.setImageDrawable(context.getDrawable(R.drawable.before_liked));
                    }
                }
            }
        });
        //likes count
        firestore.collection("Posts/" + postId + "/Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null){
                    if (!value.isEmpty()){
                        int count = value.size();
                        holder.setPostLikes(count);
                    }else{
                        holder.setPostLikes(0);
                    }
                }
            }
        });
        //comments implementation
        holder.commentsPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentIntent = new Intent(context , CommentsActivity.class);
                commentIntent.putExtra("postid" , postId);
                context.startActivity(commentIntent);
            }
        });

        if (currentUserId.equals(post.getUser())){
            holder.deleteBtn.setVisibility(View.VISIBLE);
            holder.deleteBtn.setClickable(true);
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Delete")
                            .setMessage("Are You Sure ?")
                            .setNegativeButton("No" , null)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    firestore.collection("Posts/" + postId + "/Comments").get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    for (QueryDocumentSnapshot snapshot : task.getResult()){
                                                        firestore.collection("Posts/" + postId + "/Comments").document(snapshot.getId()).delete();
                                                    }
                                                }
                                            });
                                    firestore.collection("Posts/" + postId + "/Likes").get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    for (QueryDocumentSnapshot snapshot : task.getResult()){
                                                        firestore.collection("Posts/" + postId + "/Likes").document(snapshot.getId()).delete();
                                                    }
                                                }
                                            });
                                    firestore.collection("Posts").document(postId).delete();
                                    mList.remove(position);
                                    notifyDataSetChanged();
                                }
                            });
                    alert.show();
                }
            });
        }

    }




    @Override
    public int getItemCount() {
        return mList.size();
    }

    public  class PostViewHolder extends RecyclerView.ViewHolder{
        ImageView commentsPic , likePic,postPic;
        TextView postUsername , postDate , postCaption , postLikes;
        ImageButton deleteBtn;
        View mView;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            likePic = mView.findViewById(R.id.like_btn);
            commentsPic = mView.findViewById(R.id.comments_post);
            deleteBtn= mView.findViewById(R.id.delete_btn);
        }
        public void setPostPic(String urlPost){
            postPic = mView.findViewById(R.id.userPost);
            Glide.with(context).load(urlPost).into(postPic);
        }
        public void setPostLikes(int count){
            postLikes = mView.findViewById(R.id.like_count_tv);
            postLikes.setText(count + " Likes");
        }
        public void setPostUsername(String username){
            postUsername = mView.findViewById(R.id.postUser);
            postUsername.setText(username);
        }
        public void setPostDate(String date){
            postDate = mView.findViewById(R.id.postdate);
            postDate.setText(date);
        }
        public void setPostCaption(String caption){
            postCaption = mView.findViewById(R.id.caption);
            postCaption.setText(caption);
        }

    }
}
