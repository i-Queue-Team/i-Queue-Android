package com.example.i_queue.models;

import com.google.gson.JsonObject;

public class Respuesta_Login {
    private String status, message, token;
    private JsonObject data;
    private Validation_Errors validation_errors;

    public Respuesta_Login(String status, String message, String token, JsonObject data, Validation_Errors validation_errors) {
        this.status = status;
        this.message = message;
        this.token = token;
        this.data = data;
        this.validation_errors = validation_errors;
    }

    public void setValidator_errors(Validation_Errors validation_errors) {
        this.validation_errors = validation_errors;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }

    public Validation_Errors getValidator_errors() {
        return validation_errors;
    }
}
