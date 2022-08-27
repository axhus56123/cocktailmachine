package com.example.wifitest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

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
        holder.lovedrink1.setText(model.getLovedrink1()+":");
        holder.lovedrink2.setText(model.getLovedrink2()+":");
        holder.lovedrink3.setText(model.getLovedrink3()+":");
        holder.lovedrink1ml.setText(model.getLovedrink1ml()+"ml");
        holder.lovedrink2ml.setText(model.getLovedrink2ml()+"ml");
        holder.lovedrink3ml.setText(model.getLovedrink3ml()+"ml");
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

        TextView lovename,lovedrink1,lovedrink2,lovedrink3,lovedrink1ml,lovedrink2ml,lovedrink3ml;

        public loveholder(@NonNull View itemView) {
            super(itemView);
            lovename = itemView.findViewById(R.id.lovename);
            lovedrink1ml = itemView.findViewById(R.id.lovedrink1ml);
            lovedrink2ml = itemView.findViewById(R.id.lovedrink2ml);
            lovedrink3ml = itemView.findViewById(R.id.lovedrink3ml);
            lovedrink1 = itemView.findViewById(R.id.lovedrink1);
            lovedrink2 = itemView.findViewById(R.id.lovedrink2);
            lovedrink3 = itemView.findViewById(R.id.lovedrink3);
        }
    }
}
