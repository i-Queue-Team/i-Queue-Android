package com.example.i_queue.models;

import java.util.List;

public class ListShops {
    private String status;
    private List<Commerces> commerces;

    public ListShops(String status, List<Commerces> commerces) {
        this.status = status;
        this.commerces = commerces;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Commerces> getCommerces() {
        return commerces;
    }

    public void setCommerces(List<Commerces> commerces) {
        this.commerces = commerces;
    }
}
