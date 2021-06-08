package com.example.i_queue;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

public class StoreActivity extends AppCompatActivity {

    private String name_commerce, image, descripción_text;
    private TextView name, descripción;
    private ImageView imageView;
    private Toolbar store_tool;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_activity);
        Bundle extras = getIntent().getExtras();
        name = findViewById(R.id.name_commerce);
        imageView = findViewById(R.id.image_commerce);
        descripción = findViewById(R.id.details);
        store_tool = findViewById(R.id.toolbar_store);
        name_commerce = extras.getString("name");
        image = extras.getString("image");
        descripción_text = extras.getString("description");
        descripción.setText(descripción_text);
        Picasso.get().load(image).into(imageView);
        name.setText(name_commerce);
        setSupportActionBar(store_tool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(name_commerce);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();

    }
}
