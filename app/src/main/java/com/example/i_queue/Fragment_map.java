package com.example.i_queue;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.i_queue.models.Data;
import com.example.i_queue.models.Respuesta_Library;
import com.example.i_queue.webservice.WebServiceClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_map extends Fragment implements OnMapReadyCallback{

    private GoogleMap map;
    private List<Data> dataList;
    private Retrofit retrofit;
    private HttpLoggingInterceptor loggingInterceptor;
    private OkHttpClient.Builder httpClientBuilder;
    final LatLng linares = new LatLng(38.099922, -3.631077);
    private String token;
    private ProgressBar progressBar;

    private interface Async{
        void response(List<Data> respuesta);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_map, container, false);

        progressBar = v.findViewById(R.id.progressBar_map);
        progressBar.setVisibility(View.VISIBLE);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
        token = prefs.getString("token", "");

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        progressBar.setVisibility(View.GONE);

        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setCompassEnabled(true);

        lanzarPeticion("Bearer " + token, new Async() {
            @Override
            public void response(List<Data> respuesta) {

                for(int i = 0; i < dataList.size(); i++){
                    Data data = dataList.get(i);

                    MarkerOptions options = new MarkerOptions();
                    final LatLng sitio = new LatLng(data.getLatitude(), data.getLongitude());
                    options.position(sitio);
                    options.title(data.getName());
                    options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                    options.snippet("Holas");
                    options.snippet(data.getAddress());
                    map.addMarker(options).setTag(i);
                }

                map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        for(int y = 0; y < dataList.size(); y++){
                            Data data = dataList.get(y);
                            if(marker.getTag().equals(y)){
                                Intent intent = new Intent(getActivity(), StoreActivity.class);
                                intent.putExtra("name", data.getName());
                                intent.putExtra("name", data.getName());
                                intent.putExtra("address", data.getAddress());
                                intent.putExtra("description", data.getInfo());
                                intent.putExtra("image", data.getImage());
                                intent.putExtra("latitude", data.getLatitude().toString());
                                intent.putExtra("longitude", data.getLongitude().toString());
                                startActivity(intent);
                            }
                        }
                    }
                });

                map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(linares, 14));
                    }
                });
            }
        });
    }

    private void lanzarPeticion(String token, Async callback) {
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
                dataList = response.body().getData();
                if(response.isSuccessful()){
                    callback.response(dataList);
                }else{
                    Toast.makeText(getActivity(), "La carga del Mapa ha fallado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Respuesta_Library> call, Throwable t) {
                Toast.makeText(getActivity(), "La carga del Mapa ha fallado", Toast.LENGTH_SHORT).show();
            }
        });



    }
}
