package com.example.i_queue;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StoreActivity extends AppCompatActivity {

    private String name_commerce;
    private TextView name, titulo;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_activity);
        Bundle extras = getIntent().getExtras();
        name = findViewById(R.id.name_commerce);
        titulo = findViewById(R.id.titulo);
        name_commerce = extras.getString("name");

        name.setText(name_commerce);
        titulo.setText(name_commerce);
    }
}
