package com.example.i_queue;

import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.example.i_queue.models.Queue;
import com.example.i_queue.models.Respuesta_Queue;
import com.example.i_queue.webservice.WebServiceClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

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
    private Adapter_Queue adapter;
    private HttpLoggingInterceptor loggingInterceptor;
    private OkHttpClient.Builder httpClientBuilder;
    private GridLayoutManager layoutManager;
    private List<Queue> queueList;
    private String token;
    private ProgressBar progressBar;
    private TextView esconder_2, esconder_3;
    private ImageView esconder;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_queue, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        esconder_2 = view.findViewById(R.id.esconder_2);
        esconder_3 = view.findViewById(R.id.esconder_3);
        esconder = view.findViewById(R.id.esconder);
        progressBar = view.findViewById(R.id.progressbar_queue);

        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
        token = prefs.getString("token", "");

        queueList = new ArrayList<>();

        progressBar.setVisibility(View.VISIBLE);
        recyclerView = view.findViewById(R.id.recycler_view);
        layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter_Queue(queueList, getActivity());
        recyclerView.setAdapter(adapter);
        GetQueue("Bearer " + token);
        esconder.setVisibility(View.GONE);
        esconder_2.setVisibility(View.GONE);
        esconder_3.setVisibility(View.GONE);

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
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Respuesta_Queue respuesta = response.body();
                    int code = respuesta.getCode();
                    if (code == 200) {
                        for (JsonElement json : respuesta.getData()) {
                            Gson gson = new Gson();
                            Queue queue = gson.fromJson(json, Queue.class);
                            queueList.add(queue);
                        }
                        if(queueList.size() < 1){
                            esconder_3.setVisibility(View.VISIBLE);
                            esconder.setVisibility(View.VISIBLE);
                            esconder_2.setVisibility(View.VISIBLE);
                        }else{
                            esconder_3.setVisibility(View.GONE);
                            esconder.setVisibility(View.GONE);
                            esconder_2.setVisibility(View.GONE);
                        }
                        adapter.setQueue(queueList);
                    } else {
                        Toast.makeText(getActivity(), "No se pudieron obtener las colas actuales", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Respuesta_Queue> call, Throwable t) {
                Log.d("call" , t.getMessage());
                Toast.makeText(getContext(), "No se pudieron obtener las colas actuales", Toast.LENGTH_SHORT).show();
            }
        });

    }
}





