package com.example.i_queue;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.i_queue.models.Data;
import com.example.i_queue.models.Data_Queue;
import com.example.i_queue.models.Queue;
import com.example.i_queue.models.Respuesta;
import com.example.i_queue.models.Respuesta_Queue;
import com.example.i_queue.webservice.WebServiceClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Queue_Adapter extends RecyclerView.Adapter<Queue_Adapter.QueueHolder>{

    private List<Queue> queue;
    private Context context;
    private Retrofit retrofit;
    private String token;
    private HttpLoggingInterceptor loggingInterceptor;
    private OkHttpClient.Builder httpClientBuilder;

    public Queue_Adapter(List<Queue> queue, Context context) {
        this.queue = queue;
        this.context = context;
    }

    @Override
    public Queue_Adapter.QueueHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_queue,parent ,false);
        Queue_Adapter.QueueHolder holder = new Queue_Adapter.QueueHolder(itemview);
        return holder;
    }

    @Override
    public void onBindViewHolder(Queue_Adapter.QueueHolder holder, int position) {
        SharedPreferences preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");

        holder.title_card.setText(queue.get(position).getName());

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
        try {
            Date date = fmt.parse(queue.get(position).getEstimated_time());
            Log.d("date" , String.valueOf(date));
            SimpleDateFormat fmtOut = new SimpleDateFormat("H:mm:ss");
            Log.d("date" , fmtOut.format(date));
            holder.time_count.setText(fmtOut.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Picasso.get().load(queue.get(position).getImage()).into(holder.image_queue);
        holder.delete_queue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("¿Desea salir de la cola?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteQueue("Bearer " + token, String.valueOf(queue.get(position).getQueue_id()),new Async() {
                                    @Override
                                    public void Asyncs(Respuesta respuesta) {
                                        if(respuesta.getCode() == 200){
                                            Toast.makeText(context, "Saliste", Toast.LENGTH_SHORT).show();
                                            queue.remove(position);
                                            notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", null)
                        .create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return queue.size();
    }

    public void setQueue(List<Queue> queue) {
        this.queue = queue;
        notifyDataSetChanged();
    }

    private interface Async{
        void Asyncs (Respuesta respuesta);
    }
    
    private void DeleteQueue(String token, String id ,Async async){
        loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder = new OkHttpClient.Builder().addInterceptor(loggingInterceptor);

        retrofit = new Retrofit.Builder()
                .baseUrl(WebServiceClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClientBuilder.build())
                .build();
        WebServiceClient client = retrofit.create(WebServiceClient.class);
        Call<Respuesta> llamada = client.deleteQueue(token, id);
        llamada.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                if(response.isSuccessful()){
                    async.Asyncs(response.body());
                }
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {

            }
        });
    }
    
    static class QueueHolder extends RecyclerView.ViewHolder{

        TextView title_card, time_count;
        MaterialButton delete_queue;
        ImageView image_queue;
        MaterialCardView constraintLayout;

        public QueueHolder(View v){
            super(v);
            image_queue = v.findViewById(R.id.image_queue);
            title_card = v.findViewById(R.id.title_card);
            time_count = v.findViewById(R.id.time_count);
            delete_queue = v.findViewById(R.id.delete_queue);
            constraintLayout = v.findViewById(R.id.card);
        }

    }
}


