package com.example.i_queue.webservice;

import com.example.i_queue.models.Respuesta;
import com.example.i_queue.models.Respuesta_Library;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface WebServiceClient {

    public static final String BASE_URL = "http://10.144.110.119/i-Queue-BackEnd/public/api/";

    @Headers({"Content-Type: application/json", "Accept: */*"})
    @POST("register")
    Call<Respuesta> Register (@Body HashMap<String,String> user);

    @Headers({"Content-Type: application/json", "Accept: */*"})
    @POST("login")
    Call<Respuesta> doLogin (@Body HashMap<String,String> user);

    @Headers({"Content-Type: application/json", "Accept: */*"})
    @GET("commerces")
    Call<Respuesta_Library> getShops (@Header("Authorization") String authKey);
}
