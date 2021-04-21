package com.example.i_queue;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.i_queue.models.Respuesta;
import com.example.i_queue.webservice.WebServiceClient;

import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText email_field, pass_field;
    private String email_text, pass_text;
    private Button login, register_login;
    private Retrofit retrofit;
    private HttpLoggingInterceptor loggingInterceptor;
    private OkHttpClient.Builder httpClientBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Login de i-Queue");

        email_field = findViewById(R.id.email_log);
        pass_field = findViewById(R.id.password_log);
        login = findViewById(R.id.login);
        register_login = findViewById(R.id.register_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_text = email_field.getText().toString();
                pass_text = pass_field.getText().toString();

                if(email_text.isEmpty()){
                    Toast.makeText(MainActivity.this, "Introduce un email para iniciar sesion", Toast.LENGTH_SHORT).show();
                }else if(!validarEmail(email_text)){
                    Toast.makeText(MainActivity.this, "Introduce un email correcto para iniciar sesion", Toast.LENGTH_SHORT).show();
                }else if(pass_text.isEmpty()){
                    Toast.makeText(MainActivity.this, "Introduce tu contraseña para iniciar sesion", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void Login(String email, String password){
        loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder = new OkHttpClient.Builder().addInterceptor(loggingInterceptor);


        retrofit = new Retrofit.Builder()
                .baseUrl(WebServiceClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClientBuilder.build())
                .build();
        WebServiceClient client = retrofit.create(WebServiceClient.class);
        Call<Respuesta> llamada = client.doLogin(email, password);
        llamada.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                Respuesta respuesta = response.body();
                if(respuesta != null){
                    if(response.isSuccessful()){
                        String status = respuesta.getStatus();
                        if(status == "success"){
                            Toast.makeText(MainActivity.this, "Has entrado", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(RegisterActivity.this, respuesta.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}