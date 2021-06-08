package com.example.i_queue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.i_queue.models.Respuesta;
import com.example.i_queue.models.User_Login;
import com.example.i_queue.webservice.WebServiceClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText email_field, pass_field;
    private TextInputLayout pass_field_textinputext;
    private String email_text, pass_text, token;
    private int id_user;
    private Button login, register_login;
    private ProgressBar progressBar;
    private Retrofit retrofit;
    private TextView olvida;
    private HttpLoggingInterceptor loggingInterceptor;
    private OkHttpClient.Builder httpClientBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("i-Queue");
        SharedPreferences.Editor editor = getSharedPreferences("prefs", MODE_PRIVATE).edit();
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        email_field = findViewById(R.id.email_log);
        progressBar = findViewById(R.id.progressBar);
        olvida = findViewById(R.id.olvida);
        pass_field_textinputext = findViewById(R.id.pass_input);
        pass_field = findViewById(R.id.password_log);
        login = findViewById(R.id.login);
        register_login = findViewById(R.id.register_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_text = email_field.getText().toString();
                pass_text = pass_field.getText().toString();

                if(email_text.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Introduce un email para iniciar sesion", Toast.LENGTH_SHORT).show();
                }else if(!validarEmail(email_text)){
                    Toast.makeText(LoginActivity.this, "Introduce un email correcto para iniciar sesion", Toast.LENGTH_SHORT).show();
                }else if(pass_text.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Introduce tu contraseña para iniciar sesion", Toast.LENGTH_SHORT).show();
                }else{
                    Login("jose.berrio@escuelaestech.es", "estech", new Async() {
                    @Override
                    public void response(Respuesta respuesta) {
                        if(respuesta != null){
                                int code = respuesta.getCode();
                                if(code == 200){
                                    JsonObject data = respuesta.getData();
                                    Gson gson = new Gson();
                                    User_Login user = gson.fromJson(data,User_Login.class);
                                    token = user.getToken();
                                    id_user = user.getId();
                                    editor.putString("token", token);
                                    editor.putString("user_login", email_text);
                                    editor.putString("user_pass", pass_text);
                                    editor.putInt("id_user", id_user);
                                    editor.apply();
                                    Intent myIntent = new Intent(LoginActivity.this, MainPageActivity.class);
                                    LoginActivity.this.startActivity(myIntent);
                                    finish();
                                }else{
                                    Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                        }
                    }
                });
                }
            }
        });

        register_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

//        if(!sharedPreferences.getString("user_login", "").equals("")){
//            String email_guardado = sharedPreferences.getString("user_login", "");
//            String pass_guardado = sharedPreferences.getString("user_pass", "");
//            email_field.setVisibility(View.GONE);
//            pass_field_textinputext.setVisibility(View.GONE);
//            register_login.setVisibility(View.GONE);
//            olvida.setVisibility(View.GONE);
//            login.setVisibility(View.GONE);
//            showProgressBar(true);
//            Login(email_guardado, pass_guardado, new Async() {
//                @Override
//                public void response(Respuesta respuesta) {
//                    if(respuesta != null){
//                        int code = respuesta.getCode();
//                        if(code == 200){
//                            JsonObject data = respuesta.getData();
//                            Gson gson = new Gson();
//                            User_Login user = gson.fromJson(data,User_Login.class);
//                            token = user.getToken();
//                            id_user = user.getId();
//                            editor.putString("token", token);
//                            editor.putInt("id_user", id_user);
//                            editor.apply();
//                            Intent myIntent = new Intent(LoginActivity.this, MainPageActivity.class);
//                            LoginActivity.this.startActivity(myIntent);
//                            showProgressBar(false);
//                            finish();
//                        }else{
//                            Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//            });
//        }
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void showProgressBar(Boolean bool){
        if(bool){
            progressBar.setVisibility(View.VISIBLE);
        }else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void Login(String email, String password, Async callback) {
        loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder = new OkHttpClient.Builder().addInterceptor(loggingInterceptor);


        retrofit = new Retrofit.Builder()
                .baseUrl(WebServiceClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClientBuilder.build())
                .build();
        WebServiceClient client = retrofit.create(WebServiceClient.class);

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("email", email);
        hashMap.put("password", password);

        Call<Respuesta> llamada = client.doLogin(hashMap);
        llamada.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                Respuesta respuesta = response.body();
                callback.response(respuesta);
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {
                showProgressBar(false);
            }
        });

    }

    private interface Async{
        void response(Respuesta respuesta);
    }
}