package com.example.i_queue;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.i_queue.models.Data;
import com.example.i_queue.models.Queue;
import com.squareup.picasso.Picasso;

import java.util.List;


public class Adapter_Library extends RecyclerView.Adapter<Adapter_Library.LibraryHolder> {

    private List<Data> commerce;
    private Context context;

    public Adapter_Library(List<Data> commerce, Context context) {
        this.commerce = commerce;
        this.context = context;
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
        SpannableString texto = new SpannableString(commerce.getName());
        texto.setSpan(new UnderlineSpan(), 0, texto.length(), 0);
        holder.name.setText(texto);
        Picasso.get().load(commerce.getImage()).into(holder.imageView);
        holder.address.setText(commerce.getAddress());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StoreActivity.class);
                Bundle extras = new Bundle();
                extras.putString("name", commerce.getName());
                extras.putString("description", commerce.getInfo());
                extras.putString("image", commerce.getImage());
                extras.putString("latitude", commerce.getLatitude().toString());
                extras.putString("longitude", commerce.getLongitude().toString());
                extras.putString("address", commerce.getAddress());
                intent.putExtras(extras);
                context.startActivity(intent);
            }
        });
        holder.pulsar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StoreActivity.class);
                Bundle extras = new Bundle();
                extras.putString("name", commerce.getName());
                extras.putString("description", commerce.getInfo());
                extras.putString("image", commerce.getImage());
                extras.putString("latitude", commerce.getLatitude().toString());
                extras.putString("address", commerce.getAddress());
                extras.putString("longitude", commerce.getLongitude().toString());
                intent.putExtras(extras);
                context.startActivity(intent);
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

        TextView name, address;
        ConstraintLayout pulsar;
        ImageView imageView;

        public LibraryHolder(View v){
            super(v);
            name = v.findViewById(R.id.name_shop_detail_list);
            pulsar = v.findViewById(R.id.pulsar);
            imageView = v.findViewById(R.id.image_shop);
            address = v.findViewById(R.id.address_detail);
        }
    }


}
