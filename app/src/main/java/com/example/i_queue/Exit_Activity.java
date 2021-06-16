package com.example.i_queue;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Exit_Activity extends AppCompatActivity {

    private Button enter;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exit_layout);

        enter = findViewById(R.id.exit_exit);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Exit_Activity.this, MainPageActivity.class);
                Exit_Activity.this.startActivity(myIntent);
            }
        });
    }


}