package com.example.cinematesmobile.Recensioni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.cinematesmobile.Main.WelcomeActivity;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Search.MovieDetailActivity;
import com.example.cinematesmobile.SignIn.SignInActivity;

public class RecensioniActivity extends AppCompatActivity {

    private Integer Id_Film;
    private String Titolo_film;
    private String Poster;
    private AppCompatTextView TitoloFilmPerRecensioni;
    private AppCompatButton ScriviRecensione;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recensioni);
        Id_Film = getIntent().getExtras().getInt("Id_Film");
        Titolo_film = getIntent().getExtras().getString("Titolo_Film");
        Poster= getIntent().getExtras().getString("Immagine_Poster");
        TitoloFilmPerRecensioni = findViewById(R.id.Titolo_film_per_recensioni);
        ScriviRecensione = findViewById(R.id.button_scrivi_recensione);
        TitoloFilmPerRecensioni.setText(Titolo_film);
        ScriviRecensione.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent2 = new Intent(RecensioniActivity.this, ScriviRecensioneActivity.class);
                startActivity(intent2);
            }
        });
    }
}