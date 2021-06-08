package com.example.i_queue.webservice;

import com.example.i_queue.models.Respuesta;
import com.example.i_queue.models.Respuesta_Library;
import com.example.i_queue.models.Respuesta_Queue;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

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

    @Headers({"Content-Type: application/json", "Accept: */*"})
    @POST("queue-verified-users")
    Call<Respuesta> enterQueue (@Header("Authorization") String authKey, @Body HashMap<String,String> queue);

    @Headers({"Content-Type: application/json", "Accept: */*"})
    @GET("queue-verified-users")
    Call<Respuesta_Queue> getQueue (@Header("Authorization") String authKey);

    @Headers({"Content-Type: application/json", "Accept: */*"})
    @DELETE("users/{user_id}")
    Call<Respuesta> deleteUser (@Header("Authorization") String authKey , @Path("user_id") String user_id);

    @Headers({"Content-Type: application/json", "Accept: */*"})
    @DELETE("queue-verified-users/{id}")
    Call<Respuesta> deleteQueue (@Header("Authorization") String authKey, @Path("id") String id);

}
