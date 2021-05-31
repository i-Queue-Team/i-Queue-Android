package com.example.i_queue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.i_queue.models.Data;

import java.util.List;


public class Adapter_Library extends RecyclerView.Adapter<Adapter_Library.LibraryHolder> {

    private List<Data> commerce;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Data item);
    }

    public Adapter_Library(List<Data> commerce, Context context, OnItemClickListener listener) {
        this.commerce = commerce;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public LibraryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_detail,parent , false);
        LibraryHolder holder = new LibraryHolder(itemview);
        return holder;
    }

    @Override
    public void onBindViewHolder(LibraryHolder holder, int position) {
        Data commerce = this.commerce.get(position);
        holder.id.setText(String.valueOf(commerce.getId()));
        holder.name.setText(commerce.getName());
        holder.pulsar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(commerce);
            }
        });
    }

    @Override
    public int getItemCount() {
            return commerce.size();
    }

    public void setComercesList(List<Data> comerces){
        this.commerce = comerces;
        notifyDataSetChanged();
    }

    static class LibraryHolder extends RecyclerView.ViewHolder{

        TextView id,name;
        LinearLayout pulsar;

        public LibraryHolder(View v){
            super(v);
            id = v.findViewById(R.id.number_detail_list);
            name = v.findViewById(R.id.name_shop_detail_list);
            pulsar = v.findViewById(R.id.pulsar);

        }
    }


}
