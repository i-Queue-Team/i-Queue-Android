package com.example.i_queue.models;

import java.util.List;

public class List_Shops {
    private List<Data> commerce;

    public List_Shops(List<Data> commerce) {
        this.commerce = commerce;
    }

    public List<Data> getCommerces() {
        return commerce;
    }

    public void setCommerces(List<Data> commerce) {
        this.commerce = commerce;
    }
}
