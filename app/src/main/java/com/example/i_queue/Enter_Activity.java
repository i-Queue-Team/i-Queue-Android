package com.example.i_queue;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Enter_Activity extends AppCompatActivity {

    private Button enter;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_layout);

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);

        String user = sharedPreferences.getString("user_name", "");

        toolbar = findViewById(R.id.toolbar_enter);

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("white"));
        setTitle(user);

        enter = findViewById(R.id.continue_enter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Enter_Activity.this, MainPageActivity.class);
                Enter_Activity.this.startActivity(myIntent);
            }
        });
    }


}
