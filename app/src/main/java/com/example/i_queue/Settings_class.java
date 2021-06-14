package com.example.i_queue;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Settings_class extends AppCompatActivity {

    private Toolbar store_tool;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_settings, new PreferencesFragment()).commit();

        store_tool = findViewById(R.id.actionbar);
        setSupportActionBar(store_tool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Preferencias");
        store_tool.setTitleTextColor(Color.WHITE);
        store_tool.setOutlineAmbientShadowColor(Color.WHITE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}

