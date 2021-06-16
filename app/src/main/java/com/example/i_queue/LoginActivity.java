package com.example.i_queue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.i_queue.models.Respuesta;
import com.example.i_queue.models.User_Login;
import com.example.i_queue.webservice.WebServiceClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.messaging.FirebaseMessaging;
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

import static android.content.ContentValues.TAG;

public class LoginActivity extends AppCompatActivity {

    private EditText email_field, pass_field;
    private TextInputLayout ter1, ter2;
    private String email_text, pass_text, token, remember_token, user_name;
    private int id_user;
    private Button login, register_login;
    private ProgressBar progressBar;
    private Retrofit retrofit;
    private TextView olvida, cargando;
    private HttpLoggingInterceptor loggingInterceptor;
    private OkHttpClient.Builder httpClientBuilder;
    private Toolbar toolbar;
    private AlertDialog show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("i-Queue");
        SharedPreferences.Editor editor = getSharedPreferences("prefs", MODE_PRIVATE).edit();
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("white"));
        setSupportActionBar(toolbar);
        ter2 = findViewById(R.id.ter2);
        cargando = findViewById(R.id.cargando);
        email_field = findViewById(R.id.email_log);
        progressBar = findViewById(R.id.progressBar);
        olvida = findViewById(R.id.olvida);
        SpannableString texto = new SpannableString("¿Has olvidado tu contraseña?");
        texto.setSpan(new UnderlineSpan(), 0, texto.length(), 0);
        olvida.setText(texto);
        ter1 = findViewById(R.id.ter1);
        pass_field = findViewById(R.id.password_log);
        login = findViewById(R.id.login);
        register_login = findViewById(R.id.register_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_text = email_field.getText().toString();
                pass_text = pass_field.getText().toString();

                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                    return;
                                }
                                // Get new FCM registration token
                                token = task.getResult();
                                remember_token = token;
                                // Log and toast
                                Log.d("TAG", token);

                                if(email_text.isEmpty()){
                                    Toast.makeText(LoginActivity.this, "Introduce un email para iniciar sesion", Toast.LENGTH_SHORT).show();
                                }else if(!validarEmail(email_text)){
                                    Toast.makeText(LoginActivity.this, "Introduce un email correcto para iniciar sesion", Toast.LENGTH_SHORT).show();
                                }else if(pass_text.isEmpty()){
                                    Toast.makeText(LoginActivity.this, "Introduce tu contraseña para iniciar sesion", Toast.LENGTH_SHORT).show();
                                }else{
                                    Login(email_text, pass_text, remember_token, new Async() {
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
                                                    user_name = user.getName();
                                                    editor.putString("token", token);
                                                    editor.putString("user_login", email_text);
                                                    editor.putString("user_pass", pass_text);
                                                    editor.putInt("id_user", id_user);
                                                    editor.putString("user_name", user_name);
                                                    editor.apply();
                                                    Intent myIntent = new Intent(LoginActivity.this, MainPageActivity.class);
                                                    LoginActivity.this.startActivity(myIntent);
                                                    finish();
                                                }else if (code == 401){
                                                    Toast.makeText(LoginActivity.this, "Las credenciales son invalidas", Toast.LENGTH_SHORT).show();
                                                }
                                            }else{
                                                Toast.makeText(LoginActivity.this, "El email introducido es invalido", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
            }
        });

        register_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        olvida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show = alertDialog();
                show.show();
            }
        });

        if(!sharedPreferences.getString("user_login", "").equals("")){

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                return;
                            }
                            // Get new FCM registration token
                            token = task.getResult();
                            remember_token = token;
                            // Log and toast
                            Log.d("TAG", token);

                            String email_guardado = sharedPreferences.getString("user_login", "");
                            String pass_guardado = sharedPreferences.getString("user_pass", "");
                            email_field.setVisibility(View.GONE);
                            ter1.setVisibility(View.GONE);
                            register_login.setVisibility(View.GONE);
                            cargando.setVisibility(View.VISIBLE);
                            olvida.setVisibility(View.GONE);
                            ter2.setVisibility(View.GONE);
                            login.setVisibility(View.GONE);
                            showProgressBar(true);
                            Login(email_guardado, pass_guardado,remember_token ,new Async() {
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
                                            user_name = user.getName();
                                            editor.putString("token", token);
                                            editor.putInt("id_user", id_user);
                                            editor.putString("user_name" , user_name);
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
                    });
        }
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

    private void Login(String email, String password, String remember_token_firebase, Async callback) {
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
        hashMap.put("remember_token_firebase", remember_token_firebase);

        Call<Respuesta> llamada = client.doLogin(hashMap);
        llamada.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                Respuesta respuesta = response.body();
                callback.response(respuesta);
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "La petición de login fallo", Toast.LENGTH_SHORT).show();
                showProgressBar(false);
            }
        });

    }

    private void ForgottenPassword(String email){
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

        Call<Respuesta> llamada = client.forgottenPassword(hashMap);
        llamada.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                if(response.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "El email se envio correctamente", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(LoginActivity.this, "El email no se envio correctamente", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "El email no se envio correctamente", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private interface Async{
        void response(Respuesta respuesta);
    }


    private AlertDialog alertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

        LayoutInflater inflater = LoginActivity.this.getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_login, null);

        builder.setView(v);

        Button cancelar = (Button) v.findViewById(R.id.cancelar_lg);
        Button cambiar = (Button) v.findViewById(R.id.cambiar_lg);
        TextInputEditText email_field = (TextInputEditText) v.findViewById(R.id.email_field_login);

        cambiar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email_recupera = String.valueOf(email_field.getText());
                        if(email_recupera.equals("")){
                            Toast.makeText(LoginActivity.this, "Introduce un email", Toast.LENGTH_SHORT).show();
                        }else if(!validarEmail(email_recupera)){
                            Toast.makeText(LoginActivity.this, "Introduce un email valido", Toast.LENGTH_SHORT).show();
                        }else{
                            ForgottenPassword(email_recupera);
                            show.dismiss();
                        }
                    }
                }
        );

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();
            }
        });

        return builder.create();
    }
}