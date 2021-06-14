package com.example.i_queue;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.i_queue.models.Respuesta;
import com.example.i_queue.webservice.WebServiceClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainPageActivity extends AppCompatActivity {

    private ImageView settings;
    private FloatingActionButton qr_button;
    private Retrofit retrofit;
    private String token, password_verification, queue_id;
    private HttpLoggingInterceptor loggingInterceptor;
    private OkHttpClient.Builder httpClientBuilder;
    private Queue_Adapter adapter;
    private Respuesta respuesta;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage_layout);
        settings = findViewById(R.id.settings);
        qr_button = findViewById(R.id.qr);
        Toolbar toolbar = findViewById(R.id.toolbar_2);

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("white"));

        SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPageActivity.this, Settings_class.class);
                startActivity(intent);
            }
        });

        qr_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                final int MY_CAMERA_REQUEST_CODE = 100;
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                } else {
                    finish();
                    Intent myIntent = new Intent(MainPageActivity.this, QrCodeScanner.class);
                    MainPageActivity.this.startActivity(myIntent);
                }
            }
        });

        Intent intent = getIntent();
        String qrCode = intent.getStringExtra("readedData");
        if(qrCode != null){
            JSONObject reader;
            try {
                reader = new JSONObject(qrCode);
                Log.d("reader", reader.getString("commerce_id"));
                password_verification = reader.getString("password_verification");
                queue_id = reader.getString("commerce_id");
                lanzarPeticion("Bearer " + token, password_verification, queue_id);
                qrCode = null;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                navigateToFragment(id);
                return true;
            }
        });

        navigateToFragment(R.id.Queue);

    }

    private void lanzarPeticion(String token , String password_verification , String queue_id){
        loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder = new OkHttpClient.Builder().addInterceptor(loggingInterceptor);

        retrofit = new Retrofit.Builder()
                .baseUrl(WebServiceClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClientBuilder.build())
                .build();
        WebServiceClient client = retrofit.create(WebServiceClient.class);

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("password_verification", password_verification);
        hashMap.put("queue_id", queue_id);

        Call<Respuesta> llamada = client.enterQueue(token, hashMap);
        llamada.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
               if(response.isSuccessful()){
                   respuesta = response.body();
               }
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {
                Toast.makeText(MainPageActivity.this, "Hubo un fallo al entrar en la cola", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            cerrarAplicacion();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void cerrarAplicacion() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.propuestalogo)
                .setTitle("¿Realmente desea cerrar la aplicación?")
                .setCancelable(false)
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }


    private void navigateToFragment(int itemId){

        Fragment fragment;
        String title = "i-Queue";

        switch (itemId){
            default:
                setTitle("Queue");
                fragment = new Fragment_library();
                break;
            case R.id.Queue:
                setTitle("Queue");
                fragment = new Fragment_queue();
                break;
            case R.id.Tiendas:
                setTitle("Tiendas");
                fragment = new Fragment_library();
                break;
            case R.id.Mapa:
                setTitle("Mapa");
                fragment = new Fragment_map();
                break;
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();

        setTitle(title);
    }
}
