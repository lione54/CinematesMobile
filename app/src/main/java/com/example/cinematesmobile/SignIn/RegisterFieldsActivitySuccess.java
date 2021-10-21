package com.example.cinematesmobile.SignIn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinematesmobile.Frag.FragmentActivity;
import com.example.cinematesmobile.R;

import io.paperdb.Paper;

public class RegisterFieldsActivitySuccess extends AppCompatActivity {

    private Button Entra;
    private String UsernameProprietario, Passwd;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_succesful);
        Entra = findViewById(R.id.successo);
        UsernameProprietario = getIntent().getExtras().getString("UserProprietario");
        Passwd = getIntent().getExtras().getString("Passwd");
        Paper.init(this);
        Entra.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Paper.book().write("UserProprietario", UsernameProprietario);
                Paper.book().write("Passwd", Passwd);
                Intent home = new Intent(RegisterFieldsActivitySuccess.this, FragmentActivity.class);
                home.putExtra("UserProprietario", UsernameProprietario);
                startActivity(home);
            }
        });
        }
    }
