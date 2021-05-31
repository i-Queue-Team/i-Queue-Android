package com.example.i_queue.models;

import com.google.gson.JsonObject;

public class Respuesta {
    private int code;
    private String message;
    private JsonObject data, errors;

    public Respuesta(int code, String message, JsonObject data, JsonObject errors) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.errors = errors;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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

    public JsonObject getErrors() {
        return errors;
    }

    public void setErrors(JsonObject errors) {
        this.errors = errors;
    }
}
