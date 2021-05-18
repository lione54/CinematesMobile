package com.example.cinematesmobile.SignIn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinematesmobile.Frag.FragmentActivity;
import com.example.cinematesmobile.Main.WelcomeActivity;
import com.example.cinematesmobile.R;

import io.paperdb.Paper;

public class LogOutActivitySuccess extends AppCompatActivity {

    private Button TornaIndietro;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout_success);
        TornaIndietro = findViewById(R.id.successo);
        Paper.init(this);
        TornaIndietro.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Paper.book().delete("UserProprietario");
                Paper.book().delete("Passwd");
                Intent intent = new Intent(LogOutActivitySuccess.this, WelcomeActivity.class);
                startActivity(intent);
            }
        });
        }
    }
