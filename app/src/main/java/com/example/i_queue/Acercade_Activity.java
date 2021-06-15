package com.example.i_queue;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static android.content.ContentValues.TAG;

public class Acercade_Activity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView fb, insta, gmail, tw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acercade);

        toolbar = findViewById(R.id.toolbar_acercade);

        fb = findViewById(R.id.fb);
        gmail = findViewById(R.id.gmail);
        tw = findViewById(R.id.tw);
        insta = findViewById(R.id.ins);

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facebookId = "fb://page/<Facebook Page ID>";
                String urlPage = "http://www.facebook.com/mypage";

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookId )));
                } catch (Exception e) {
                    Log.e(TAG, "Aplicación no instalada.");
                    //Abre url de pagina.
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPage)));
                }
            }
        });

        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","iqueuemaster@gmail.com\n", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Introduce tu motivo");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "*Ayudanos mejorar enviando, tu sugerencia o reportando un error*");
                startActivity(Intent.createChooser(emailIntent, "Enviar comentarios"));
            }
        });

        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://instagram.com/_u/i_Queue_Official");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("com.instagram.android");

                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {

                    //No encontró la aplicación, abre la versión web.
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/i_Queue_Official")));

                }
            }
        });

        tw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("Twitter://user?screen_name=i_queueApp"));
                    startActivity(intent);
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://Twitter.com/#!/i_queueApp")));
                }
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.parseColor("white"));
        setTitle("Acerca sobre nosotros");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
