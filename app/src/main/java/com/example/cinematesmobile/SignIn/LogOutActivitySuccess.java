package com.example.cinematesmobile.SignIn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinematesmobile.Frag.FragmentActivity;
import com.example.cinematesmobile.Main.WelcomeActivity;
import com.example.cinematesmobile.R;

public class LogOutActivitySuccess extends AppCompatActivity {

    private Button TornaIndietro;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout_success);
        TornaIndietro = findViewById(R.id.successo);

        TornaIndietro.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(LogOutActivitySuccess.this, WelcomeActivity.class);
                startActivity(intent);
            }
        });
        }
    }
