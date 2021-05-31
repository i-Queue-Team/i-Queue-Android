package com.example.i_queue;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.i_queue.models.Data;
import com.example.i_queue.models.Respuesta_Library;
import com.example.i_queue.webservice.WebServiceClient;

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

public class Fragment_library extends Fragment {

    private RecyclerView recyclerView;
    private Adapter_Library adapter;
    private List<Data> comercesList;
    private Retrofit retrofit;
    private HttpLoggingInterceptor loggingInterceptor;
    private OkHttpClient.Builder httpClientBuilder;
    private String token;
    private GridLayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
        token = prefs.getString("token", "");
        View view = inflater.inflate(R.layout.frag_library, container, false);
        comercesList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerview_library);
        layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter_Library(comercesList, getActivity(), new Adapter_Library.OnItemClickListener() {
            @Override
            public void onItemClick(Data data) {
                Intent intent = new Intent(getActivity(), StoreActivity.class);
                Bundle extras = new Bundle();
                extras.putString("name", data.getName());
                intent.putExtras(extras);
                startActivityForResult(intent, 1);
            }
        });
        recyclerView.setAdapter(adapter);
        lanzarPeticion("Bearer "+token);
        return view;
    }

    private void lanzarPeticion(String token){
        loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder = new OkHttpClient.Builder().addInterceptor(loggingInterceptor);

        retrofit = new Retrofit.Builder()
                .baseUrl(WebServiceClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClientBuilder.build())
                .build();
        WebServiceClient client = retrofit.create(WebServiceClient.class);
        Call<Respuesta_Library> llamada = client.getShops(token);
        llamada.enqueue(new Callback<Respuesta_Library>() {
            @Override
            public void onResponse(Call<Respuesta_Library> call, Response<Respuesta_Library> response) {
                if(response.isSuccessful()){
                    Respuesta_Library respuesta = response.body();
                    int code = respuesta.getCode();
                    if(code == 200){
                        comercesList = respuesta.getData();
                        adapter.setComercesList(comercesList);
                    }else{
                        Toast.makeText(getActivity(), "Hubo un error", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Respuesta_Library> call, Throwable t) {
                Toast.makeText(getActivity(), "Hubo un error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
