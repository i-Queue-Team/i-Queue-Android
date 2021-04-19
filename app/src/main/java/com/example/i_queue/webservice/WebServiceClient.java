package com.example.i_queue.webservice;

import com.example.i_queue.models.Respuesta;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface WebServiceClient {

    public static final String BASE_URL = "";

    @Headers({"Content-Type: application/json", "Accept: */*"})
    @POST("register")
    Call<Respuesta> Register (@Body String user, String email, String password);
}
