package com.example.wifitest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class reAdapter extends FirestoreRecyclerAdapter<reuser,reAdapter.reHolder> {

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
        holder.Redrink1ml.setText(model.getRedrink1ml());
        holder.Redrink2ml.setText(model.getRedrink2ml());
        holder.Redrink3ml.setText(model.getRedrink3ml());
        holder.Redrink4ml.setText(model.getRedrink4ml());
        holder.Redrink5ml.setText(model.getRedrink5ml());
        holder.Redrink6ml.setText(model.getRedrink6ml());
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

        TextView Redrink1,Redrink2,Redrink3,Redrink4,Redrink5,Redrink6;
        TextView Redrink1ml,Redrink2ml,Redrink3ml,Redrink4ml,Redrink5ml,Redrink6ml;
        TextView Rename;

        public reHolder(@NonNull View itemView) {
            super(itemView);
            Redrink1 = itemView.findViewById(R.id.redrink1);
            Redrink2 = itemView.findViewById(R.id.redrink2);
            Redrink3 = itemView.findViewById(R.id.redrink3);
            Redrink4 = itemView.findViewById(R.id.redrink4);
            Redrink5 = itemView.findViewById(R.id.redrink5);
            Redrink6 = itemView.findViewById(R.id.redrink6);
            Redrink1ml = itemView.findViewById(R.id.redrinkml1);
            Redrink2ml = itemView.findViewById(R.id.redrinkml2);
            Redrink3ml = itemView.findViewById(R.id.redrinkml3);
            Redrink4ml = itemView.findViewById(R.id.redrinkml4);
            Redrink5ml = itemView.findViewById(R.id.redrinkml5);
            Redrink6ml = itemView.findViewById(R.id.redrinkml6);
            Rename = itemView.findViewById(R.id.redrinkname);
        }
    }
}
