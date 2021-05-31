package com.example.i_queue;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainPageActivity extends AppCompatActivity {

    private ImageView settings;
    private FloatingActionButton qr_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage_layout);
        settings = findViewById(R.id.settings);
        qr_button = findViewById(R.id.qr);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPageActivity.this, Settings_class.class);
                startActivity(intent);
            }
        });

        qr_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int MY_CAMERA_REQUEST_CODE = 100;
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                } else {
                    Intent myIntent = new Intent(MainPageActivity.this, QrCodeScanner.class);
                    MainPageActivity.this.startActivity(myIntent);
                    finish();
                }
            }
        });

        Intent intent = getIntent();
        String qrCode = intent.getStringExtra("readedData");
        if(qrCode != null){

        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                navigateToFragment(id);
                return true;
            }
        });

        navigateToFragment(R.id.Queue);

    }

    private void navigateToFragment(int itemId){

        Fragment fragment;

        String title = "i-Queue";

        switch (itemId){
            default:
                setTitle("Queue");
                fragment = new Fragment_queue();
                break;
            case R.id.Queue:
                setTitle("Queue");
                fragment = new Fragment_queue();
                break;
            case R.id.Tiendas:
                setTitle("Tiendas");
                fragment = new Fragment_library();
                break;
            case R.id.Mapa:
                setTitle("Mapa");
                fragment = new Fragment_map();
                break;
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();

        setTitle(title);
    }
}
