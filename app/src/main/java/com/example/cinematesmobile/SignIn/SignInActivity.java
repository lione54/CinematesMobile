package com.example.cinematesmobile.SignIn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cinematesmobile.Main.WelcomeActivity;
import com.example.cinematesmobile.R;

public class SignInActivity extends AppCompatActivity {
    private Button Continua;
    private  Button Indietro;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Continua = findViewById(R.id.continua);
        Indietro = findViewById(R.id.indietro);
        Continua.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, ConfirmCodeActivity.class);
                startActivity(intent);
            }
        });
        Indietro.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}