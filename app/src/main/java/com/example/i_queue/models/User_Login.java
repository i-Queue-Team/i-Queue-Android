package com.example.i_queue.models;

public class User_Login {
    private int id;
    private String name, emial, email_verified_at, role, created_at, updated_at, token;

    public User_Login(int id, String name, String emial, String email_verified_at, String role, String created_at, String updated_at, String token) {
        this.id = id;
        this.name = name;
        this.emial = emial;
        this.email_verified_at = email_verified_at;
        this.role = role;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmial() {
        return emial;
    }

    public void setEmial(String emial) {
        this.emial = emial;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public void setEmail_verified_at(String email_verified_at) {
        this.email_verified_at = email_verified_at;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
