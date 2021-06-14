package com.example.i_queue;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import androidx.core.app.ActivityCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.i_queue.models.Respuesta;
import com.example.i_queue.webservice.WebServiceClient;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PreferencesFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Retrofit retrofit;
    private HttpLoggingInterceptor loggingInterceptor;
    private OkHttpClient.Builder httpClientBuilder;
    private int id_user;
    private String token;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        preferences = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        editor = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE).edit();
        id_user = preferences.getInt("id_user", 0);
        token = preferences.getString("token", "");

        Preference myPref2 = (Preference) findPreference("pref_open_service_key2");
        myPref2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Eliminar Cuenta");
                builder.setMessage("¿Seguro que desea eliminar su cuenta?");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().onBackPressed();
                        lanzarPeticion("Bearer "+token, String.valueOf(id_user));
                        ActivityCompat.finishAffinity(getActivity());
                    }
                });

                builder.setNegativeButton("NO", null);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return false;
            }
        });

        Preference myPref3 = (Preference) findPreference("pref_open_service_key3");
        myPref3.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                editor.putString("user_login" , "");
                editor.putString("user_pass" , "");
                editor.apply();

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Cerrar sesión");
                builder.setMessage("¿Seguro que desea cerrar sesión?");

                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().onBackPressed();
                        startActivity(new Intent(getContext(), LoginActivity.class));
                        ActivityCompat.finishAffinity(getActivity());
                    }
                });

                builder.setNegativeButton("No", null);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();


                return false;
            }
        });
    }

    private void lanzarPeticion(String token, String user_id){
        loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder = new OkHttpClient.Builder().addInterceptor(loggingInterceptor);

        retrofit = new Retrofit.Builder()
                .baseUrl(WebServiceClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClientBuilder.build())
                .build();
        WebServiceClient client = retrofit.create(WebServiceClient.class);

        Call<Respuesta> llamada = client.deleteUser(token, user_id);
        llamada.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                editor.putString("user_login" , "");
                editor.putString("user_pass" , "");
                editor.apply();
                startActivity(new Intent(getContext(), LoginActivity.class));
                Toast.makeText(getActivity(), "Tu cuenta se ha borrado correctamente", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {
                Toast.makeText(getActivity(), "Hubo un fallo al borra el usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
    }



}

