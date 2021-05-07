package com.example.i_queue;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage_layout);

        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                navigateToFragment(id);
                return true;
            }
        });

        navigateToFragment(R.id.nav_queue);

    }

    private void navigateToFragment(int itemId){

        Fragment fragment = new Fragment_queue();

        String title = "Queue";

        switch (itemId){
            default:
                title = "Queue";
                fragment = new Fragment_queue();
                break;
            case R.id.nav_queue:
                title = "Queue";
                fragment = new Fragment_queue();
                break;
            case R.id.nav_map:
                title = "Mapa";
                fragment = new Fragment_map();
                break;
            case R.id.nav_store:
                title = "Tiendas";
                fragment = new Fragment_library();
                break;
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        setTitle(title);
    }
}
