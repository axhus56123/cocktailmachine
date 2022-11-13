package com.example.wifitest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.wifitest.Adpter.PostAdpter;
import com.example.wifitest.model.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Blog extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;
    private PostAdpter adpter;
    private List<Post> list;
    private Query query;
    private ListenerRegistration listenerRegistration;
    private ImageButton back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        back = findViewById(R.id.blogBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Blog.this,bottomOption.class);
                startActivity(intent);
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        fab = findViewById(R.id.blogAdd);
        mRecyclerView = findViewById(R.id.blogview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(Blog.this));

        list = new ArrayList<>();
        adpter = new PostAdpter(Blog.this ,list);
        mRecyclerView.setAdapter(adpter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Blog.this , AddPost.class));
            }
        });

        if (firebaseAuth.getCurrentUser() != null){

            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    Boolean isBotttom = !mRecyclerView.canScrollVertically(1);
                    if(isBotttom)
                        Toast.makeText(Blog.this,"Reached Bottom",Toast.LENGTH_SHORT);
                }
            });
            query = firestore.collection("Posts").orderBy("time" , Query.Direction.DESCENDING);
            listenerRegistration = query.addSnapshotListener(Blog.this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    for(DocumentChange doc : value.getDocumentChanges()){
                        if(doc.getType() == DocumentChange.Type.ADDED){
                            String postId = doc.getDocument().getId();
                            Post post = doc.getDocument().toObject(Post.class).withId(postId);
                            String postUserId = doc.getDocument().getString("user");
                            list.add(post);
                            adpter.notifyDataSetChanged();
                        }else{
                            adpter.notifyDataSetChanged();
                        }
                    }
                    listenerRegistration.remove();
                }
            });
        }
    }
}
