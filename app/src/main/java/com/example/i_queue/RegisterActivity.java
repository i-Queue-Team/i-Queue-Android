package com.example.i_queue;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.i_queue.models.Respuesta;
import com.example.i_queue.webservice.WebServiceClient;

import java.util.HashMap;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private String user_text, email_text, pass_text, confirm_pass_text;
    private Button cancel, register;
    private EditText user_editext, email_editext, pass_editext, confirm_pass_editext;
    private Retrofit retrofit;
    private HttpLoggingInterceptor loggingInterceptor;
    private OkHttpClient.Builder httpClientBuilder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        setTitle("Registro de i-Queue");

        Toolbar toolbar = findViewById(R.id.toolbar_2);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("white"));
        user_editext = findViewById(R.id.user);
        email_editext = findViewById(R.id.email);
        pass_editext = findViewById(R.id.password);
        confirm_pass_editext = findViewById(R.id.password_confirmed);
        cancel = findViewById(R.id.register_login);
        register = findViewById(R.id.login);
        
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_text = user_editext.getText().toString();
                email_text = email_editext.getText().toString();
                pass_text = pass_editext.getText().toString();
                confirm_pass_text = confirm_pass_editext.getText().toString();
                
                if(user_text.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Introduce un Usuario para poder continuar", Toast.LENGTH_SHORT).show();
                }else if(user_text.length() < 5){
                    Toast.makeText(RegisterActivity.this, "Introduce un usuario con mas de 4 caracteres", Toast.LENGTH_SHORT).show();
                }else if(email_text.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Introduce un email para continuar", Toast.LENGTH_SHORT).show();
                }else if(!validarEmail(email_text)){
                    Toast.makeText(RegisterActivity.this, "Introduce un email valido para continuar", Toast.LENGTH_SHORT).show();
                }else if(pass_text.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Introduce una contraseña para continuar", Toast.LENGTH_SHORT).show();
                }else if(pass_text.length() < 6){
                    Toast.makeText(RegisterActivity.this, "Introduce una contraseña mas larga de 6 caracteres", Toast.LENGTH_SHORT).show();
                }else if(confirm_pass_text.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Introduce la confirmacion de la contraseña para continuar", Toast.LENGTH_SHORT).show();
                }else if(!pass_text.equals(confirm_pass_text)){
                    Toast.makeText(RegisterActivity.this, "Las contraseñas introducidas no son las mismas, por favor introduzca las mismas para continuar", Toast.LENGTH_SHORT).show();
                }else{
                    Register(user_text, email_text, pass_text, new Async() {
                        @Override
                        public void response(Respuesta respuesta) {
                            if(respuesta != null){
                                    int code = respuesta.getCode();
                                    if(code == 200){
                                        Toast.makeText(RegisterActivity.this, respuesta.getMessage(), Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                            }
                        }
                    });
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private interface Async{
        void response(Respuesta respuesta);
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void Register(String user , String email , String password, Async callback){
        loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder = new OkHttpClient.Builder().addInterceptor(loggingInterceptor);


        retrofit = new Retrofit.Builder()
                .baseUrl(WebServiceClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClientBuilder.build())
                .build();
        WebServiceClient client = retrofit.create(WebServiceClient.class);

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("name", user);
        hashMap.put("email", email);
        hashMap.put("password", password);


        Call<Respuesta> llamada = client.Register(hashMap);
        llamada.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                Respuesta respuesta = response.body();
                if(response.isSuccessful()){
                    callback.response(respuesta);
                }else{
                    Toast.makeText(RegisterActivity.this, "Fallo el registro", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {

            }
        });

    }
}
