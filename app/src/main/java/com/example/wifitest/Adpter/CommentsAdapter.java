package com.example.wifitest.Adpter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wifitest.CommentsActivity;
import com.example.wifitest.R;
import com.example.wifitest.model.Comments;
import com.example.wifitest.model.Post;
import com.example.wifitest.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;



public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {

    private Activity context;
    private List<Users> usersList;
    private List<Comments> commentsList;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    public CommentsAdapter(Activity context , List<Comments> commentsList, List<Users> usersList){
        this.context = context;
        this.commentsList = commentsList;
        this.usersList = usersList;
    }
    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.each_comment , parent , false);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Comments comments = commentsList.get(position);
        holder.setmComment(comments.getComment());

        Users users = usersList.get(position);
        holder.setmUserName(users.getName());
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder{
        TextView mComment , mUserName;
        ImageButton deleteBtn;
        View mView;
        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            deleteBtn= mView.findViewById(R.id.delete_comment);
        }
        public void setmComment(String comment){
            mComment = mView.findViewById(R.id.comment_tv);
            mComment.setText(comment);
        }
        public void setmUserName(String userName){
            mUserName = mView.findViewById(R.id.comment_user);
            mUserName.setText(userName);
        }

    }
}
