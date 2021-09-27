package com.example.sagarcloths;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class MyAdapter extends FirebaseRecyclerAdapter<Model,MyAdapter.holder> {


    public MyAdapter(@NonNull FirebaseRecyclerOptions<Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull holder holder, int position, @NonNull Model model) {


        holder.detail.setText(model.getName());
        holder.price.setText("Price "+String.valueOf(model.getPrice()));
        holder.send.setBackgroundResource(R.drawable.ic_baseline_send_24);
        Glide.with(holder.homePic.getContext()).load(model.getPurl()).into(holder.homePic);
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_data,parent,false);
        return new holder(view);
    }

    public static class holder extends RecyclerView.ViewHolder{
                  ImageView homePic,send;
                    TextView price,detail;
        public holder(@NonNull View itemView) {
            super(itemView);
            homePic=(ImageView)itemView.findViewById(R.id.homePic);
            price=(TextView) itemView.findViewById(R.id.price);
            detail=(TextView) itemView.findViewById(R.id.detail);
            send=(ImageView) itemView.findViewById(R.id.send);
        }
    }


}
