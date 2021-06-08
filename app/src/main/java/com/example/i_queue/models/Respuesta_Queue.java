package com.example.i_queue.models;

import com.google.gson.JsonArray;

public class Respuesta_Queue {
    private int code;
    private String message;
    private JsonArray data, errors;

    public Respuesta_Queue() {
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

    public JsonArray getData() {
        return data;
    }

    public void setData(JsonArray data) {
        this.data = data;
    }

    public JsonArray getErrors() {
        return errors;
    }

    public void setErrors(JsonArray errors) {
        this.errors = errors;
    }
}
