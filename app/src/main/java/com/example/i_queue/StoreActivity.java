package com.example.i_queue;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

public class StoreActivity extends AppCompatActivity {

    private String name_commerce, image, descripción_text, latitude, longitude;
    private TextView name, descripción;
    private ImageView imageView;
    private Toolbar store_tool;
    private Button map;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_activity);

        name = findViewById(R.id.name_commerce);
        map = findViewById(R.id.map_button);
        imageView = findViewById(R.id.image_commerce);
        descripción = findViewById(R.id.details);
        store_tool = findViewById(R.id.toolbar_store);

        Bundle extras = getIntent().getExtras();
        name_commerce = extras.getString("name");
        image = extras.getString("image");
        descripción_text = extras.getString("description");
        latitude = extras.getString("latitude");
        longitude = extras.getString("longitude");

        descripción.setText(descripción_text);
        Picasso.get().load(image).into(imageView);
        name.setText(name_commerce);

        setSupportActionBar(store_tool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(name_commerce);
        store_tool.setTitleTextColor(Color.WHITE);


        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String labelLocation = "Titulo: "+ name_commerce;
                String uri = "geo:<" + latitude + ">,<" + longitude+ ">?q=<" + latitude + ">,<" + longitude + ">(" + labelLocation + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();

    }
}
