package com.example.i_queue.models;

import com.google.gson.JsonObject;

public class Respuesta {
    private String status, message;
    private JsonObject data;

    public Respuesta(String status, String message, JsonObject data) {
        
        this.status = status;
        this.message = message;
        this.data = data;
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

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }
}
