package com.example.i_queue;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.i_queue.models.Data;
import com.example.i_queue.models.Data_Queue;
import com.example.i_queue.models.Queue;
import com.example.i_queue.models.Respuesta;
import com.example.i_queue.models.Respuesta_Library;
import com.example.i_queue.models.Respuesta_Queue;
import com.example.i_queue.webservice.WebServiceClient;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_queue extends Fragment {

    private RecyclerView recyclerView;
    private Retrofit retrofit;
    private Queue_Adapter adapter;
    private HttpLoggingInterceptor loggingInterceptor;
    private OkHttpClient.Builder httpClientBuilder;
    private GridLayoutManager layoutManager;
    private List<Queue> queueList;
    private String token;
    private Runnable mTicker;
    private ProgressBar progressBar;
    private TextView esconder_2;
    private ImageView esconder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
        token = prefs.getString("token", "");
        View view = inflater.inflate(R.layout.frag_queue, container, false);
        queueList = new ArrayList<>();
        esconder_2 = view.findViewById(R.id.esconder_2);
        esconder = view.findViewById(R.id.esconder);
        progressBar = view.findViewById(R.id.progressbar_queue);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView = view.findViewById(R.id.recycler_queue);
        layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Queue_Adapter(queueList, getActivity());
        recyclerView.setAdapter(adapter);
        GetQueue("Bearer " + token);

//            final Handler mHandler = new Handler();
//            mTicker = new Runnable() {
//                @Override
//                public void run() {
//                    queueList = new ArrayList<>();
//                    GetQueue("Bearer " + token);
//                    mHandler.postDelayed(mTicker, 10000);
//                }
//            };
//            mHandler.postDelayed(mTicker, 10000);

        return view;
    }

    private void GetQueue(String token) {

        loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder = new OkHttpClient.Builder().addInterceptor(loggingInterceptor);

        retrofit = new Retrofit.Builder()
                .baseUrl(WebServiceClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClientBuilder.build())
                .build();
        WebServiceClient client = retrofit.create(WebServiceClient.class);
        Call<Respuesta_Queue> llamada = client.getQueue(token);
        llamada.enqueue(new Callback<Respuesta_Queue>() {
            @Override
            public void onResponse(Call<Respuesta_Queue> call, Response<Respuesta_Queue> response) {
                if(response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Respuesta_Queue respuesta = response.body();
                    JsonArray array = new JsonArray();
                    if(respuesta.getData().equals(array)){
                        esconder_2.setVisibility(View.VISIBLE);
                        esconder.setVisibility(View.VISIBLE);

                        int code = respuesta.getCode();
                        if(code == 200){
                            for (JsonElement json: respuesta.getData()) {
                                Gson gson = new Gson();
                                Queue queue = gson.fromJson(json, Queue.class);
                                queueList.add(queue);
                            }
                            adapter.setQueue(queueList);

                        }else {
                            Toast.makeText(getActivity(), "Hubo un error", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        esconder_2.setVisibility(View.GONE);
                        esconder.setVisibility(View.GONE);
                        int code = respuesta.getCode();
                        if(code == 200){
                            for (JsonElement json: respuesta.getData()) {
                                Gson gson = new Gson();
                                Queue queue = gson.fromJson(json, Queue.class);
                                queueList.add(queue);
                            }
                            adapter.setQueue(queueList);

                        }else {
                            Toast.makeText(getActivity(), "Hubo un error", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<Respuesta_Queue> call, Throwable t) {
                Log.d("call" , t.getMessage());
            }
        });

    }
}





