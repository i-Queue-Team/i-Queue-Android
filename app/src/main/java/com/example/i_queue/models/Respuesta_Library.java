package com.example.i_queue.models;

import java.util.List;

public class Respuesta_Library {
    private int code;
    private String message;
    private List<Data> data;

    public Respuesta_Library(int code, String message, List<Data> data) {
        this.code = code;
        this.message = message;
        this.data = data;
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

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
}
