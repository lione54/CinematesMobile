package com.example.cinematesmobile.Search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.cinematesmobile.R;

public class CreaListePopUp extends AppCompatActivity {
    private AppCompatButton Conferma,Annulla;
    private AppCompatEditText InserisciTitolo;
    public static String Titolo;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crea_liste_pop_up);
    }
}